package com.crt.service;

import com.crt.adapter.acao.AlphaVantageService;
import com.crt.adapter.acao.AlphaVantageService;
import com.crt.adapter.acao.BrapiService;
import com.crt.adapter.acao.BrapiService;
import com.crt.adapter.acao.TwelveService;
import com.crt.adapter.acao.TwelveService;
import com.crt.entity.Acao;
import com.crt.entity.Corretora;
import com.crt.repository.AcaoRepository;
import com.crt.repository.CorretoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AcaoService {

    @Autowired
    private AcaoRepository repository;

    @Autowired
    private BrapiService brapiAcaoService;

    @Autowired
    private TwelveService twelveDataAcaoService;

    @Autowired
    private AlphaVantageService alphaVantageAcaoService;

    @Autowired
    private CorretoraRepository corretoraRepository;

    public Acao cadastrar(String ticker, String mercado, String cnpj) {
        if (repository.findByTicker(ticker).isPresent()) {
            throw new RuntimeException("Ticker já cadastrado: " + ticker);
        }

        Corretora corretora = corretoraRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Corretora não encontrada"));

        Acao acao;
        if ("BR".equalsIgnoreCase(mercado)) {
            acao = brapiAcaoService.buscar(ticker);
        } else if ("EUA".equalsIgnoreCase(mercado)) {
            try {
                acao = twelveDataAcaoService.buscar(ticker);
            } catch (Exception e) {
                acao = alphaVantageAcaoService.buscar(ticker);
            }
        } else {
            throw new RuntimeException("Mercado inválido. Use BR ou EUA.");
        }

        acao.setCorretora(corretora); // <-- associa a corretora
        return repository.save(acao);
    }

    public List<Acao> listar() {
        return repository.findAll();
    }

    public Acao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ação não encontrada"));
    }

    public Acao buscarPorTicker(String ticker) {
        return repository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("Ticker não encontrado"));
    }

    public Acao atualizarCotacao(Long id) {
        Acao acao = buscarPorId(id);

        Acao atualizada;
        if ("BR".equalsIgnoreCase(acao.getMercado())) {
            atualizada = brapiAcaoService.buscar(acao.getTicker());
        } else {
            try {
                atualizada = twelveDataAcaoService.buscar(acao.getTicker());
            } catch (Exception e) {
                System.out.println("TwelveData falhou, tentando Alpha Vantage...");
                atualizada = alphaVantageAcaoService.buscar(acao.getTicker());
            }
        }

        acao.setCotacaoAtual(atualizada.getCotacaoAtual());
        acao.setDataHoraCotacao(LocalDateTime.now());
        return repository.save(acao);
    }
}
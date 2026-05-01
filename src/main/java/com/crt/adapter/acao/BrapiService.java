package com.crt.adapter.acao;

import com.crt.dto.BrapiResponseDTO;
import com.crt.entity.Acao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service("brapiAcaoService")
public class BrapiService implements AcaoApi {

    @Value("${brapi.token}")
    private String token;

    @Override
    public Acao buscar(String ticker) {
        RestTemplate rest = new RestTemplate();
        String url = "https://brapi.dev/api/quote/" + ticker + "?token=" + token;

        BrapiResponseDTO response = rest.getForObject(url, BrapiResponseDTO.class);

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            throw new RuntimeException("Ticker não encontrado na brapi: " + ticker);
        }

        BrapiResponseDTO.Result r = response.getResults().get(0);

        Acao acao = new Acao();
        acao.setTicker(r.getSymbol());
        acao.setNomeEmpresa(r.getLongName());
        acao.setMoeda(r.getCurrency());
        acao.setMercado("BR");
        acao.setCotacaoAtual(r.getRegularMarketPrice());
        acao.setDataHoraCotacao(LocalDateTime.now());
        return acao;
    }
}
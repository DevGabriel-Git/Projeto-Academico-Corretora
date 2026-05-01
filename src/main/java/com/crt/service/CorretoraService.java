package com.crt.service;

import com.crt.adapter.cep.CepService;
import com.crt.adapter.cnpj.CnpjService;
import com.crt.dto.CepResponseDTO;
import com.crt.dto.CnpjResponseDTO;
import com.crt.entity.Corretora;
import com.crt.repository.CorretoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CorretoraService {

    @Autowired
    private CnpjService cnpjService;

    @Autowired
    private CepService cepService;

    @Autowired
    private CorretoraRepository repository;

    public Corretora cadastrar(String cnpj){


        if (repository.findByCnpj(cnpj).isPresent()){
            throw new RuntimeException("CNPJ já cadastrado");
        }


        CnpjResponseDTO dados;
        try {
            dados = cnpjService.buscar(cnpj);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar dados do CNPJ");
        }

        CepResponseDTO cepDados = null;

        if (dados.getCep() != null && !dados.getCep().isEmpty()) {
            try {
                cepDados = cepService.buscar(dados.getCep());
            } catch (Exception e) {
                System.out.println("Erro ao buscar CEP");
            }
        }


        Corretora c = new Corretora();
        c.setCnpj(cnpj);
        c.setRazaoSocial(dados.getRazao_social());
        c.setNomeFantasia(dados.getNome_fantasia());
        c.setCep(dados.getCep());
        c.setUf(dados.getUf());
        c.setDataCadastro(LocalDateTime.now());
        c.setValidadaNaCvm(false);


        if (cepDados != null) {
            c.setCidade(cepDados.getLocalidade());
            c.setLogradouro(cepDados.getLogradouro());
            c.setBairro(cepDados.getBairro());
        }

        return repository.save(c);
    }
    public List<Corretora> listar() { return repository.findAll(); }

    public Corretora buscarPorId(Long id) { return repository.findById(id) .orElseThrow(() -> new RuntimeException("Corretora não encontrada"));
    }
    public Corretora buscarPorCnpj(String cnpj) { return repository.findByCnpj(cnpj) .orElseThrow(() -> new RuntimeException("CNPJ não encontrado"));
    }
}

package com.crt.adapter.cep;

import com.crt.dto.CepResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService implements CepService {

    @Override
    public CepResponseDTO buscar(String cep) {

        RestTemplate rest = new RestTemplate();
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        return rest.getForObject(url, CepResponseDTO.class);
    }
}
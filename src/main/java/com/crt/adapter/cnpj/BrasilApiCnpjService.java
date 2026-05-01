package com.crt.adapter.cnpj;

import com.crt.dto.CnpjResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BrasilApiCnpjService implements CnpjService {

    @Override
    public CnpjResponseDTO buscar(String cnpj) {

        RestTemplate rest = new RestTemplate();
        String url = "https://brasilapi.com.br/api/cnpj/v1/" + cnpj;

        return rest.getForObject(url, CnpjResponseDTO.class);
    }
}
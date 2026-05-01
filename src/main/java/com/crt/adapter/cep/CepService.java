package com.crt.adapter.cep;

import com.crt.dto.CepResponseDTO;

public interface CepService {
    CepResponseDTO buscar(String cep);
}

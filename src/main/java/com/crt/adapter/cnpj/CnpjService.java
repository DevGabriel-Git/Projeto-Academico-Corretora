package com.crt.adapter.cnpj;

import com.crt.dto.CnpjResponseDTO;

public interface CnpjService {
    CnpjResponseDTO buscar(String cnpj);
}

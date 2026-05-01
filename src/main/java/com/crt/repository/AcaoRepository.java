package com.crt.repository;

import com.crt.entity.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AcaoRepository extends JpaRepository<Acao, Long> {
    Optional<Acao> findByTicker(String ticker);
}
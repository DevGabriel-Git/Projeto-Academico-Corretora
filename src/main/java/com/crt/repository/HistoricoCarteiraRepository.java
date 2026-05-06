package com.crt.repository;

import com.crt.entity.HistoricoCarteira;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoCarteiraRepository extends JpaRepository<HistoricoCarteira, Long> {
    List<HistoricoCarteira> findByCarteiraId(Long carteiraId);
}

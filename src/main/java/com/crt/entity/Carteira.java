package com.crt.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "corretora_id")
    @JsonIgnore
    private Corretora corretora;

    @ManyToOne
    @JoinColumn(name = "acao_id")
    private Acao acao;

    private Integer quantidade;
    private BigDecimal precoMedio;
    private LocalDateTime dataAtualizacao;

    @JsonManagedReference
    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL)
    private List<HistoricoCarteira> historico = new ArrayList<>();
}
package com.crt.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Corretora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private Boolean validadaNaCvm;
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "corretora")
    @JsonBackReference
    private List<Acao> acoes = new ArrayList<>();
}
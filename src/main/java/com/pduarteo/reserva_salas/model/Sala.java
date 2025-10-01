package com.pduarteo.reserva_salas.model;

import com.pduarteo.reserva_salas.enums.Recurso;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private Integer andar;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "sala_recursos",
            joinColumns = @JoinColumn(name = "sala_id")
    )
    private Set<Recurso> recursos = new HashSet<>();

    private Boolean ativa = true;

    public Sala() {
    }

    public Sala(Long id, String nome, Integer capacidade, Integer andar, Set<Recurso> recursos, Boolean ativa) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.andar = andar;
        this.recursos = recursos;
        this.ativa = ativa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Integer getAndar() {
        return andar;
    }

    public void setAndar(Integer andar) {
        this.andar = andar;
    }

    public Set<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(Set<Recurso> recursos) {
        this.recursos = recursos;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}

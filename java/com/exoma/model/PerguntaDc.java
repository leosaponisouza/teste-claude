package com.exoma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "PerguntaDc")
@Table(name = "pergunta_dc", schema = "public")
public class PerguntaDc {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pergunta_dc_id_gen")
    @SequenceGenerator(name = "pergunta_dc_id_gen", sequenceName = "pergunta_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "texto", nullable = false)
    private String texto;

    @NotNull
    @Column(name = "flag_ativo", nullable = false)
    private Boolean flagAtivo = false;

    @Column(name = "tipo")
    private String tipo;

    @OneToMany(mappedBy = "pergunta")
    private Set<RespostaDc> respostaDcs = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Boolean getFlagAtivo() {
        return flagAtivo;
    }

    public void setFlagAtivo(Boolean flagAtivo) {
        this.flagAtivo = flagAtivo;
    }

    public Set<com.exoma.model.RespostaDc> getRespostaDcs() {
        return respostaDcs;
    }

    public void setRespostaDcs(Set<com.exoma.model.RespostaDc> respostaDcs) {
        this.respostaDcs = respostaDcs;
    }

}
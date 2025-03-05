package com.exoma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "resposta_dc")
public class RespostaDc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "participante_cpf", nullable = false)
    private com.exoma.pojo.participante participante;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private PerguntaDc pergunta;

    @Size(max = 10)
    @Column(name = "resposta", nullable = false, length = 10)
    private String resposta;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public com.exoma.pojo.participante getParticipante() {
        return participante;
    }

    public void setParticipante(com.exoma.pojo.participante participanteCpf) {
        this.participante = participanteCpf;
    }

    public PerguntaDc getPergunta() {
        return pergunta;
    }

    public void setPergunta(PerguntaDc pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
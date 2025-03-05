package com.exoma.dto;

import com.exoma.model.PerguntaDc;

public class RespostaDTO {

    private String resposta;
    private String descricao;
    private PerguntaDc perguntaDc;

    public PerguntaDc getPerguntaDc() {
        return perguntaDc;
    }

    public void setPerguntaDc(PerguntaDc perguntaDc) {
        this.perguntaDc = perguntaDc;
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

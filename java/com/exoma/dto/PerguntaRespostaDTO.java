package com.exoma.dto;

import com.exoma.model.PerguntaDc;

public class PerguntaRespostaDTO {
    private PerguntaDc pergunta;
    private RespostaDTO resposta;

    // Constructor
    public PerguntaRespostaDTO(PerguntaDc pergunta, RespostaDTO resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    // Getters and Setters
    public PerguntaDc getPergunta() {
        return pergunta;
    }

    public void setPergunta(PerguntaDc pergunta) {
        this.pergunta = pergunta;
    }

    public RespostaDTO getResposta() {
        return resposta;
    }

    public void setResposta(RespostaDTO resposta) {
        this.resposta = resposta;
    }
}

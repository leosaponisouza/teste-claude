package com.exoma.dto;

import java.util.ArrayList;
import java.util.List;

public class PerguntaRespostaGroup {
    private String tipo;
    private List<PerguntaRespostaDTO> perguntasRespostas;

    // Constructor
    public PerguntaRespostaGroup(String tipo) {
        this.tipo = tipo;
        this.perguntasRespostas = new ArrayList<>();
    }

    // Getters and Setters
    public String getTipo() {
        return tipo;
    }

    public List<PerguntaRespostaDTO> getPerguntasRespostas() {
        return perguntasRespostas;
    }

    public void addPerguntaResposta(PerguntaRespostaDTO prDTO) {
        this.perguntasRespostas.add(prDTO);
    }
}

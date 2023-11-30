package com.mygdx.game;

import java.util.List;

public class PerguntaDinossauro {
    private String pergunta;
    private List<Alternativa> alternativas;

    public PerguntaDinossauro(String pergunta, List<Alternativa> alternativas) {
        this.pergunta = pergunta;
        this.alternativas = alternativas;
    }

    public String getPergunta() {
        return pergunta;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }
}

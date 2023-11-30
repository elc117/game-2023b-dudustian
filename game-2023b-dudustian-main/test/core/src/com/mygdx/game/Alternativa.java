package com.mygdx.game;

public class Alternativa {
    private String texto;
    private boolean correta;

    public Alternativa(String texto, boolean correta) {
        this.texto = texto;
        this.correta = correta;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isCorreta() {
        return correta;
    }
}
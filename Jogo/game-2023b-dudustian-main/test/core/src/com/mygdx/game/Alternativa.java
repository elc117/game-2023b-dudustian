package com.mygdx.game;

import java.util.List;

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
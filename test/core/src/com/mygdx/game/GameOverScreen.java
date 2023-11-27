package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {
    private JogoVelha parent;
    private String victoryMessage;
    private BitmapFont font;
    private SpriteBatch batch;

    public GameOverScreen(String victoryMessage) {
        this.victoryMessage = victoryMessage;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show() {
        // Configurações de inicialização, se necessário
    }

    @Override
    public void renderGameOverScreen(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Lógica de renderização da tela de Game Over

        // Exemplo: Desenhe um texto indicando o Game Over
        parent.getBatch().begin();
        parent.getFont().draw(parent.getBatch(), victoryMessage, 0, );
        parent.getBatch().end();
        // Verifique se o jogador tocou na tela para retornar ao menu principal
        if (Gdx.input.justTouched()) {
            parent.setScreen(new MainMenuScreen(parent));
        }
    }

    // Implemente outros métodos da interface Screen conforme necessário

    @Override
    public void resize(int width, int height) {
        // Lógica de redimensionamento, se necessário
    }

    @Override
    public void pause() {
        // Lógica de pausa, se necessário
    }

    @Override
    public void resume() {
        // Lógica de retomada, se necessário
    }

    @Override
    public void hide() {
        // Lógica de ocultar, se necessário
    }

    @Override
    public void dispose() {
        // Lógica de limpeza, se necessário
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {
    private JogoVelha parent;
    private BitmapFont font;
    private SpriteBatch batch;
    private char winner;

    public GameOverScreen(JogoVelha parent, char winner) {
        this.parent = parent;
        this.winner = winner;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show() {
        // Configurações de inicialização, se necessário
    }

    @Override
    public void render(float delta) {
        clearScreen();

        drawVictoryMessage();
        drawOptions();

        handleInput();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(1, 1, 1, 1); // Cor branca
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void drawVictoryMessage() {
        batch.begin();
        font.draw(batch, "Jogador " + winner + " venceu!", 100, 300);
        batch.end();
    }

    private void drawOptions() {
        batch.begin();
        font.draw(batch, "Toque para reiniciar", 100, 200);
        font.draw(batch, "Segure para fechar", 100, 150);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            parent.setScreen(new GameScreen(parent)); // Reiniciar
        } else if (Gdx.input.isTouched()) {
            Gdx.app.exit(); // Fechar
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
        batch.dispose();
        font.dispose();
    }
}

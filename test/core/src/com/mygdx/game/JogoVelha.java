package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import com.mygdx.game.GameState;
import com.mygdx.game.GameScreen;
import com.mygdx.game.GameOverScreen;

public class JogoVelha extends Game {
    private GameState gameState;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private char winner;

    @Override
    public void create() {
        gameState = GameState.PLAYING;
        setScreen(new GameScreen(this));
    }


    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newState) {
        gameState = newState;
    }

    @Override
    public void render() {
        super.render();

        switch (gameState) {
            case PLAYING:
                if (!(getScreen() instanceof GameScreen)) {
                    setScreen(new GameScreen(this));
                }
                break;
            case GAME_OVER:
                if (!(getScreen() instanceof GameOverScreen)) {
                    setScreen(new GameOverScreen(this, winner));
                }
                break;
            // Adicione outros casos conforme necessário
        }
    }

    @Override
    public void dispose() {
        // Lógica de limpeza, se necessário
    }
}
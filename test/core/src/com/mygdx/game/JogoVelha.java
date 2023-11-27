package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameState;
import com.mygdx.game.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import com.mygdx.game.GameScreen;
import com.mygdx.game.GameOverScreen;
import com.mygdx.game.MainMenuScreen;

public class JogoVelha extends Game {
    private GameState gameState;
    private SpriteBatch batch;
    private Texture xTexture;
    private Texture oTexture;
    private Board board;
    private char currentPlayer;
    private MainMenuScreen mainMenuScreen;


    @Override
    public void create() {
        batch = new SpriteBatch();
        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
        xTexture = new Texture("x.jpg");
        oTexture = new Texture("o.jpg");
        board = new Board();
        currentPlayer = 'X';
        gameState = GameState.MAIN_MENU;
    }

    @Override
    public void render() {
        // Lógica de renderização do jogo
        switch (gameState) {
            case PLAYING:
                renderGameScreen();
                break;
            case GAME_OVER:
               renderGameOverScreen();
                break;
        }
    }


    private void update() {
        
    }


    public void setScreen(Screen screen) {
        Gdx.input.setInputProcessor(screen.getInputProcessor());
        ((Screen) getScreen()).dispose();
        screen.show();
        super.setScreen(screen);
    }


    @Override
    public void dispose() {
        xTexture.dispose();
        oTexture.dispose();
        batch.dispose();
    }
}
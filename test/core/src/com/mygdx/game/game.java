package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture xTexture;
    private Texture oTexture;
    private Board board;
    private char currentPlayer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        xTexture = new Texture("x.jpg"); 
        oTexture = new Texture("o.jpg");
        board = new Board();
        currentPlayer = 'X';
    }

    @Override
    public void render() {
        handleInput();
        update();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            int row = Gdx.input.getY() / (Gdx.graphics.getHeight() / 3);
            int col = Gdx.input.getX() / (Gdx.graphics.getWidth() / 3);
    
            if (board.isCellEmpty(row, col)) {
                board.setCell(row, col, currentPlayer);
    
                char winner = board.checkWinner();
                
                if (winner != ' ') {
                    gameOver(winner);
                }
    
                switchPlayer();
            }
        }
    }

    private void update() {
        
    }

    private void draw() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();

        // tabuleiro e as peças com base no estado atual do jogo
        drawBoard();
        drawPieces();

        batch.end();
    }

    private void drawBoard() {
        float cellWidth = Gdx.graphics.getWidth() / 3.0f;
        float cellHeight = Gdx.graphics.getHeight() / 3.0f;
    
        batch.setColor(0, 0, 0, 1);
        
        // linhas verticais
        for (int i = 1; i < 3; i++) {
            batch.draw(xTexture, i * cellWidth, 0, 2.0f, Gdx.graphics.getHeight());
        }
    
        // linhas horizontais
        for (int i = 1; i < 3; i++) {
            batch.draw(xTexture, 0, i * cellHeight, Gdx.graphics.getWidth(), 2.0f);
        }
    
        batch.setColor(1, 1, 1, 1);
    }

    private void drawPieces() {
        float cellWidth = Gdx.graphics.getWidth() / 3.0f;
        float cellHeight = Gdx.graphics.getHeight() / 3.0f;
    
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float x = j * cellWidth;
                float y = (2 - i) * cellHeight;
    
                if (board.getCell(i, j) == 'X') {
                    batch.draw(xTexture, x, y, cellWidth, cellHeight);
                } else if (board.getCell(i, j) == 'O') {
                    batch.draw(oTexture, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private void gameOver(char winner) {
        System.out.println("jogador " + winner + " venceu");
    
    }

    @Override
    public void dispose() {
        batch.dispose();
        xTexture.dispose();
        oTexture.dispose();
    }
}
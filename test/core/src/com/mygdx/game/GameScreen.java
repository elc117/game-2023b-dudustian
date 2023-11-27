package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {

    private final JogoVelha parent;
    private String victoryMessage;

    public GameScreen(JogoVelha parent) {
    
        this.parent = parent;
    }

    @Override
    public void show() {
        // Lógica de inicialização, se necessário
    }

    @Override
    public void renderGameScreen() {
        handleInput();
        update();
        draw();
    }

    private void handleInput() { 
        if (Gdx.input.justTouched()) {
            if (gameState == GameState.GAME_OVER) {
                // ignora as jogadas se o jogo já terminou
                return;
            }
    
            int row = Gdx.input.getY() / (Gdx.graphics.getHeight() / 3);
            int col = Gdx.input.getX() / (Gdx.graphics.getWidth() / 3);
    
            if (board.isCellEmpty(row, col)) {
                board.setCell(row, col, currentPlayer);
    
                char winner = board.checkWinner();
    
                if (winner != ' ') {
                    gameOver(winner);
                } else {
                    switchPlayer();
                }
            }
        }
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
        float cellWidth = Gdx.graphics.getWidth() / 4.0f;
        float cellHeight = Gdx.graphics.getHeight() / 4.0f;
    
        batch.setColor(0, 0, 0, 1);
    
        // Centralizando o tabuleiro
        float offsetX = (Gdx.graphics.getWidth() - 3 * cellWidth) / 2.0f;
        float offsetY = (Gdx.graphics.getHeight() - 3 * cellHeight) / 2.0f;
    
        // linhas verticais
        for (int i = 1; i < 3; i++) {
            batch.draw(xTexture, offsetX + i * cellWidth - 1.0f, offsetY, 2.0f, 3 * cellHeight);
        }
    
        // linhas horizontais
        for (int i = 1; i < 3; i++) {
            batch.draw(xTexture, offsetX, offsetY + i * cellHeight - 1.0f, 3 * cellWidth, 2.0f);
        }
    
        batch.setColor(1, 1, 1, 1);
    }
    
    private void drawPieces() {
        float cellWidth = Gdx.graphics.getWidth() / 4.0f;
        float cellHeight = Gdx.graphics.getHeight() / 4.0f;
    
        // Centralizando o tabuleiro
        float offsetX = (Gdx.graphics.getWidth() - 3 * cellWidth) / 2.0f;
        float offsetY = (Gdx.graphics.getHeight() - 3 * cellHeight) / 2.0f;
    
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float x = offsetX + j * cellWidth;
                float y = offsetY + (2 - i) * cellHeight;
    
                if (board.getCell(i, j) == 'X') {
                    batch.draw(xTexture, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                } else if (board.getCell(i, j) == 'O') {
                    batch.draw(oTexture, x + 5, y + 5, cellWidth - 10, cellHeight - 10);
                }
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private void gameOver(char winner) {
        System.out.println("Jogador " + winner + " venceu");
        gameState = GameState.GAME_OVER;
    }

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
        // Lógica de resumo, se necessário
    }

    @Override
    public void hide() {
        // Lógica de ocultar a tela, se necessário
    }

    @Override
    public void dispose() {
        // Lógica de disposição (liberação de recursos), se necessário
    }
}

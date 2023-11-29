package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class JogoVelha extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture xTexture;
    private Texture oTexture;
    private Board board;
    private char currentPlayer;
    private boolean gameOver;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        xTexture = new Texture("x.jpg");
        oTexture = new Texture("o.jpg");
        board = new Board();
        currentPlayer = 'X';
        gameOver = false;
        font = new BitmapFont();
    }

    @Override
    public void render() {
        handleInput();
        update();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            if (gameOver) {
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
                } else if (isBoardFull()) {
                    System.out.println("Empate!");
                    gameOver = true;
                } else {
                    switchPlayer();
                }
            }
        }
    }

    private void update() {
        // lógica de atualização, se necessário
    }

    private void draw() {
    ScreenUtils.clear(1, 1, 1, 1);
    batch.begin();

    // Se o jogo terminou, exiba a mensagem de "Game Over" e os botões de reinício e sair
    if (gameOver) {
        font.getData().setScale(2);
        font.setColor(0, 0, 0, 1);

        char winner = board.checkWinner();
        String message = (winner != ' ') ? "Parabéns Jogador " + winner + "!" : "Empate!";

        font.draw(batch, message, Gdx.graphics.getWidth() / 2.0f - 120, Gdx.graphics.getHeight() / 2.0f + 50);

        font.getData().setScale(1);
        font.setColor(1, 1, 1, 1);

        // Desenhe os botões de reinício e fechar
        Texture restartButtonTexture = new Texture("restart_button.png");
        Texture exitButtonTexture = new Texture("exit_button.png");

        float buttonWidth = 250;
        float buttonHeight = 70;
        float buttonSpacing = 50;

        float restartButtonX = Gdx.graphics.getWidth() / 2.0f - buttonWidth - buttonSpacing / 2.0f;
        float exitButtonX = Gdx.graphics.getWidth() / 2.0f + buttonSpacing / 2.0f;

        float buttonY = Gdx.graphics.getHeight() / 2.0f - buttonHeight - 70;

        batch.draw(restartButtonTexture, restartButtonX, buttonY, buttonWidth, buttonHeight);
        batch.draw(exitButtonTexture, exitButtonX, buttonY, buttonWidth, buttonHeight);

        // Lógica para reiniciar o jogo ou sair ao tocar nos botões
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Verifique o botão de reinício
            if (touchX >= restartButtonX &&
                    touchX <= restartButtonX + buttonWidth &&
                    touchY >= buttonY &&
                    touchY <= buttonY + buttonHeight) {
                restartGame();
            }

            // Verifique o botão de fechar
            if (touchX >= exitButtonX &&
                    touchX <= exitButtonX + buttonWidth &&
                    touchY >= buttonY &&
                    touchY <= buttonY + buttonHeight) {
                Gdx.app.exit(); // Fechar o jogo
            }
        }
    } else {
        // Desenhe o tabuleiro e as peças apenas se o jogo não tiver terminado
        drawBoard();
        drawPieces();
    }

    batch.end();
    }


    private void drawBoard() {
        float cellWidth = Gdx.graphics.getWidth() / 4.0f;
        float cellHeight = Gdx.graphics.getHeight() / 4.0f;

        batch.setColor(0, 0, 0, 1);

        // centralizando o tabuleiro
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

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getCell(i, j) == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private void gameOver(char winner) {
        System.out.println("Jogador " + winner + " venceu");
        gameOver = true;
    }

    private void restartGame() {
        // Limpar o tabuleiro
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.setCell(i, j, ' ');
            }
        }

        currentPlayer = 'X';
        gameOver = false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        xTexture.dispose();
        oTexture.dispose();
    }
}

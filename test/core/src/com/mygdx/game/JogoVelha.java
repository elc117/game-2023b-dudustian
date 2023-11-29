package com.mygdx.game;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.mygdx.game.PerguntaDinossauro;


public class JogoVelha extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture xTexture;
    private Texture oTexture;
    private Board board;
    private char currentPlayer;
    private boolean gameOver;
    private BitmapFont font;
    private List<PerguntaDinossauro> perguntas;
    private PerguntaDinossauro perguntaAtual;
    private int indicePerguntaAtual = -1;
    private Texture buttonATexture;
    private Texture buttonBTexture;
    private Texture buttonCTexture;
    private Texture buttonDTexture;
    private boolean perguntaEmAndamento;

    private float buttonSpacing = 20;

    @Override
    public void create() {
        batch = new SpriteBatch();
        xTexture = new Texture("x.jpg");
        oTexture = new Texture("o.jpg");
        board = new Board();
        currentPlayer = 'X';
        gameOver = false;
        font = new BitmapFont();
        perguntas = new ArrayList<>();
        criarPerguntas();
        Collections.shuffle(perguntas);
        proximaPergunta();
        buttonATexture = new Texture("button_A.png");
        buttonBTexture = new Texture("button_B.png");
        buttonCTexture = new Texture("button_C.png");
        buttonDTexture = new Texture("button_D.png");
        perguntaEmAndamento = true;
    }

    @Override
    public void render() {
        handleInput();
        update();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            if (gameOver || perguntaEmAndamento) {
                // ignora as jogadas se o jogo já terminou ou se uma pergunta está em andamento
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
                    perguntaEmAndamento = true;
                    switchPlayer();
                }
            }
        }
    }

    private void update() {
    }

    private void draw() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
    
        if (perguntaEmAndamento == true) {
            drawPergunta();
            drawRespostas();
        } else if (!gameOver && perguntaEmAndamento == false) {
            drawBoard();
            drawPieces();
        } else {
            font.getData().setScale(2);
            font.setColor(0, 0, 0, 1);
    
            char winner = board.checkWinner();
            String message = (winner != ' ') ? "Parabéns Jogador " + winner + "!" : "Empate!";
    
            font.draw(batch, message, Gdx.graphics.getWidth() / 2.0f - 120, Gdx.graphics.getHeight() / 2.0f + 50);
    
            font.getData().setScale(1);
            font.setColor(1, 1, 1, 1);
    
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
    
            if (Gdx.input.justTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
    
                if (touchX >= restartButtonX && touchX <= restartButtonX + buttonWidth &&
                        touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                    perguntaEmAndamento = true;
                    restartGame();
                }
    
                if (touchX >= exitButtonX && touchX <= exitButtonX + buttonWidth &&
                        touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                    Gdx.app.exit(); // fecha o jogo
                }
            }
        }
    
        batch.end();
    }


    private void drawBoard() {
        float cellWidth = Gdx.graphics.getWidth() / 4.0f;
        float cellHeight = Gdx.graphics.getHeight() / 4.0f;

        batch.setColor(0, 0, 0, 1);

        // centraliza o tabuleiro
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

    private void drawPergunta() {
        float perguntaX = Gdx.graphics.getWidth() / 15.0f;  //posição horizontal
        float perguntaY = Gdx.graphics.getHeight() * 0.90f;  //posição vertical
        font.getData().setScale(2);  //tamanho da fonte
        font.draw(batch, perguntaAtual.getPergunta(), perguntaX, perguntaY);
        
        float respostaX = Gdx.graphics.getWidth() / 8.0f;  //posição horizontal
        float respostaY = perguntaY - 100;  //posição vertical
        for (Alternativa alternativa : perguntaAtual.getAlternativas()) {
            font.draw(batch, alternativa.getTexto(), respostaX, respostaY);
            respostaY -= 50; 
        }
        font.getData().setScale(1); 
    }

    private void drawRespostas() {
        float buttonWidth = 150; 
        float buttonHeight = 100; 
        float buttonSpacing = 40;  
    
        float buttonAX = Gdx.graphics.getWidth() / 2.0f - buttonWidth - buttonSpacing / 2.0f;
        float buttonBX = Gdx.graphics.getWidth() / 2.0f + buttonSpacing / 2.0f;
        float buttonCX = Gdx.graphics.getWidth() / 2.0f - buttonWidth - buttonSpacing / 2.0f;
        float buttonDX = Gdx.graphics.getWidth() / 2.0f + buttonSpacing / 2.0f;
    
        float buttonY = Gdx.graphics.getHeight() / 2.0f - buttonHeight - 70;
    
        batch.draw(buttonATexture, buttonAX, buttonY, buttonWidth, buttonHeight);
        batch.draw(buttonBTexture, buttonBX, buttonY, buttonWidth, buttonHeight);
        batch.draw(buttonCTexture, buttonCX, buttonY - buttonHeight - buttonSpacing, buttonWidth, buttonHeight);
        batch.draw(buttonDTexture, buttonDX, buttonY - buttonHeight - buttonSpacing, buttonWidth, buttonHeight);
    
        // verifica clique do mouse nos botões
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
    
            if (touchX >= buttonAX && touchX <= buttonAX + buttonWidth &&
                    touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                verificarResposta(0);  //resposta A
            }
    
            if (touchX >= buttonBX && touchX <= buttonBX + buttonWidth &&
                    touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                verificarResposta(1);  //resposta B
            }
    
            if (touchX >= buttonCX && touchX <= buttonCX + buttonWidth &&
                    touchY >= buttonY - buttonHeight - buttonSpacing && touchY <= buttonY - buttonHeight - buttonSpacing + buttonHeight) {
                verificarResposta(2);  //resposta C
            }
    
            if (touchX >= buttonDX && touchX <= buttonDX + buttonWidth &&
                    touchY >= buttonY - buttonHeight - buttonSpacing && touchY <= buttonY - buttonHeight - buttonSpacing + buttonHeight) {
                verificarResposta(3);  //resposta D
            }
        }
    }

    private void verificarResposta(int indiceResposta) {
        //resposta correspondente ao índice
        Alternativa respostaSelecionada = perguntaAtual.getAlternativas().get(indiceResposta);
    
        // verifica se resposta selecionada é correta
        if (respostaSelecionada.isCorreta()) {
            System.out.println("Resposta correta! Jogador pode realizar sua jogada.");
            perguntaEmAndamento = false;
            
        } else {
            System.out.println("Resposta incorreta! Próximo jogador.");
            //resposta estiver incorreta, passa para o próximo jogador
            switchPlayer();
            perguntaEmAndamento = true;
        }
        proximaPergunta();
    }

    private void proximaPergunta() {

        if (indicePerguntaAtual < perguntas.size() - 1) {
            indicePerguntaAtual++;
        } else {
            indicePerguntaAtual = 0;
            // embaralhador de perguntas
            Collections.shuffle(perguntas);
        }
    
        // atribui a pergunta atual com base no índice atual
        perguntaAtual = perguntas.get(indicePerguntaAtual);
    }

    private void restartGame() {
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

    private void criarPerguntas() {
        List<Alternativa> alternativas1 = Arrays.asList(
                new Alternativa("A) Brontossauro", false),
                new Alternativa("B) T-Rex", true),
                new Alternativa("C) Triceratops", false),
                new Alternativa("D) Diplodocus", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual é o maior dinossauro?", alternativas1));
    
        List<Alternativa> alternativas2 = Arrays.asList(
                new Alternativa("A) Cretáceo", false),
                new Alternativa("B) Jurássico", true),
                new Alternativa("C) Triássico", false),
                new Alternativa("D) Paleogene", false)
        );
        perguntas.add(new PerguntaDinossauro("Em que período geológico viveu o Tyrannosaurus Rex?", alternativas2));
    
        List<Alternativa> alternativas3 = Arrays.asList(
                new Alternativa("A) Velociraptor", false),
                new Alternativa("B) Stegosaurus", false),
                new Alternativa("C) Spinosaurus", true),
                new Alternativa("D) Ankylosaurus", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual dinossauro tinha espinhos nas costas?", alternativas3));
        
                List<Alternativa> alternativas4 = Arrays.asList(
                new Alternativa("A) Diplodocus", false),
                new Alternativa("B) Archaeopteryx", false),
                new Alternativa("C) Dimorphodon", true),
                new Alternativa("D) Quetzalcoatlus", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual desses dinossauros é um pterossauro?", alternativas4));

        List<Alternativa> alternativas5 = Arrays.asList(
                new Alternativa("A) Dilofossauro", true),
                new Alternativa("B) Allosaurus", false),
                new Alternativa("C) Brachiosaurus", false),
                new Alternativa("D) Compsognathus", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual dinossauro foi apelidado de \"demônio de choque\" e apareceu no filme Jurassic Park?", alternativas5));

    }
    
}

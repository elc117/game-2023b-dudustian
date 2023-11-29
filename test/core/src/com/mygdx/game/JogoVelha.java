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
import com.badlogic.gdx.graphics.Color;

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
    private Texture backgroundTexture;
    private Texture lineTexture;

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
        buttonATexture = new Texture("button_A2.png");
        buttonBTexture = new Texture("button_B2.png");
        buttonCTexture = new Texture("button_C2.png");
        buttonDTexture = new Texture("button_D2.png");
        perguntaEmAndamento = true;
        backgroundTexture = new Texture("background.png");
        lineTexture = new Texture("osso.png"); 
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
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
    
            Texture restartButtonTexture = new Texture("restart_button1.png");
            Texture exitButtonTexture = new Texture("exit_button1.png");
    
            float buttonWidth = 200;
            float buttonHeight = 150;
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
    
        float offsetX = (Gdx.graphics.getWidth() - 3 * cellWidth) / 2.0f;
        float offsetY = (Gdx.graphics.getHeight() - 3 * cellHeight) / 2.0f;
    
        batch.setColor(0, 0, 0, 1);  // Define a cor preta para desenhar as linhas
    
        // Desenha linhas verticais
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
                    batch.draw(xTexture, x + 5, y + 5, cellWidth - 40, cellHeight - 10);
                } else if (board.getCell(i, j) == 'O') {
                    batch.draw(oTexture, x + 5, y + 5, cellWidth - 40, cellHeight - 10);
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
        float buttonWidth = 180; 
        float buttonHeight = 180; 
        float buttonSpacing = 25;  

        float buttonAX = Gdx.graphics.getWidth() / 2.0f - buttonWidth - buttonSpacing / 2.0f;
        float buttonBX = Gdx.graphics.getWidth() / 2.0f + buttonSpacing / 2.0f;
        float buttonCX = Gdx.graphics.getWidth() / 2.0f - buttonWidth - buttonSpacing / 2.0f;
        float buttonDX = Gdx.graphics.getWidth() / 2.0f + buttonSpacing / 2.0f;
    
        float buttonY = Gdx.graphics.getHeight() / 2.0f - buttonHeight - 5;
    
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

    private void criarPerguntas() {
    
        List<Alternativa> alternativas1 = Arrays.asList(
                new Alternativa("A) Cretáceo", false),
                new Alternativa("B) Triássico", true),
                new Alternativa("C) Jurássico", false),
                new Alternativa("D) Paleogene", false)
        );
        perguntas.add(new PerguntaDinossauro("De que período geológico são os fósseis da Quarta Colônia?", alternativas1));
    
        List<Alternativa> alternativas2 = Arrays.asList(
                new Alternativa("A) Spinosaurus", false),
                new Alternativa("B) Paralititan", false),
                new Alternativa("C) Rincossauros", true),
                new Alternativa("D) Ankylosaurus", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual fóssil foi encontrado na Quarta Colônia?", alternativas2));
    
        List<Alternativa> alternativas3 = Arrays.asList(
                new Alternativa("A) Africana", false),
                new Alternativa("B) Portuguesa", false),
                new Alternativa("C) Italiana", true),
                new Alternativa("D) Indígena", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual é a influência cultural predominante na arquitetura da Quarta Colônia?", alternativas3));
    
        List<Alternativa> alternativas4 = Arrays.asList(
                new Alternativa("A) 9", true),
                new Alternativa("B) 5", false),
                new Alternativa("C) 11", false),
                new Alternativa("D) 7", false)
        );
        perguntas.add(new PerguntaDinossauro("Quantos municípios compõem a Quarta Colônia?", alternativas4));
    
        List<Alternativa> alternativas5 = Arrays.asList(
                new Alternativa("A) Restinga Seca", false),
                new Alternativa("B) São João do Polêsine", false),
                new Alternativa("C) Frederico Westphalen", true),
                new Alternativa("D) Agudo", false)
        );
        perguntas.add(new PerguntaDinossauro("Qual destas cidades não pertence à Quarta Colônia?", alternativas5));
    
        List<Alternativa> alternativas6 = Arrays.asList(
                new Alternativa("A) 1750", false),
                new Alternativa("B) 1850", true),
                new Alternativa("C) 1800", false),
                new Alternativa("D) 1900", false)
        );
        perguntas.add(new PerguntaDinossauro("Quando foi fundada a Quarta Colônia?", alternativas6));
    
    }

    @Override
    public void dispose() {
        batch.dispose();
        xTexture.dispose();
        oTexture.dispose();
        backgroundTexture.dispose();
    }
    
}
package com.mygdx.game;

public class Board {
    private char[][] cells;
    
    public Board() {
        cells = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = ' ';
            }
        }
    }
    

    public char getCell(int row, int col) {
        return cells[row][col];
    }

    public void setCell(int row, int col, char player) {
        cells[row][col] = player;
    }

    public boolean isCellEmpty(int row, int col) {
        return cells[row][col] == ' ';
    }

    public char checkWinner() {
    
        for (int i = 0; i < 3; i++) {
            if (cells[i][0] != ' ' && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2]) {
                return cells[i][0]; // vencedor linha
            }
            if (cells[0][i] != ' ' && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i]) {
                return cells[0][i]; // vencedor coluna
            }
        }
    
        if (cells[0][0] != ' ' && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2]) {
            return cells[0][0]; // vencedor diagonal principal
        }
        if (cells[0][2] != ' ' && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0]) {
            return cells[0][2]; // vencedor diagonal secundária
        }
    
        // venhum vencedor
        return ' ';
    }
    
    
}

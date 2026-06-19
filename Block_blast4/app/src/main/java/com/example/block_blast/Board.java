package com.example.block_blast;

import android.graphics.Canvas;

public class Board {
    static int width;
    static int height;
    int x;
    int y;

    public static int score = 0;
    public static Cell[][] boardArr = new Cell[8][8];

    public Board(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        generateCells();
    }

    public void generateCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int cellWidth = width / 8;
                int cellHeight = height / 8;
                int cellY = cellHeight * j + this.y;
                int cellX = cellWidth * i + this.x;
                boardArr[i][j] = new Cell(cellWidth, cellHeight, cellY, cellX, false);
            }
        }
    }

    public void drawBoard(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardArr[i][j].drawCell(canvas);
            }
        }
    }

    public void ClearLines() {
        boolean[] rowsToClear = new boolean[8];
        boolean[] colsToClear = new boolean[8];

        for (int j = 0; j < 8; j++) {
            boolean isRowFull = true;
            for (int i = 0; i < 8; i++) {
                if (!boardArr[i][j].isYes()) {
                    isRowFull = false;
                    break;
                }
            }
            if (isRowFull) {
                rowsToClear[j] = true;
                score += 100;
            }
        }

        for (int i = 0; i < 8; i++) {
            boolean isColFull = true;
            for (int j = 0; j < 8; j++) {
                if (!boardArr[i][j].isYes()) {
                    isColFull = false;
                    break;
                }
            }
            if (isColFull) {
                colsToClear[i] = true;
                score += 100;
            }
        }

        for (int j = 0; j < 8; j++) {
            if (rowsToClear[j]) {
                for (int i = 0; i < 8; i++) boardArr[i][j].setYes(false);
            }
        }
        for (int i = 0; i < 8; i++) {
            if (colsToClear[i]) {
                for (int j = 0; j < 8; j++) boardArr[i][j].setYes(false);
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getX() { return x; }
    public int getY() { return y; }
}


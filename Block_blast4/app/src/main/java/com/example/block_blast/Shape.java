package com.example.block_blast;

import android.graphics.Canvas;

public class Shape {
    int startx, starty;
    int[][] template;
    Cell[][] shapeArr = new Cell[3][3];
    int cellWidth, cellHeight;

    public Shape(int[][] template, int startX, int startY) {
        this.template = template;
        this.startx = startX;
        this.starty = startY;
    }

    public void changeScale(double scale) {
        this.cellWidth = (int) ((Board.width / Board.boardArr.length) * scale);
        this.cellHeight = (int) ((Board.height / Board.boardArr.length) * scale);
        generateCellsShape();
    }

    public void generateCellsShape() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = (this.cellWidth * i) + this.startx;
                int y = (this.cellHeight * j) + this.starty;
                shapeArr[i][j] = new Cell(this.cellWidth, this.cellHeight, x, y, true);
            }
        }
    }

    public boolean isTouched(float Mx, float My) {
        int sizeW = this.cellWidth * 3;
        int sizeH = this.cellHeight * 3;
        return Mx >= startx && Mx <= startx + sizeW &&
                My >= starty && My <= starty + sizeH;
    }

    public void updatePosition(int newX, int newY) {
        this.startx = newX;
        this.starty = newY;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                shapeArr[i][j].setCx((this.cellWidth * i) + this.startx);
                shapeArr[i][j].setCy((this.cellHeight * j) + this.starty);
            }
        }
    }

    public void drawShape(Canvas canvas) {
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[i].length; j++) {
                if (template[i][j] == 1) {
                    shapeArr[i][j].drawCell(canvas);
                }
            }
        }
    }

    public int getStartx() { return startx; }
    public int getStarty() { return starty; }
}

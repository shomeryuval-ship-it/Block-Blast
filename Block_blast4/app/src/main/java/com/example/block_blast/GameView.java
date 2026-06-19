package com.example.block_blast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.Random;

public class GameView extends View {
    Board board;
    Shape[] ThreeShapes = new Shape[3];

    Random rand = new Random();

    Shape draggedShape = null;
    float dL, dT;

    int startX, startY;
    boolean gameOver = false;

    private boolean isTimerModeActive = false;
    private int timerSecondsLeft = 60;

    public GameView(Context context, Board b) {
        super(context);
        board = b;

        generateNewShapes();
    }

    public void setTimerModeActive(boolean active) {
        this.isTimerModeActive = active;
    }

    public void updateTimerText(int seconds) {
        this.timerSecondsLeft = seconds;
        invalidate();
    }

    private void generateNewShapes() {
        int screenWidth = 1080;
        int screenHeight = 1920;

        int shapeWidth = (int) (screenWidth * 0.25);
        int spaceBetween = (int) (screenWidth * 0.04);
        int WidthOfAllShapes = (3 * shapeWidth) + (2 * spaceBetween);

        int startRowX = (((screenWidth - WidthOfAllShapes) / 2) - (shapeWidth / 2)) + 170;
        int shapeY = (int) (screenHeight * 0.75);

        for (int i = 0; i < 3; i++) {
            int randomShape = rand.nextInt(ShapeTemplates.ALL.length);
            int[][] selectedTemplate = ShapeTemplates.ALL[randomShape];

            int shapeX = startRowX + (i * (shapeWidth + spaceBetween));

            ThreeShapes[i] = new Shape(selectedTemplate, shapeX, shapeY);

            ThreeShapes[i].changeScale(0.8);
        }
    }

    private boolean isGameOver() {
        for (int k = 0; k < 3; k++) {
            Shape s = ThreeShapes[k];
            if (s != null) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (canPlace(s, j, i)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver) return false;

        float Mx = event.getX();
        float My = event.getY();
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < 3; i++) {
                Shape s = ThreeShapes[i];
                if (s != null && s.isTouched(Mx, My)) {
                    draggedShape = s;

                    startX = s.getStartx();
                    startY = s.getStarty();

                    draggedShape.changeScale(1.0);

                    dL = s.getStartx() - Mx;
                    dT = s.getStarty() - My;
                    break;
                }
            }
        }

        else if (action == MotionEvent.ACTION_MOVE) {
            if (draggedShape != null) {
                draggedShape.updatePosition((int) (Mx + dL), (int) (My + dT));
                invalidate();
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (draggedShape != null) {
                int cellSize = board.getWidth() / 8;
                int col = (draggedShape.getStartx() - board.getX() + cellSize / 2) / cellSize;
                int row = (draggedShape.getStarty() - board.getY() + cellSize / 2) / cellSize;

                if (canPlace(draggedShape, col, row)) {
                    Board.score += 15;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (draggedShape.template[i][j] == 1) {
                                Board.boardArr[col + i][row + j].setYes(true);
                            }
                        }
                    }
                    for (int k = 0; k < 3; k++) {
                        if (ThreeShapes[k] == draggedShape) {
                            ThreeShapes[k] = null;
                        }
                    }
                    board.ClearLines();

                    if (ThreeShapes[0] == null && ThreeShapes[1] == null && ThreeShapes[2] == null) {
                        generateNewShapes();
                    }

                    if (isGameOver()) {
                        gameOver = true;
                        Intent intent = new Intent(getContext(), GameOverActivity.class);
                        getContext().startActivity(intent);
                        if (getContext() instanceof Activity) {
                            ((Activity) getContext()).finish();
                        }
                    }
                } else {
                    draggedShape.changeScale(0.8);
                    draggedShape.updatePosition(startX, startY);
                }
                draggedShape = null;
                invalidate();
            }
        }
        return true;
    }

    private boolean canPlace(Shape s, int col, int row) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (s.template[i][j] == 1) {
                    int targetCol = col + i;
                    int targetRow = row + j;
                    if (targetCol < 0 || targetCol >= 8 || targetRow < 0 || targetRow >= 8) return false;
                    if (Board.boardArr[targetCol][targetRow].isYes()) return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        board.drawBoard(canvas);

        for (int i = 0; i < 3; i++) {
            if (ThreeShapes[i] != null) {
                int currentX = ThreeShapes[i].getStartx();
                int currentY = ThreeShapes[i].getStarty();
                ThreeShapes[i].updatePosition(currentX, currentY);

                ThreeShapes[i].drawShape(canvas);
            }
        }

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(70f);
        paint.setFakeBoldText(true);
        paint.setAntiAlias(true);

        canvas.drawText("Score: " + Board.score, 150, 130, paint);

        if (isTimerModeActive) {
            Paint timerPaint = new Paint();
            timerPaint.setColor(Color.RED);
            timerPaint.setTextSize(70f);
            timerPaint.setFakeBoldText(true);
            timerPaint.setAntiAlias(true);
            canvas.drawText("Time: " + timerSecondsLeft + "s", 600, 130, timerPaint);
        }
    }
}

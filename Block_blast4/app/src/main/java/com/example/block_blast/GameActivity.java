package com.example.block_blast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    LinearLayout boardLl;
    Board GameBoard;
    GameView gameView;
    GameTimer gameTimer;
    boolean isTimerMode = false;

    private final Handler timerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int secondsLeft = msg.arg1;

            if (gameView != null) {
                gameView.updateTimerText(secondsLeft);
            }

            if (secondsLeft == 0) {
                if (gameTimer != null) {
                    gameTimer.stopTimer();
                }
                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Board.score = 0;
        setContentView(R.layout.activity_game);

        boardLl = findViewById(R.id.bordLl);

        SharedPreferences sp = getSharedPreferences("GameSettings", MODE_PRIVATE);
        isTimerMode = sp.getBoolean("timer_mode", false);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        int boardSize = 800;
        int boardX = (screenWidth - boardSize) / 2;
        int boardY = 250;

        GameBoard = new Board(boardSize, boardSize, boardX, boardY);

        gameView = new GameView(this, GameBoard);
        gameView.setTimerModeActive(isTimerMode);

        boardLl.addView(gameView);

        if (isTimerMode) {
            gameTimer = new GameTimer(timerHandler);
            gameTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.stopTimer();
        }
    }
}

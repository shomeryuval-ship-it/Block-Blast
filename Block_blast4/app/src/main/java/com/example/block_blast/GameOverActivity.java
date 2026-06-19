package com.example.block_blast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    Button HomeBtn, ReGamebtn, RecordsBtn;
    TextView scoreTv, bestScoreTv;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setDailyAlarm();

        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HomeBtn = findViewById(R.id.HomeBtn);
        ReGamebtn = findViewById(R.id.ReGamebtn);
        RecordsBtn = findViewById(R.id.RecordsBtn);
        scoreTv = findViewById(R.id.finalScoreText);
        bestScoreTv = findViewById(R.id.bestScoreText);

        HomeBtn.setOnClickListener(this);
        ReGamebtn.setOnClickListener(this);
        RecordsBtn.setOnClickListener(this);

        int currentScore = Board.score;
        scoreTv.setText("Score: " + currentScore);

        database = FirebaseDatabase.getInstance("https://blockblastgame-12944-default-rtdb.europe-west1.firebasedatabase.app");

        saveScoreToFirebase(currentScore);

        SharedPreferences sp = getSharedPreferences("GameSettings", MODE_PRIVATE);
        int currentHighScore = sp.getInt("personal_high_score", 0);

        if (currentScore > currentHighScore) {
            currentHighScore = currentScore;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("personal_high_score", currentScore);
            editor.apply();

            scoreTv.setText("NEW RECORD! " + currentScore);
        }

        if (bestScoreTv != null) {
            bestScoreTv.setText("Best: " + currentHighScore);
        }
    }

    private void saveScoreToFirebase(int currentScore) {
        try {
            SharedPreferences gameSettings = getSharedPreferences("GameSettings", MODE_PRIVATE);
            boolean timerMode = gameSettings.getBoolean("timer_mode", false);
            String name = gameSettings.getString("player_name", "");

            String folder;
            if (timerMode) {
                folder = "timer";
            } else {
                folder = "normal";
            }

            DatabaseReference ref = database.getReference("scores").child(folder);
            String key = "game_" + System.currentTimeMillis();
            GameResult result = new GameResult(name, currentScore);
            ref.child(key).setValue(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.HomeBtn) {
            resetWholeGame();
            finish();
        }
        else if (v.getId() == R.id.ReGamebtn) {
            resetWholeGame();
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            finish();
        }
        else if (v.getId() == R.id.RecordsBtn) {
            Intent intent = new Intent(this, RecordsActivity.class);
            startActivity(intent);
        }
    }

    private void resetWholeGame() {
        Board.score = 0;
        if (Board.boardArr != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (Board.boardArr[i][j] != null) {
                        Board.boardArr[i][j].setYes(false);
                    }
                }
            }
        }
    }

    private void setDailyAlarm() {
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);

            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                flags |= PendingIntent.FLAG_IMMUTABLE;
            }

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags);

            android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(android.icu.util.Calendar.HOUR, 24);
            if (alarmManager != null) {
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }


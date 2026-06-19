package com.example.block_blast;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    TextView n1, n2, n3, n4, n5;
    TextView t1, t2, t3, t4, t5;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        n1 = findViewById(R.id.n1);
        n2 = findViewById(R.id.n2);
        n3 = findViewById(R.id.n3);
        n4 = findViewById(R.id.n4);
        n5 = findViewById(R.id.n5);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);

        database = FirebaseDatabase.getInstance("https://blockblastgame-12944-default-rtdb.europe-west1.firebasedatabase.app");

        loadScoresFromPath("scores/normal", new TextView[]{n1, n2, n3, n4, n5});
        loadScoresFromPath("scores/timer", new TextView[]{t1, t2, t3, t4, t5});
    }

    private void loadScoresFromPath(String folderPath, final TextView[] scoreViews) {
        DatabaseReference scoresRef = database.getReference(folderPath);
        scoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<GameResult> recordsList = new ArrayList<>();

                for (DataSnapshot singleGame : snapshot.getChildren()) {
                    int gameScore = 0;
                    String playerName = "אנונימי";

                    if (singleGame.hasChild("score")) {
                        Integer scoreVal = singleGame.child("score").getValue(Integer.class);
                        String nameVal = singleGame.child("name").getValue(String.class);
                        if (scoreVal != null) gameScore = scoreVal;
                        if (nameVal != null && !nameVal.isEmpty()) playerName = nameVal;
                    } else {
                        Integer scoreVal = singleGame.getValue(Integer.class);
                        if (scoreVal != null) gameScore = scoreVal;
                    }

                    recordsList.add(new GameResult(playerName, gameScore));
                }

                java.util.Collections.sort(recordsList, new java.util.Comparator<GameResult>() {
                    @Override
                    public int compare(GameResult r1, GameResult r2) {
                        if (r2.score > r1.score) return 1;
                        if (r2.score < r1.score) return -1;
                        return 0;
                    }
                });

                for (int i = 0; i < 5; i++) {
                    if (scoreViews[i] != null) {
                        if (i < recordsList.size()) {
                            GameResult currentRecord = recordsList.get(i);
                            scoreViews[i].setText((i + 1) + ". " + currentRecord.name + " — " + currentRecord.score);
                        } else {
                            scoreViews[i].setText((i + 1) + ". ---");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                for (TextView scoreView : scoreViews) {
                    if (scoreView != null) {
                        scoreView.setText("שגיאה");
                    }
                }
            }
        });
    }

}

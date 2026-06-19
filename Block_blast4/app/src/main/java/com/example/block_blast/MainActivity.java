package com.example.block_blast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button gamebtn;
    Button settbtn;
    Button recbtn;
    Button instbtn;
    TextView personalHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gamebtn = (Button) findViewById(R.id.gamebtn);
        gamebtn.setOnClickListener(this);

        settbtn = (Button) findViewById(R.id.settbtn);
        settbtn.setOnClickListener(this);

        recbtn = (Button) findViewById(R.id.recbtn);
        recbtn.setOnClickListener(this);

        instbtn = (Button) findViewById(R.id.instbtn);
        instbtn.setOnClickListener(this);

        personalHighScore = (TextView) findViewById(R.id.personalHighScore);

        SharedPreferences sp = getSharedPreferences("GameSettings", MODE_PRIVATE);
        String savedName = sp.getString("player_name", null);
        if (savedName == null) {
            showNameDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        int savedHighScore = preferences.getInt("personal_high_score", 0);
        String playerName = preferences.getString("player_name", "");

        if (personalHighScore != null) {
            personalHighScore.setText("Best Score: " + savedHighScore);
        }

        TextView welcomeTv = findViewById(R.id.welcomeTv);
        if (welcomeTv != null && !playerName.isEmpty()) {
            welcomeTv.setText("שלום, " + playerName);
        }
    }

    @Override
    public void onClick(View v) {
        if (settbtn == v) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (recbtn == v) {
            Intent intent = new Intent(this, RecordsActivity.class);
            startActivity(intent);
        }
        if (gamebtn == v) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
        if (instbtn == v) {
            Intent intent = new Intent(this, InstructionsActivity.class);
            startActivity(intent);
        }
    }

    private void showNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 40);
        layout.setBackgroundColor(Color.parseColor("#1E1E2E"));

        TextView title = new TextView(this);
        title.setText("ברוך הבא!");
        title.setTextSize(24);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView miniTitle = new TextView(this);
        miniTitle.setText("הכנס שם שחקן");
        miniTitle.setTextSize(14);
        miniTitle.setTextColor(Color.parseColor("#AAAAAA"));
        miniTitle.setGravity(Gravity.CENTER);
        miniTitle.setPadding(0, 10, 0, 30);
        layout.addView(miniTitle);

        EditText input = new EditText(this);
        input.setHint("השם שלך...");
        input.setHintTextColor(Color.parseColor("#888888"));
        input.setTextColor(Color.WHITE);
        input.setTextSize(16);
        input.setGravity(Gravity.CENTER);
        input.setBackgroundColor(Color.parseColor("#2E2E3E"));
        input.setPadding(30, 20, 30, 20);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("אישור", null);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6C63FF"));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = input.getText().toString().trim();

                if (!name.isEmpty()) {
                    getSharedPreferences("GameSettings", MODE_PRIVATE)
                            .edit()
                            .putString("player_name", name)
                            .apply();

                    TextView welcomeTv = findViewById(R.id.welcomeTv);
                    if (welcomeTv != null) {
                        welcomeTv.setText("שלום, " + name);
                    }

                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "חובה להזין שם משתמש!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

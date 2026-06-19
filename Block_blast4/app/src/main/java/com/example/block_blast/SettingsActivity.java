package com.example.block_blast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    CheckBox modeChb;
    SharedPreferences sp;
    EditText playerNameInput;
    Button saveNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("GameSettings", MODE_PRIVATE);

        modeChb = findViewById(R.id.modeChb);
        playerNameInput = findViewById(R.id.nameEt);
        saveNameButton = findViewById(R.id.saveNameBtn);

        String savedName = sp.getString("player_name", "");
        playerNameInput.setText(savedName);

        boolean isTimerOn = sp.getBoolean("timer_mode", false);
        modeChb.setChecked(isTimerOn);

        modeChb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sp.edit().putBoolean("timer_mode", isChecked).apply();
        });

        saveNameButton.setOnClickListener(v -> {
            String newName = playerNameInput.getText().toString().trim();
            if (!newName.isEmpty()) {
                sp.edit().putString("player_name", newName).apply();
                Toast.makeText(this, "השם נשמר!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "הכנס שם תקין", Toast.LENGTH_SHORT).show();
            }
        });
    }}


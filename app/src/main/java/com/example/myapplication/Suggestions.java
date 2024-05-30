package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Suggestions extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_SUGGESTION = 1;
    private Button btnAddSuggestion;
    private TextView tvSuggestionCount;
    private TextView tvSuggestions;
    private int suggestionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        btnAddSuggestion = findViewById(R.id.btnAddSuggestion);
        tvSuggestionCount = findViewById(R.id.tvSuggestionCount);
        tvSuggestions = findViewById(R.id.tvSuggestions);

        btnAddSuggestion.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddSuggestionActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_SUGGESTION);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_SUGGESTION && resultCode == RESULT_OK && data != null) {
            String suggestion = data.getStringExtra("suggestion");
            suggestionCount++;

            // 건의사항을 화면에 추가
            String currentSuggestions = tvSuggestions.getText().toString();
            String newSuggestion = suggestionCount + ". " + suggestion + "\n";
            tvSuggestions.setText(currentSuggestions + newSuggestion);

            // 건의사항 개수 업데이트
            tvSuggestionCount.setText("건의사항 개수: " + suggestionCount);
        }
    }
}
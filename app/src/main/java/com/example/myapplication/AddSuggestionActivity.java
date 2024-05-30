package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddSuggestionActivity extends AppCompatActivity {

    private EditText etSuggestion;
    private Button btnSubmitSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suggestion);

        etSuggestion = findViewById(R.id.etSuggestion);
        btnSubmitSuggestion = findViewById(R.id.btnSubmitSuggestion);

        btnSubmitSuggestion.setOnClickListener(v -> {
            String suggestion = etSuggestion.getText().toString();

            // 인텐트에 건의사항 데이터 추가
            Intent resultIntent = new Intent();
            resultIntent.putExtra("suggestion", suggestion);
            setResult(RESULT_OK, resultIntent);

            // 건의사항 제출 후 메인 액티비티로 돌아가기
            finish();
        });
    }
}
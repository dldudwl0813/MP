package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoticeActivity extends AppCompatActivity {

    private EditText etNoticeTitle;
    private EditText etNoticeContent;
    private Button btnSubmitNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        etNoticeTitle = findViewById(R.id.etNoticeTitle);
        etNoticeContent = findViewById(R.id.etNoticeContent);
        btnSubmitNotice = findViewById(R.id.btnSubmitNotice);

        btnSubmitNotice.setOnClickListener(v -> {
            String title = etNoticeTitle.getText().toString();
            String content = etNoticeContent.getText().toString();

            // 인텐트에 공지사항 데이터 추가
            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("content", content);
            setResult(RESULT_OK, resultIntent);

            // 공지사항 제출 후 메인 액티비티로 돌아가기
            finish();
        });
    }
}
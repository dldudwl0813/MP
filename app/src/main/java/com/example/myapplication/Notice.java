package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Notice extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_NOTICE = 1;
    private Button btnAddNotice;
    private TextView tvNotices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        btnAddNotice = findViewById(R.id.btnAddNotice);
        tvNotices = findViewById(R.id.tvNotices);

        btnAddNotice.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNoticeActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTICE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTICE && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");

            // 공지사항을 화면에 추가
            String currentNotices = tvNotices.getText().toString();
            String newNotice = "제목: " + title + "\n내용: " + content + "\n\n";
            tvNotices.setText(currentNotices + newNotice);
        }
    }
}
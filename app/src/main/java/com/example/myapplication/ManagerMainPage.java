/*
* LYJ
* 매니저 메인 페이지
* - 버튼이랑 intent 연결
* */

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManagerMainPage extends AppCompatActivity {

    EditText idText;
    Button btn_toStudentManagement;
    Button btn_toCheckSuggestion;
    Button btn_toNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main_page);

        // 학생 관리 페이지로 이동
        btn_toStudentManagement = findViewById(R.id.btn_toStudentManagement);
        btn_toStudentManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StudentDetail.class);
                startActivity(intent);

            }
        });

        // 건의함 확인 페이지로 이동
        btn_toCheckSuggestion = findViewById(R.id.btn_toCheckSuggestion);
        btn_toCheckSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // StudentDetail.class 로 임의로 지정했습니다
                Intent intent = new Intent(getApplicationContext(),Suggestions.class);
                startActivity(intent);
            }
        });

        // 공지사항 등록 페이지로 이동
        btn_toNotice = findViewById(R.id.btn_toNotice);
        btn_toNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // StudentDetail.class 로 임의로 지정했습니다
                Intent intent = new Intent(getApplicationContext(),AddNoticeActivity.class);
                startActivity(intent);
            }
        });
    }
}
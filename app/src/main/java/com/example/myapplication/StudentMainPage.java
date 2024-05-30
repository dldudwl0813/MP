package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StudentMainPage extends AppCompatActivity {


    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mainpage);

        Button studentInfo = (Button) findViewById(R.id.studentInfo);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            private long presstime = 0;

            @Override
            public void onClick(View view) {

                if (System.currentTimeMillis() - presstime < 2500) {  // 2.5초 이내에 다시 뒤로가기 클릭 시
                    // SharedPreferences 객체 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

                    // SharedPreferences에서 userID 값 제거하기
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("userID");
                    editor.apply();

                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);

                    return;
                }
                Toast.makeText(getApplicationContext(), "If you press the back button one more time, the app will shut down.", Toast.LENGTH_SHORT).show();
                presstime = System.currentTimeMillis();
            }
        });

        TextView logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView deleteAccount = (TextView) findViewById(R.id.deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainPage.this, StudentDeleteAccount.class);
                StudentMainPage.this.startActivity(intent);
            }
        });

        studentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainPage.this, StudentInfo.class);
                StudentMainPage.this.startActivity(intent);

            }
        }); // 각 버튼 클릭 시 이동할 클래스
        Button access = (Button) findViewById(R.id.access);
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainPage.this, StudentAccess.class);
                StudentMainPage.this.startActivity(intent);

            }

        });

        Button stayOut = findViewById(R.id.staying_out);
        stayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentStayOutOvernight.class);
                startActivity(intent);

            }
        });

    }

}

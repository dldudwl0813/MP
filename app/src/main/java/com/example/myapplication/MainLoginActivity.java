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

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            private long presstime = 0;
            @Override
            public void onClick(View view) {


                if (System.currentTimeMillis() - presstime < 2500){  // 2.5초 이내에 다시 뒤로가기 클릭 시

                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);

                    return;
                }
                Toast.makeText(getApplicationContext(),"If you press the back button one more time, the app will shut down.",Toast.LENGTH_SHORT).show();
                presstime = System.currentTimeMillis();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String originID = sharedPreferences.getString("userID", null);


        Button goStudentLogin = (Button) findViewById(R.id.goStudentLogin);
        Button goManagerLogin = (Button) findViewById(R.id.goManagerLogin);

        //Press the button to go to the student login screen
        goStudentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainLoginActivity.this, StudentLoginActivity.class);
                MainLoginActivity.this.startActivity(registerIntent);
            }
        });

        //Press the button to go to the manager login screen
        goManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainLoginActivity.this, ManagerLoginActivity.class);
                MainLoginActivity.this.startActivity(registerIntent);
            }
        });
    }
}

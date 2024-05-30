package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import android.content.Intent;
import android.content.SharedPreferences;
public class StudentLoginActivity extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Press button to go to StudentRegisterActivity
        TextView goStudentRegister = (TextView)findViewById(R.id.goStudentRegister);
        goStudentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StudentLoginActivity.this, StudentRegisterActivity.class);
                StudentLoginActivity.this.startActivity(registerIntent);
            }
        });

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID", userID);
                                editor.apply();

                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentLoginActivity.this);
                                dialog = builder.setMessage("Login successfully")
                                        .setPositiveButton("Check", null)
                                        .create();
                                dialog.show();


                                // 사감 메인 페이지로 이동해야 하지만
                                // 학생 메인 페이지만 담당했기 때문에 임시로 학생 메인 페이지로 이동하게 함
                                Intent intent = new Intent(StudentLoginActivity.this, StudentMainPage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentLoginActivity.this);
                                dialog = builder.setMessage("Check your account again")
                                        .setNegativeButton("Retry", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                StudentLoginRequest studentLoginRequest = new StudentLoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(StudentLoginActivity.this);
                queue.add(studentLoginRequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) { // 다이얼로그가 켜져있을때 함부로 종료가 되지 않게함
            dialog.dismiss();
            dialog = null;
        }
    }
}
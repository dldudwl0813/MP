package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class StudentAccess extends AppCompatActivity {


    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_access);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button enter=findViewById(R.id.enter);
        Button exit=findViewById(R.id.exit);

        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String originID = sharedPreferences.getString("userID", null);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccess(originID, "1");
                updateStayOut(originID,"0");




//                Student student = new Student();
//                if(student.getStay_out()==1)
//                {
//                    updateStayOut(originID,"0");
//                }


            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccess(originID, "0");
            }
        });
    }

    private void updateStayOut(String userID, String stay_out) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Stay out updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update stay out", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StayOutOvernightUpdateRequest stayOutOvernightUpdateRequest = new StayOutOvernightUpdateRequest(userID, stay_out, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stayOutOvernightUpdateRequest);
    }


    private void updateAccess(String userID, String access) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(StudentAccess.this, "Access updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentAccess.this, "Failed to update access", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AccessUpdateRequest accessUpdateRequest = new AccessUpdateRequest(userID, access, responseListener);
        RequestQueue queue = Volley.newRequestQueue(StudentAccess.this);
        queue.add(accessUpdateRequest);
    }
}


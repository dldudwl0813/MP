package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class StudentInfo extends AppCompatActivity {
    private ArrayAdapter adapter;
    private Spinner spinner;
    private String userID;
    private String userPassword;
    private String name;
    private String phone;
    private String studentID;
    private String userDorm;
    private String userEmail;
    private String roomNum;
    private AlertDialog dialog;
    private boolean isUserIDValidated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        spinner = findViewById(R.id.dormSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.dorm, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final EditText idText = findViewById(R.id.idText);
        final EditText passwordText = findViewById(R.id.passwordText);
        final EditText nameText = findViewById(R.id.nameText);
        final EditText emailText = findViewById(R.id.emailText);
        final EditText phoneText = findViewById(R.id.phone);
        final EditText studentIDText = findViewById(R.id.studentID);
        final EditText roomNumText = findViewById(R.id.roomNum);
        Button studentUpdateButton = findViewById(R.id.studentUpdateButton);
        Button validateButton = findViewById(R.id.validateButton);

        final Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String originID = sharedPreferences.getString("userID", null);

        fetchUserInfo(originID, idText, passwordText, nameText, emailText, phoneText, studentIDText, roomNumText);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserID = idText.getText().toString();
                if (newUserID.equals(originID)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                    dialog = builder.setMessage("The new userID is the same as the original userID.")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    isUserIDValidated = false;
                    return;
                }

                if (newUserID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                    dialog = builder.setMessage("ID is empty")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    isUserIDValidated = false;
                    return;
                }

                // Validate the new userID
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                                dialog = builder.setMessage("You can use this ID")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                isUserIDValidated = true;
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                                dialog = builder.setMessage("Already used ID")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                                isUserIDValidated = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            isUserIDValidated = false;
                        }
                    }
                };

                StudentValidateRequest validateRequest = new StudentValidateRequest(newUserID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(StudentInfo.this);
                queue.add(validateRequest);
            }
        });

        studentUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                name = nameText.getText().toString();
                userEmail = emailText.getText().toString();
                phone = phoneText.getText().toString();
                studentID = studentIDText.getText().toString();
                userDorm = spinner.getSelectedItem().toString();
                roomNum = roomNumText.getText().toString();

                if (userID.equals("") || userPassword.equals("") || name.equals("") || userEmail.equals("") || phone.equals("") || studentID.equals("") || userDorm.equals("") || roomNum.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                    dialog = builder.setMessage("Empty text exist")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if (!userID.equals(originID) && !isUserIDValidated) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                    dialog = builder.setMessage("Please validate the new userID first.")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID", userID);
                                editor.apply();

                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                                dialog = builder.setMessage("Update successful.")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();

                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                                dialog = builder.setMessage("Update failed")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                StudentUpdateRequest studentUpdateRequest = new StudentUpdateRequest(originID, userID, userPassword, name, userEmail, phone, studentID, roomNum, userDorm, responseListener);
                RequestQueue queue = Volley.newRequestQueue(StudentInfo.this);
                queue.add(studentUpdateRequest);
            }
        });
    }

    private void fetchUserInfo(String userID, final EditText idText, final EditText passwordText, final EditText nameText, final EditText emailText, final EditText phoneText, final EditText studentIDText, final EditText roomNumText) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        idText.setText(jsonResponse.getString("userID"));
                        passwordText.setText(jsonResponse.getString("userPassword"));
                        nameText.setText(jsonResponse.getString("userName"));
                        emailText.setText(jsonResponse.getString("userEmail"));
                        phoneText.setText(jsonResponse.getString("phone"));
                        studentIDText.setText(jsonResponse.getString("studentID"));
                        roomNumText.setText(jsonResponse.getString("roomNum"));
                        String userDorm = jsonResponse.getString("userDorm");

                        // Set the spinner value
                        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
                        int position = adapter.getPosition(userDorm);
                        spinner.setSelection(position);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentInfo.this);
                        dialog = builder.setMessage("Failed to fetch user info")
                                .setNegativeButton("OK", null)
                                .create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        StudentInfoRequest studentInfoRequest = new StudentInfoRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(StudentInfo.this);
        queue.add(studentInfoRequest);
    }
}


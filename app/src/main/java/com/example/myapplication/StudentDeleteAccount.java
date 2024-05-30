package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class StudentDeleteAccount extends AppCompatActivity {
    private ArrayAdapter adapter;
    private String userPassword;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_delete_account);

        final EditText passwordText = findViewById(R.id.passwordText);

        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String originID = sharedPreferences.getString("userID", null);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button passwordCheck = findViewById(R.id.passwordCheck);
        passwordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPassword = passwordText.getText().toString();
                if (validate) {
                    return; // Validation completed
                }
                // If password is not entered
                if (userPassword.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                    dialog = builder.setMessage("Please enter your password.")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                // Start validation
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { // If the password matches
                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                                dialog = builder.setMessage("Password matches.")
                                        .setPositiveButton("OK", null)
                                        .create();
                                if (!isFinishing()) {
                                    dialog.show();
                                }
                                passwordText.setEnabled(false); // Disable changing the password field
                                validate = true; // Validation completed
                            } else { // If the password does not match
                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                                dialog = builder.setMessage("Password does not match.")
                                        .setNegativeButton("OK", null)
                                        .create();
                                if (!isFinishing()) {
                                    dialog.show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                // Implement the actual server communication using the Volley library
                StudentPasswordCheck studentPasswordCheck = new StudentPasswordCheck(originID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(StudentDeleteAccount.this);
                queue.add(studentPasswordCheck);
            }
        });

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPassword = passwordText.getText().toString();

                // Check if the password validation has been completed
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                    dialog = builder.setMessage("Please check your password first.")
                            .setNegativeButton("OK", null)
                            .create();
                    if (!isFinishing()) {
                        dialog.show();
                    }
                    return;
                }

                // Create a delete confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                builder.setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog2, int id) {
                                // If the user confirms, proceed with account deletion
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) { // Account deletion successful
                                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                                                dialog = builder.setMessage("Account deletion successful.")
                                                        .setPositiveButton("OK", null)
                                                        .create();
                                                if (!isFinishing()) {
                                                    dialog.show();
                                                }

                                                // Remove userID from SharedPreferences
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.remove("userID");
                                                editor.apply();

                                                Intent intent = new Intent(StudentDeleteAccount.this, MainLoginActivity.class);
                                                StudentDeleteAccount.this.startActivity(intent);

                                                finish(); // Close the activity (registration window)
                                            } else { // Account deletion failed
                                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDeleteAccount.this);
                                                dialog = builder.setMessage("Account deletion failed.")
                                                        .setNegativeButton("OK", null)
                                                        .create();
                                                if (!isFinishing()) {
                                                    dialog.show();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                // Implement the actual server communication using the Volley library
                                StudentDeleteRequest studentDeleteRequest = new StudentDeleteRequest(originID, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(StudentDeleteAccount.this);
                                queue.add(studentDeleteRequest);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If the user cancels, do nothing
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                if (!isFinishing()) {
                    alert.show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) { // Prevent accidental exit if the dialog is open
            dialog.dismiss();
            dialog = null;
        }
    }
}


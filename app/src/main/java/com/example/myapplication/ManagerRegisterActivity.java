package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ManagerRegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private String userID;
    private String userPassword;
    private String name;
    private String phone;
    private String userEmail;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final EditText nameText = (EditText)findViewById(R.id.nameText);
        final EditText emailText = (EditText)findViewById(R.id.emailText);
        final EditText phoneText = (EditText)findViewById(R.id.phone);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //회원가입시 아이디가 사용가능한지 검증하는 부분
        final Button validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if(validate){
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                    dialog = builder.setMessage("ID is empty")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                                dialog = builder.setMessage("you can use this ID")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                validate = true;//검증완료
                            }else{//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                                dialog = builder.setMessage("alreay used ID")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                ManagerValidateRequest validateRequest = new ManagerValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ManagerRegisterActivity.this);
                queue.add(validateRequest);
            }
        });


        //회원 가입 버튼이 눌렸을때
        Button studentRegisterButton = (Button)findViewById(R.id.managerRegisterButton);
        studentRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                name = nameText.getText().toString();
                userEmail = emailText.getText().toString();
                phone = phoneText.getText().toString();


                //ID 중복체크를 했는지 확인함
                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                    dialog = builder.setMessage("First Check ID plz")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                //한칸이라도 빠트렸을 경우
                if(userID.equals("")||userPassword.equals("")||name.equals("")||
                        userEmail.equals("")||phone.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                    dialog = builder.setMessage("Empty text exist")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                //회원가입 시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                                dialog = builder.setMessage("Register Your ID")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();



                                Intent intent = new Intent(ManagerRegisterActivity.this, ManagerLoginActivity.class);
                                ManagerRegisterActivity.this.startActivity(intent);
                                finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                            }else{//회원가입 실패
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerRegisterActivity.this);
                                dialog = builder.setMessage("Register fail")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                ManagerRegisterRequest ManagerRegisterRequest = new ManagerRegisterRequest(userID, userPassword,
                        name, userEmail, phone, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ManagerRegisterActivity.this);
                queue.add(ManagerRegisterRequest);


            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}

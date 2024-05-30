package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kch on 2018. 5. 14..
 */

public class ManagerRegisterRequest extends StringRequest {

    final static private String URL = "http://adqrs70.dothome.co.kr/ManagerRegister.php";
    private Map<String, String> parameters;

    public ManagerRegisterRequest(String userID, String userPassword, String userName,
                                  String userEmail, String phone, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파라미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userEmail", userEmail);
        parameters.put("phone", phone);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}

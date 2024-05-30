package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kch on 2018. 5. 14..
 */


public class StudentUpdateRequest extends StringRequest {

    final static private String URL = "http://adqrs70.dothome.co.kr/StudentUpdate.php";

    private Map<String, String> parameters;

    public StudentUpdateRequest(String originID, String userID, String userPassword, String userName, String userEmail, String phone, String studentID, String roomNum, String userDorm, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("originID", originID);
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userEmail", userEmail);
        parameters.put("phone", phone);
        parameters.put("studentID", studentID);
        parameters.put("roomNum", roomNum);
        parameters.put("userDorm", userDorm);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}

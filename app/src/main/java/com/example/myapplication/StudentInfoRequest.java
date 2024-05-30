package com.example.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StudentInfoRequest extends StringRequest {
    private static final String URL = "http://adqrs70.dothome.co.kr/StudentInfoRequest.php";
    private Map<String, String> params;

    public StudentInfoRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
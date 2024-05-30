/*
* LYJ
* 리사이클러뷰를 활용하여 학생 관리 페이지 구현
* - 데이터 가져와서 디스플레이 (완)
* - 이름 기준 검색 기능 (완)
* - 버튼 누를 시 팝업 화면 띄우기
* - 팝업 화면에서 각 학생에 맞는 데이터 가져와서 디스플레이
* - 도미토리 기준 분리 ? (나중에 얘기 해봐야할 것 같음)
* - 가장 구현하기 쉬운 방법은 학생 관리 페이지 들어가기 전 도미토리 별로 버튼 나눠두고,
*   PHP 파일을 where dorm~~ 으로 필터링하는 방법일 듯
*/

/*
* 참고링크
* https://velog.io/@de-quei/AndroidKotlin-PHP-Mysql%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EA%B0%92%EC%9D%84-%EB%B6%88%EB%9F%AC%EC%98%A4%EA%B3%A0-TextView%EB%A1%9C-%EB%8F%99%EC%A0%81-%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0
* https://stackoverflow.com/questions/73147744/how-to-get-data-from-mysql-database-into-recycler-view
*
*  */


package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StudentDetail extends AppCompatActivity {

    EditText search;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    List<Student> studentList;
    Spinner spinner;
    Button btn_back,btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        studentList = new ArrayList<>();

        // 검색 시 같은 이름이 담길 리스트
        List<Student> search_list = new ArrayList<>();

        // 기숙사 스피너
        /*
         * 기숙사 스피너 추가하고 나서 문제는
         * 1) 스피너가 UI 상으로 보기 좋지 않다는 것 -> 옵션메뉴로 바꿔야 될 듯
         * 2) 1기숙사로 선택한 후, 2기숙사에 있는 이름을 검색하면 검색이 되면 안 되는데, 된다는 것
         * -> 수정하려면 시간 많이 들 것 같음
         * */
        List<Student> search_dorm = new ArrayList<>();

        spinner = findViewById(R.id.spinner);
        String[] items = getResources().getStringArray(R.array.dorm);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String searchDorm = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(),searchDorm,Toast.LENGTH_SHORT).show();
                search_dorm.clear();
                if(searchDorm.equals("")){
                    mainAdapter.setItems(studentList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int a = 0; a < studentList.size(); a++) {
                        if(studentList.get(a).getDorm().toString().toLowerCase().contains(searchDorm.toLowerCase()))
                        {
                            search_dorm.add(studentList.get(a));
                        }
                        mainAdapter.setItems(search_dorm);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mainAdapter = new MainAdapter(StudentDetail.this,studentList);
        recyclerView.setAdapter(mainAdapter);

        fetchDataFromServer();


        // 데이터 검색 기능
        // https://velog.io/@krrong/Android-RecyclerView-%EA%B2%80%EC%83%89-%EA%B8%B0%EB%8A%A5
        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = search.getText().toString();
                //Toast.makeText(getApplicationContext(),searchText,Toast.LENGTH_SHORT).show();
                search_list.clear();

                if(searchText.equals("")){
                    mainAdapter.setItems(studentList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int a = 0; a < studentList.size(); a++) {
                        if(studentList.get(a).getName().toString().toLowerCase().contains(searchText.toLowerCase()))
                        {
                            search_list.add(studentList.get(a));
                        }
                        mainAdapter.setItems(search_list);
                    }
                }

            }
        });

        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"데이터 업데이트",Toast.LENGTH_SHORT).show();
                fetchDataFromServer();

                //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }

    private void fetchDataFromServer() {
        // 서버에서 데이터를 가져오기 위한 Volley 요청 생성
        String serverUrl = "http://adqrs70.dothome.co.kr/Info.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // JSON 응답을 파싱하고 UI 업데이트
                        studentList.clear();
                        updateUIWithFetchedData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 오류 처리
                        Log.e("VolleyError", "Volley Error: " + error.networkResponse.statusCode + ", " + error.getMessage());
                    }
                }
        );

        // 요청을 큐에 추가
        requestQueue.add(stringRequest);
    }

    private void updateUIWithFetchedData(String response) {
        // JSON 응답을 파싱하고 UI를 업데이트
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // JSON 객체에서 데이터 추출
                // MainAdapter에 데이터 줄 요소들 세팅
                //String userID = jsonObject.getString("userID");
                String userDorm = jsonObject.getString("userDorm");
                String userName = jsonObject.getString("userName");
                String email = jsonObject.getString("userEmail");
                String id = jsonObject.getString("id");
                String roomNum = jsonObject.getString("roomNum");
                int penalty = jsonObject.getInt("penalty_points");
                int access = jsonObject.getInt("access");
                int stay_out = jsonObject.getInt("stay_out");


                Student student = new Student();
                student.setName(userName);
                student.setDorm(userDorm);
                student.setEmail(email);
                student.setId(id);
                student.setRoomNum(roomNum);
                student.setPenalty(penalty);
                student.setAccess(access);
                student.setStay_out(stay_out);

                studentList.add(student);

            }
        } catch (JSONException e) {
            Log.e("JSONError", "JSON 파싱 오류: " + response);
        }
        mainAdapter.setItems(studentList);
        recyclerView.setAdapter(mainAdapter);
    }

}
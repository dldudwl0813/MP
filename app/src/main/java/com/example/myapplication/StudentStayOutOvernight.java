package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentStayOutOvernight extends AppCompatActivity {

    private Button stayOutButton, CancleButton;
    private RecyclerView stayOutRecyclerView;
    private StayOutAdapter stayOutAdapter;
    private List<StayOutInfo> stayOutList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_stayoutovernight);

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        stayOutButton = findViewById(R.id.stayOutButton);
        CancleButton = findViewById(R.id.CancelButton);
        stayOutRecyclerView = findViewById(R.id.stayOutRecyclerView);
        stayOutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stayOutList = new ArrayList<>();
        stayOutAdapter = new StayOutAdapter(stayOutList);
        stayOutRecyclerView.setAdapter(stayOutAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String originID = sharedPreferences.getString("userID", null);


        fetchUserInfo(originID);


        stayOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StayOutInfo newStayOut = new StayOutInfo("Request information for an overnight stay");
//                stayOutList.add(newStayOut);
//                stayOutAdapter.notifyItemInserted(stayOutList.size() - 1);

                updateStayOut(originID, "1");
                showToast("Stay out requested");
                stayOutButton.setText("외박 신청됨");
            }
        });
        CancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStayOut(originID, "0");
                showToast("Stay out request cancled");
                stayOutButton.setText("외박 신청하기");

            }
        });
    }

    public void updateStayOut(String userID, String stay_out) {
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

    private void fetchUserInfo(String userID) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        //idText.setText(jsonResponse.getString("userID"));
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        int stayout = jsonResponse.getInt("stay_out");
                        if(stayout == 1)
                        {
                            stayOutButton.setText("외박 신청됨");

                        }

                        Toast.makeText(getApplicationContext(),Integer.toString(stayout),Toast.LENGTH_SHORT).show();
                        //stayOut.setText(jsonResponse.getInt("stay_out"));

                    } else {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        StudentInfoRequest studentInfoRequest = new StudentInfoRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(studentInfoRequest);
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // 외박 취소 메서드
    public void cancelStayOut(int position) {

        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String originID = sharedPreferences.getString("userID", null);

        stayOutList.remove(position);
        stayOutAdapter.notifyItemRemoved(position);
        showToast("Stay out canceled");

        updateStayOut(originID, "0");

    }

    private class StayOutAdapter extends RecyclerView.Adapter<StayOutAdapter.StayOutViewHolder> {
        private List<StayOutInfo> stayOutList;

        public StayOutAdapter(List<StayOutInfo> stayOutList) {
            this.stayOutList = stayOutList;
        }

        @Override
        public StayOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stayout_item, parent, false);
            return new StayOutViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StayOutViewHolder holder, int position) {
            StayOutInfo stayOutInfo = stayOutList.get(position);
            holder.textView.setText(stayOutInfo.getDescription());
        }

        @Override
        public int getItemCount() {
            return stayOutList.size();
        }

        public class StayOutViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            Button cancelButton;

            public StayOutViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                cancelButton = itemView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelStayOut(getAdapterPosition());
                    }
                });
            }
        }
    }

    private class StayOutInfo {
        private String description;

        public StayOutInfo(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
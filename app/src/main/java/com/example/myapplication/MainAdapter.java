/*
* LYJ
* 리사이클러뷰에 붙일 어댑터 구현
* - 각 버튼 다이얼로그 구현
*
* */

package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    List<Student> studentList;


    public MainAdapter(Context context, List<Student> studentList)
    {
        this.context = context;
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        Student student = studentList.get(position);
        holder.name.setText(student.getName());
        //holder.roomNumber.setText(Integer.toString(student.getAccess()));
        //holder.roomNumber.setText(student.getDorm());
        holder.roomNumber.setText(student.getRoomNum());

        /* 출입 확인 버튼 클릭 */
        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context.getApplicationContext(), "출입확인",Toast.LENGTH_SHORT).show();


                Student studentData = studentList.get(holder.getAdapterPosition());

                Toast.makeText(context.getApplicationContext(), Integer.toString(student.getAccess()),Toast.LENGTH_SHORT).show();


                String name = studentData.getName();
                String dorm = studentData.getDorm();
                int access = studentData.getAccess();


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_btn1);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();


                Button bt_back = dialog.findViewById(R.id.btn_back);
                TextView tv_name = dialog.findViewById(R.id.tv_name);
                TextView tv_roomNumber = dialog.findViewById(R.id.tv_roomNumber);


                tv_name.setText(name);
                tv_roomNumber.setText(dorm);
                //tv_info.setMovementMethod(new ScrollingMovementMethod()); // 스크롤바 설정


                // 뒤로가기
                bt_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                TextView entry = dialog.findViewById(R.id.entry);

                if(access == 0)
                {
                    entry.setText("외출");
                    entry.setTextColor(Color.RED);
                }
                else {
                    entry.setText("귀사");
                }


            }
        });

        /*
         * 외출 여부를 쉽게 알 수 있도록
         * 외출인 학생은 외출 버튼을
         * 빨간색으로 바꾸는 기능을 추가하였습니다.
         * */

        Student data1 = studentList.get(holder.getAdapterPosition());
        int access = data1.getAccess();
        if(access == 0)
        {
            holder.btn1.setColorFilter(Color.RED);
        }





        /* 외박 확인 버튼 클릭 */
        holder.btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context.getApplicationContext(), "외박확인",Toast.LENGTH_SHORT).show();

                Student studentData = studentList.get(holder.getAdapterPosition());

                String name = studentData.getName();
                String dorm = studentData.getDorm();


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_btn2);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();


                Button bt_back = dialog.findViewById(R.id.btn_back);
                TextView tv_name = dialog.findViewById(R.id.tv_name);
                TextView tv_roomNumber = dialog.findViewById(R.id.tv_roomNumber);


                tv_name.setText(name);
                tv_roomNumber.setText(dorm);
                //tv_info.setMovementMethod(new ScrollingMovementMethod()); // 스크롤바 설정


                // 뒤로가기
                bt_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                /*
                *
                * 위에 출입 기능과 마찬가지로 데이터가 없어서
                * 일단 이름 기준으로 바꾸도록 하였습니다.
                *
                * 다이얼로그에서 많은 데이터를 다루기에는 어려움이 있어
                * 다이얼로그에 따로 버튼을 추가하여 상세 내용을 볼 수 있는 페이지로 이동할 수 있도록 할 예정입니다.
                *
                * */


                int stay_out = studentData.getStay_out();

                TextView overnight = dialog.findViewById(R.id.overnight);

                if(stay_out == 1)
                {
                    overnight.setText("외박 신청함");
                    overnight.setTextColor(Color.BLUE);
                }
                else {
                    overnight.setText("외박 신청안함");
                }

                // 외박 상세 내역 버튼 누르면 페이지 이동하는 코드입니다
                Button btn_toOvernightDetail = dialog.findViewById(R.id.btn_to_overnight_detail);
                btn_toOvernightDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 매니저 메인 페이지로 이동하도록 짰습니다
                        Toast.makeText(context.getApplicationContext(), "외박 상세 내용",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(dialog.getContext(), ManagerMainPage.class);
                        context.startActivity(intent);
                    }
                });



            }
        });

        /*
        * 외박 여부를 쉽게 알 수 있도록
        * 외박인 학생은 외박 버튼을
        * 파란색으로 바꾸는 기능을 추가하였습니다.
        * */

        Student data2 = studentList.get(holder.getAdapterPosition());
        int stay_out = data2.getStay_out();
        if(stay_out == 1)
        {
            holder.btn2.setColorFilter(Color.BLUE);
        }




        /* 벌점 버튼 클릭 */
        holder.btn3.setOnClickListener(new View.OnClickListener()
        {
            private ArrayAdapter<String> adapter;

            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "벌점", Toast.LENGTH_SHORT).show();

                Student studentData = studentList.get(holder.getAdapterPosition());

                String name = studentData.getName();
                String dorm = studentData.getDorm();
                String id = studentData.getId();
                int penalty = studentData.getPenalty();


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_btn3);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();


                Button bt_back = dialog.findViewById(R.id.btn_back);
                TextView tv_name = dialog.findViewById(R.id.tv_name);
                TextView tv_roomNumber = dialog.findViewById(R.id.tv_roomNumber);
                TextView tv_penalty = dialog.findViewById(R.id.penalty);

                tv_name.setText(name);
                tv_roomNumber.setText(dorm);


                tv_penalty.setText(Integer.toString(penalty));


                // 벌점 상세 내역 버튼 누르면 페이지 이동하는 코드입니다
                Button btn_toPenaltyDetail = dialog.findViewById(R.id.btn_to_penalty_detail);
                btn_toPenaltyDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context.getApplicationContext(), "벌점 부과 페이지로 이동",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(dialog.getContext(), ManagerRewardPunishmentPoint.class);
                        // putExtra 사용하여 벌점 상세 페이지로 이동할 때 학생 아이디 보낼 수 있도록
                        intent.putExtra("ID",id);
                        intent.putExtra("Name",name);
                        intent.putExtra("Penalty",Integer.toString(penalty));
                        context.startActivity(intent);


                    }
                });


                // 뒤로가기
                bt_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }

        });


    }

    @Override
    public int getItemCount()
    {
        if (studentList != null) {
            Log.e("getItemCount", String.valueOf(studentList.size()));

            return studentList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, roomNumber;
        ImageView btn1, btn2, btn3;


        public ViewHolder(@NonNull View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            roomNumber = (TextView) view.findViewById(R.id.tv_roomNumber);

            btn1 = view.findViewById(R.id.btn1);
            btn2 = view.findViewById(R.id.btn2);
            btn3 = view.findViewById(R.id.btn3);
        }
    }

    public void setItems(List<Student> list)
    {
        studentList = list;
        notifyDataSetChanged();

    }
}

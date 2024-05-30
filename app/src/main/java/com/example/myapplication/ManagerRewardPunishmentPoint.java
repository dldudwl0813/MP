package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ManagerRewardPunishmentPoint extends AppCompatActivity {


    private TextView penaltyTextView;
    private Button plusButton;
    private Button minusButton;
    private Button assignPenaltyButton;

    TextView tv_id,tv_name;


    private int penaltyCount = 0;

    private static final int REQUEST_CODE = 777; //상수값 선언


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_rewardpunishment_point);

        penaltyTextView = findViewById(R.id.penaltyTextView);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);
        assignPenaltyButton = findViewById(R.id.assignPenaltyButton);

        tv_id = findViewById(R.id.tv_id);
        tv_name = findViewById(R.id.tv_name);


        // Intent로 학생 ID 값 받아오기
        // 이 ID로 학생에게 벌점 부과할 수 있도록 코드 추가
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("Name");
        //int penalty = Integer.parseInt(intent.getStringExtra("Penalty"));
        String penalty =intent.getStringExtra("Penalty");
        // 우선 패널티 보여주는 곳에 ID display 할 수 있도록 코드 추가함
        tv_id.setText(id);
        tv_name.setText(name);
        penaltyTextView.setText(penalty);


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penaltyCount < 10) {
                    penaltyCount++;
                    updatePenaltyTextView();
                } else {
                    Toast.makeText(ManagerRewardPunishmentPoint.this, "Penalty points can only be registered up to 10 points.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penaltyCount > 0) {
                    penaltyCount--;
                    updatePenaltyTextView();
                } else {
                    Toast.makeText(ManagerRewardPunishmentPoint.this, "Penalty points cannot be set to less than zero points.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        assignPenaltyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penaltyCount > 0) {
                    // TODO: 관생에게 벌점을 부여하는 기능 구현
                    Toast.makeText(ManagerRewardPunishmentPoint.this, penaltyCount + "penalty points have been awarded.", Toast.LENGTH_SHORT).show();
                    penaltyCount = 0; // 부여 후 벌점 카운트 초기화
                    updatePenaltyTextView();
                } else {
                    Toast.makeText(ManagerRewardPunishmentPoint.this, "No penalty points to grant.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updatePenaltyTextView() {
        penaltyTextView.setText(String.valueOf(penaltyCount));
    }
}

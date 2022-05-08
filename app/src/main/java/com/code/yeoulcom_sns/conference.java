package com.code.yeoulcom_sns;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yeoulcom_sns.R;

public class conference extends AppCompatActivity {

    // + 버튼 누르면 버튼 생성
    Button addBtn, addBtn2, addBtn3;

    // 새로고침
    private SwipeRefreshLayout swipeRefreshLayout;

    // 달력
    CalendarView calendar;

    String name, generation, key, Time;
    boolean adminCheck;

    //기수,이름 핸드폰에 저장 SharedPreferences
    SharedPreferences SP;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference);

        Button postBtn = (Button) findViewById(R.id.postBtn);
        Button conferenceBtn = (Button) findViewById(R.id.conferenceBtn);
        Button voteBtn = (Button) findViewById(R.id.voteBtn);
        Button Chairman_Btn = (Button) findViewById(R.id.Chairman_Btn);

        // + 버튼 누르면 버튼 생성
        Button addBtn = (Button) findViewById(R.id.about_btn);
        Button addBtn2 = (Button) findViewById(R.id.about_btn2);
        Button addBtn3 = (Button) findViewById(R.id.about_btn3);

        //새로고침
        swipeRefreshLayout = findViewById(R.id.conference);

        // 달력
        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

        init();

        // 게시물 클릭 시 이동
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // 투표 클릭 시 이동
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), vote.class);
//                화면 전환, 기수,이름,권한 정보 다음 엑티비티로 넘기기
                editor.putString("name", name);
                editor.putString("generation", generation);
                editor.putBoolean("admin", adminCheck);
                editor.apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // 내 정보 클릭 시 이동
        addBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), aboutMe.class);
                startActivity(intent);
            }
        });


        // 회장페이지 버튼 클릭 시 이동 (오직 임원에게만 Active)
        Chairman_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminCheck == true){
                    Intent intent = new Intent(getApplicationContext(), LeadersActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }else {
                    Toast.makeText(getApplicationContext(),"권한이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // + 버튼 누르면 속성 버튼 생성
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBtn2.getVisibility() == View.GONE && addBtn3.getVisibility() == View.GONE) {
                    addBtn2.setVisibility(View.VISIBLE); // or GONE
                    addBtn3.setVisibility(View.VISIBLE); // or GONE
                } else {
                    addBtn2.setVisibility(View.GONE);
                    addBtn3.setVisibility(View.GONE);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateLayoutView();

                swipeRefreshLayout.setRefreshing(false);
            }

            private void updateLayoutView() {
                if (addBtn2.getVisibility() + addBtn3.getVisibility() == View.VISIBLE) {
                    addBtn2.setVisibility(View.GONE);
                    addBtn3.setVisibility(View.GONE);
                }

            }
        });
    }
    public void init(){
        //SharedPreferences
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        editor = SP.edit();

        //이전 엑티비티에서 넘어온 기수,이름 받기
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        name = SP.getString("name","");
        generation = SP.getString("generation","");
        adminCheck = SP.getBoolean("admin",false);
    }

}
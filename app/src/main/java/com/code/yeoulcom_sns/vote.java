package com.code.yeoulcom_sns;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;

public class vote extends AppCompatActivity {


    // + 버튼 누르면 버튼 생성
    Button addBtn, addBtn2, addBtn3;

    // 변수
    int count1;
    int count2;

    // 투표창 --> 득표 셀때 사용하는 기능
    private int yes_count = 0;
    private int no_count = 0;

    SharedPreferences pref;          // 프리퍼런스
    SharedPreferences.Editor editor; // 에디터


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);

        Button postBtn = (Button) findViewById(R.id.postBtn);
        Button conferenceBtn = (Button) findViewById(R.id.conferenceBtn);
        Button Chairman_Btn = (Button) findViewById(R.id.Chairman_Btn);

        // 투표 버튼
        Button yesBtn = (Button) findViewById(R.id.yes_btn);
        Button noBtn = (Button) findViewById(R.id.no_btn);

        // 득표 현황
        TextView get_vote_no = (TextView) findViewById(R.id.get_vote_no);
        TextView get_vote_yes = (TextView) findViewById(R.id.get_vote_yes);

        // + 버튼 누르면 버튼 생성
        Button addBtn = (Button) findViewById(R.id.about_btn);
        Button addBtn2 = (Button) findViewById(R.id.about_btn2);
        Button addBtn3 = (Button) findViewById(R.id.about_btn3);
        Button bt_write_vote = (Button) findViewById(R.id.bt_write_vote);

//        Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_month);
//        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
//                R.array.date_month, android.R.layout.simple_spinner_item);
//        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        monthSpinner.setAdapter(monthAdapter);


        // 게시물 클릭 시 이동
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // 컨퍼런스 클릭 시 이동
        conferenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), conference.class);
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
                Intent intent = new Intent(getApplicationContext(), LeadersActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBtn2.getVisibility() == View.GONE && addBtn3.getVisibility() == View.GONE) {
                    addBtn2.setVisibility(View.VISIBLE); // or GONE
                    addBtn3.setVisibility(View.VISIBLE); // or GONE
                    bt_write_vote.setVisibility(View.VISIBLE); // or GONE
                } else {
                    addBtn2.setVisibility(View.GONE);
                    addBtn3.setVisibility(View.GONE);
                    bt_write_vote.setVisibility(View.GONE);
                }
            }
        });

        // 1. Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        // 2. 저장해둔 값 불러오기 ("식별값", 초기값) -> 식별값과 초기값은 직접 원하는 이름과 값으로 작성.
        yes_count = pref.getInt("Yes", 0);        // int 불러오기 (저장해둔 값 없으면 초기값인 0으로 불러옴)
        no_count = pref.getInt("No", 0);   // int 불러오기 (저장해둔 값 없으면 초기값인 0으로 불러옴)

        // 각 상황에 맞는 버튼을 클릭하면 득표수 증가
        get_vote_yes.setText(yes_count+"");
        get_vote_no.setText(no_count+"");

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes_count ++;
                get_vote_yes.setText(yes_count+"");
                yesBtn.setEnabled(false);
                // 클릭 횟수 저장
                editor.putInt("Yes", yes_count);
                editor.apply(); // 저장
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_count ++;
                get_vote_no.setText(no_count+"");
                noBtn.setEnabled(false);
                // 클릭 횟수 저장
                editor.putInt("No", no_count);
                editor.apply(); // 저장
            }
        });

    }
}

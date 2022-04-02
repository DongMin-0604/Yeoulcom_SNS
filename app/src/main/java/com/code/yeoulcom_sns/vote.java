package com.code.yeoulcom_sns;

import android.content.Intent;
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
    
    // 투표창 --> 득표 셀때 사용하는 기능
    private int yes_count = 0;
    private int no_count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);

        Button postBtn = (Button) findViewById(R.id.postBtn);
        Button conferenceBtn = (Button) findViewById(R.id.conferenceBtn);
        Button voteBtn = (Button) findViewById(R.id.voteBtn);

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

        
        // 각 상황에 맞는 버튼을 클릭하면 득표수 증가
        get_vote_yes.setText(yes_count+"");
        get_vote_no.setText(no_count+"");

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes_count ++;
                get_vote_yes.setText(yes_count+"");
                yesBtn.setEnabled(false);
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_count ++;
                get_vote_no.setText(no_count+"");
                noBtn.setEnabled(false);
            }
        });
    }
}

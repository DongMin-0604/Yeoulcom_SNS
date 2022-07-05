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

    //뒤로가기 두번 입력 체크용
    private long backKeyPressedTime;
    Toast toast;

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
        Button addBtn1 = (Button) findViewById(R.id.about_btn1);
        Button addBtn2 = (Button) findViewById(R.id.about_btn2);

        //새로고침
        swipeRefreshLayout = findViewById(R.id.conference);

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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // 내 정보 클릭 시 이동
        addBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
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

        // 개발자 이메일 클릭하면 이메일 띄우기
        addBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBtn2 = new Intent(Intent.ACTION_SEND);
                addBtn2.setType("Plain/text");
                String[] address = {"dongmin3147@gmail.com"};
                addBtn2.putExtra(Intent.EXTRA_EMAIL, address);
                addBtn2.putExtra(Intent.EXTRA_SUBJECT, "오류 피드백입니다.");
                addBtn2.putExtra(Intent.EXTRA_TEXT, "내용 미리보기 (미리 적을 수 있음");
                startActivity(addBtn2);
            }
        });

        // + 버튼 누르면 속성 버튼 생성
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBtn1.getVisibility() == View.GONE && addBtn2.getVisibility() == View.GONE) {
                    addBtn1.setVisibility(View.VISIBLE); // or GONE
                    addBtn2.setVisibility(View.VISIBLE); // or GONE
                } else {
                    addBtn1.setVisibility(View.GONE);
                    addBtn2.setVisibility(View.GONE);
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
                if (addBtn1.getVisibility() + addBtn2.getVisibility() == View.VISIBLE) {
                    addBtn1.setVisibility(View.GONE);
                    addBtn2.setVisibility(View.GONE);
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            toast.cancel();
        }
    }
    public void init(){
        //SharedPreferences

        //SharedPreferences 기수,이름 받기
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        name = SP.getString("name","");
        generation = SP.getString("generation","");
        adminCheck = SP.getBoolean("admin",false);
    }

}
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;

public class vote extends AppCompatActivity {


    // + 버튼 누르면 버튼 생성
    Button addBtn, addBtn2, addBtn3;

    // 변수
    int count1;
    int count2;

    int no_count1;
    int yes_count1;

    // 투표창 --> 득표 셀때 사용하는 기능
    private int yes_count = 0;
    private int no_count = 0;

    SharedPreferences pref;          // 프리퍼런스
    SharedPreferences.Editor editor; // 에디터

    String name, generation, key, Time;
    boolean adminCheck;

    //기수,이름 핸드폰에 저장 SharedPreferences
    SharedPreferences SP;
    Toast toast;
    //뒤로가기 두번 입력 체크용
    private long backKeyPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);

        Button postBtn = (Button) findViewById(R.id.postBtn);
        Button conferenceBtn = (Button) findViewById(R.id.conferenceBtn);
        Button Chairman_Btn = (Button) findViewById(R.id.Chairman_Btn);

        // 투표 버튼
//        Button yesBtn = (Button) findViewById(R.id.yes_btn);
//        Button noBtn = (Button) findViewById(R.id.no_btn);

//        // 득표 현황
//        TextView get_vote_no = (TextView) findViewById(R.id.get_vote_no);
//        TextView get_vote_yes = (TextView) findViewById(R.id.get_vote_yes);

        // + 버튼 누르면 버튼 생성
        Button addBtn = (Button) findViewById(R.id.about_btn);
        Button addBtn1 = (Button) findViewById(R.id.about_btn1);
        Button addBtn2 = (Button) findViewById(R.id.about_btn2);
        Button bt_write_vote = (Button) findViewById(R.id.bt_write_vote);

//        Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_month);
//        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
//                R.array.date_month, android.R.layout.simple_spinner_item);
//        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        monthSpinner.setAdapter(monthAdapter);

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

        // 컨퍼런스 클릭 시 이동
        conferenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), conference.class);
                //화면 전환, 기수,이름,권한 정보 다음 엑티비티로 넘기기
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
                //관리자 권한 체크 후 관리페이지 활성화
                if (adminCheck == true){
                    Intent intent = new Intent(getApplicationContext(), LeadersActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }else {
                    Toast.makeText(getApplicationContext(),"권한이 없습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBtn1.getVisibility() == View.GONE && addBtn2.getVisibility() == View.GONE) {
                    addBtn1.setVisibility(View.VISIBLE); // or GONE
                    addBtn2.setVisibility(View.VISIBLE); // or GONE
//                    bt_write_vote.setVisibility(View.VISIBLE); // or GONE
                } else {
                    addBtn1.setVisibility(View.GONE);
                    addBtn2.setVisibility(View.GONE);
//                    bt_write_vote.setVisibility(View.GONE);
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

        // 1. Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        // 2. 저장해둔 값 불러오기 ("식별값", 초기값) -> 식별값과 초기값은 직접 원하는 이름과 값으로 작성.
        yes_count = pref.getInt("Yes", 0);        // int 불러오기 (저장해둔 값 없으면 초기값인 0으로 불러옴)
        no_count = pref.getInt("No", 0);   // int 불러오기 (저장해둔 값 없으면 초기값인 0으로 불러옴)

        // 각 상황에 맞는 버튼을 클릭하면 득표수 증가
//        get_vote_yes.setText(yes_count+"");
//        get_vote_no.setText(no_count+"");

//        yesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                yes_count ++;
//                get_vote_yes.setText(yes_count+"");
//                yesBtn.setEnabled(false);
//                // 클릭 횟수 저장
//                editor.putInt("Yes", yes_count);
//                editor.apply(); // 저장
//            }
//        });
//
//        noBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                no_count ++;
//                get_vote_no.setText(no_count+"");
//                noBtn.setEnabled(false);
//                // 클릭 횟수 저장
//                editor.putInt("No", no_count);
//                editor.apply(); // 저장
//            }
//        });

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
        //SharedPreferences에 저장된 값 받기
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        name = SP.getString("name","");
        generation = SP.getString("generation","");
        adminCheck = SP.getBoolean("admin",false);

    }
}
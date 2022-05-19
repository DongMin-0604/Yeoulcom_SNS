package com.code.yeoulcom_sns;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;

public class WaitingOkayActivity extends AppCompatActivity {

    //기수,이름 핸드폰에 저장 SharedPreferences
    SharedPreferences SP;
    SharedPreferences.Editor editor;

    String name = "", generation = "";
    boolean adminCheck,OkayCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //이름, 기수 없으면 첫 화면으로
        init();
        if (name == "" || generation == "") {
            Toast.makeText(getApplicationContext(), "승인되지 않은 사용자입니다.", Toast.LENGTH_SHORT).show();
            Intent intent_view_change = new Intent(getApplicationContext(), InputInformationActivity.class);
            startActivity(intent_view_change);
        }
        setContentView(R.layout.activity_waiting_okay);

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
        OkayCheck = SP.getBoolean("Okay", false);
    }
}

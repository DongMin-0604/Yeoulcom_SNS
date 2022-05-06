package com.code.yeoulcom_sns;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;

public class WaitingOkayActivity extends AppCompatActivity {

    //기수,이름 핸드폰에 저장 SharedPreferences
    SharedPreferences SP;
    SharedPreferences.Editor editor;

    String name = "", generation = "", key, Time;
    boolean adminCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_okay);

        init();
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

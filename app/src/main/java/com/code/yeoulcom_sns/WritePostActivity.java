package com.code.yeoulcom_sns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritePostActivity extends AppCompatActivity {
    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    EditText et_title, et_main_text;
    Button bt_write;
    String name;
    String generation;
    //오늘 날짜 가져오기 위한 코드
    long mNow;
    Date mDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

//    게시글 작성 화면 자바
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        init();
        onClick();
    }
    public void init(){
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        generation = intent.getStringExtra("generation");

        et_main_text = findViewById(R.id.et_main_text);
        et_title = findViewById(R.id.et_title);
        bt_write = findViewById(R.id.bt_write);

    }
//    private String getTime(){
//        //현재 날짜 받아오기
//        mNow = System.currentTimeMillis();
//        mDate = new Date(mNow);
//
//        return format.format(mDate);
//    }
    public void onClick(){
        //onClick 모아놓는 메소드
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_post(name,generation,et_title.getText().toString(),et_main_text.getText().toString());
                Intent intent_view_change = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent_view_change);
            }
        });
    }
    public void write_post(String name, String generation, String title, String text){
        //파이어베이스에 저장하기
        addPostSave addPostSave = new addPostSave(name,generation,title,text);
        databaseReference.child("post_save").push().setValue(addPostSave);
    }
}

package com.code.yeoulcom_sns;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WritePostActivity extends AppCompatActivity {
    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    EditText et_title, et_main_text;
    Button bt_write;


//    게시글 작성 화면 자바
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        init();
        onClick();
    }
    public void init(){

        et_main_text = findViewById(R.id.et_main_text);
        et_title = findViewById(R.id.et_title);
        bt_write = findViewById(R.id.bt_write);

    }
    public void onClick(){
        //onClick 모아놓는 메소드
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

package com.code.yeoulcom_sns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class InputInformationActivity extends AppCompatActivity {

    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //기수 선택 스피너
    Spinner sp_generation;


    Button bt_Apply;
    EditText et_name;
    String st_generation, st_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_information);
        init();
        onClick();

    }public void init(){
        //기본 세팅 메소드

        sp_generation = findViewById(R.id.sp_generation);

        //Spinner 관련 세팅
        List<String> array_generation = Arrays.asList(getResources().getStringArray(R.array.generation));
        ArrayAdapter<String> adapter_generation = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner, array_generation);
        adapter_generation.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_generation.setAdapter(adapter_generation);

        bt_Apply = findViewById(R.id.bt_Apply);
        et_name = findViewById(R.id.et_name);
    }
    public void onClick(){
        //onClick 모아놓는 메소드

        bt_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser(st_name = et_name.getText().toString(), st_generation = sp_generation.getSelectedItem().toString());
                //화면 전환
                Intent intent_view_change = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent_view_change);
                
            }
        });
    }
    public void addUser(String name, String generation) {
        //파이어베이스에 업로드
        databaseReference.child("user").child(st_generation).push().setValue(st_name);
    }
}

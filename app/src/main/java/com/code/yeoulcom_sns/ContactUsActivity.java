package com.code.yeoulcom_sns;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity {

    Spinner SP_category;
    String[] items = {"버그신고", "문의", "기능추가제안", "개발자 신청" ,"기타"};
    ImageButton back_btn;
    TextView TV_category_info;
    Button BT_send;

    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    String category,generation,name,text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        init();
        onClick();
    }

    public void init(){
        SP_category = findViewById(R.id.sp_category);
        back_btn = (ImageButton) findViewById(R.id.IV_onBack);
        TV_category_info = findViewById(R.id.TV_category_info);
        BT_send = findViewById(R.id.BT_send);

        //문의하기 Spinner 설정
        ArrayAdapter<String> adapter_generation = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner, items);
        adapter_generation.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        SP_category.setAdapter(adapter_generation);

    }
    void onClick(){
        SP_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (items[position]){
                    case "버그신고":
                        TV_category_info.setText("버그가 일어났던 상황,발생조건을 상세히 기재해주세요.");
                        category = items[position];
                        break;
                    case "문의":
                        TV_category_info.setText("개발자에게 문의할 내용을 입력해주세요.");
                        category = items[position];
                        break;
                    case "기능추가제안":
                        TV_category_info.setText("새로운 기능이나 수정할 기능을 말해주세요.");
                        category = items[position];
                        break;
                    case "개발자 신청":
                        TV_category_info.setText("이 프로젝트를 함께 완수할 새로운 개발자를 모집합니다.\n포부와 하고싶은 말을 기재해주세요.");
                        category = items[position];
                        break;
                    case "기타":
                        TV_category_info.setText("기타사항을 기재해주세요.");
                        category = items[position];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //뒤로가기 버튼
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        BT_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = TV_category_info.getText().toString();

            }
        });
    }
    public void write_post_img(String name, String generation, String category, String text){
        //이미지 포함 파이어베이스에 올리는 메소드

        databaseReference.child("문의사항").child(category).push().setValue(addPostSaveImg);
    }
    }

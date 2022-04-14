package com.code.yeoulcom_sns;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.yeoulcom_sns.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class InputInformationActivity extends AppCompatActivity {

    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //SharedPreferences
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    //기수 선택 스피너
    Spinner sp_generation;


    Button bt_Apply,cancel_BT,request_BT;
    TextView name_generation_check_TV;
    LinearLayout info_check_Layout;
    EditText et_name;
    String st_generation, st_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_information);
        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
        onClick();

    }

    public void init() {
        //기본 세팅 메소드

        sp_generation = findViewById(R.id.sp_generation);
        //Spinner 관련 세팅
        List<String> array_generation = Arrays.asList(getResources().getStringArray(R.array.generation));
        ArrayAdapter<String> adapter_generation = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner, array_generation);
        adapter_generation.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_generation.setAdapter(adapter_generation);

        //xml 요소 연결
        bt_Apply = findViewById(R.id.bt_Apply);
        et_name = findViewById(R.id.et_name);
        request_BT = findViewById(R.id.request_BT);
        cancel_BT = findViewById(R.id.cancel_BT);
        name_generation_check_TV = findViewById(R.id.name_generation_check_TV);
        info_check_Layout = findViewById(R.id.info_check_Layout);

        //글자 길이와 한글만 입력되게 하는 필터
        InputFilter lengthFilter = new InputFilter.LengthFilter(4);
        InputFilter[] filters = new InputFilter[]{filterKor,lengthFilter};
        et_name.setFilters(filters);

        //SharedPreferences
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        editor = SP.edit();
    }

    public void onClick() {
        //onClick 모아놓는 메소드
        bt_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else {
                    info_check_Layout.setVisibility(View.VISIBLE);
                    name_generation_check_TV.setText(sp_generation.getSelectedItem().toString() +" "+et_name.getText().toString() );
                    bt_Apply.setEnabled(false);
                }
            }
        });
        cancel_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_check_Layout.setVisibility(View.GONE);
                bt_Apply.setEnabled(true);
            }
        });
        request_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_check_Layout.setVisibility(View.GONE);
                bt_Apply.setEnabled(true);
                //이름,기수 변수에 값 넣어주기
                st_name = et_name.getText().toString();
                st_generation = sp_generation.getSelectedItem().toString();
                //화면 전환, 기수,이름 다음 엑티비티로 넘기기
                editor.putString("name",st_name);
                editor.putString("generation",st_generation);
                editor.apply();
                Intent intent_view_change = new Intent(getApplicationContext(), MainActivity.class);
                intent_view_change.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // 게시물 테스트를 위한 잠깐 막아놓기,파이어베이스 업로드
                addUser(st_name,st_generation);
                startActivity(intent_view_change);
            }
        });
    }

    public void addUser(String name, String generation) {
        //파이어베이스에 업로드
        databaseReference.child("user").child(st_generation).push().setValue(st_name);
    }

    public InputFilter filterKor = new InputFilter() {
        //한글만 입력받는 필터 설정
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅣ가-힣]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
}

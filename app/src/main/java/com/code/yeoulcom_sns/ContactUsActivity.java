package com.code.yeoulcom_sns;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;

import java.util.Arrays;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity {

    Spinner SP_category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        init();
    }

    void init(){
        SP_category = findViewById(R.id.sp_category);

        //Spinner 관련 세팅
        List<String> array_category = Arrays.asList(getResources().getStringArray(R.array.category));
        ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner, array_category);
        adapter_category.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Log.d("S ","Spinner:" + adapter_category);
        SP_category.setAdapter(adapter_category);
    }
}

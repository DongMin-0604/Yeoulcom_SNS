package com.code.yeoulcom_sns;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yeoulcom_sns.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

    ImageView IV_image_select,img_test;
    ImageView IV_onBack;

    int a = 1;

    //오늘 날짜 가져오기 위한 코드
    long mNow;
    Date mDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    //갤러리 접근 권한
    private final int GALLERY_CODE = 10;

    //스토리지 접근 권한
    private FirebaseStorage storage;

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

        IV_image_select = findViewById(R.id.IV_image_select);
        img_test = findViewById(R.id.img_test);
        IV_onBack = findViewById(R.id.IV_onBack);

        //파이어베이스 스토리지
        storage = FirebaseStorage.getInstance();

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
        IV_image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadGallery();
            }
        });

        IV_onBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void LoadGallery(){
        Intent intent_load = new Intent(Intent.ACTION_PICK);
        intent_load.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent_load, GALLERY_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE){
            Uri file = data.getData();
            StorageReference storageRef = storage.getReference();
            StorageReference reversRef = storageRef.child("img/"+"img"+ a +".png");
            UploadTask uploadTask = reversRef.putFile(file);

            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                img_test.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "사진이 정상적으로 업로드 되지 않았습니다.",Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void write_post(String name, String generation, String title, String text){
        //파이어베이스에 저장하기
        addPostSave addPostSave = new addPostSave(name,generation,title,text);
        databaseReference.child("post_save").push().setValue(addPostSave);
    }
}

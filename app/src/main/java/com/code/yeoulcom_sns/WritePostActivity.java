package com.code.yeoulcom_sns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.yeoulcom_sns.R;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WritePostActivity extends AppCompatActivity {
    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //스토리지 접근 권한
    private FirebaseStorage storage;

    Uri file;


    EditText et_title, et_main_text;
    Button bt_write;
    String name;
    String generation;

    ImageView IV_image_select,img_test;
    ImageView IV_onBack;

    boolean uploadCheck = false;

    int a = 1;

    //오늘 날짜 가져오기 위한 코드
    long mNow;
    Date mDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    //갤러리 접근 권한
    private final int GALLERY_CODE = 10;
    //테스트
    Bitmap img;

    //로딩 알려주는 위젯
    ProgressDialog dialog;


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
                if (img_test.getDrawable() == null){
                    //디폴트 하얀 화면 파이어베이스 storage 주소
                    String WhiteImage ="content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F33/ORIGINAL/NONE/image%2Fjpeg/998580531";
                    write_post_img(name,generation,et_title.getText().toString(),et_main_text.getText().toString(),WhiteImage);
                    Log.d("1","디폴트"+WhiteImage);
//                    write_post(name,generation,et_title.getText().toString(),et_main_text.getText().toString());
                }else{
                        write_post_img(name,generation,et_title.getText().toString(),et_main_text.getText().toString(),file.toString());
                        Log.d("1",file.toString());
                }
                dialog = new ProgressDialog(WritePostActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("게시물 작성중. . .");

                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent_view_change = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent_view_change);
                        dialog.dismiss();
                    }
                },600);
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
        //갤러리로 이동
        Intent intent_load = new Intent(Intent.ACTION_PICK);
        intent_load.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent_load, GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //갤러리에서 선택한 사진 읽어오는 메소드
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE){
            if (data != null){
            file = data.getData();
            Log.d("file",file.toString());
            StorageReference storageRef = storage.getReference();
            StorageReference reversRef = storageRef.child("img/"+file);
            UploadTask uploadTask = reversRef.putFile(file);
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(in);
                in.close();
                img_test.setImageBitmap(img);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"이미지 선택 안함",Toast.LENGTH_SHORT);
                e.printStackTrace();
            }

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "사진이 정상적으로 업로드 되지 않았습니다.",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getApplicationContext(), "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            }
        }
    }
    public void write_post_img(String name, String generation, String title, String text, String file){
        //이미지 포함 파이어베이스에 올리는 메소드
        addPostSaveImg addPostSaveImg = new addPostSaveImg(name,generation,title,text,file);
        databaseReference.child("post_save").push().setValue(addPostSaveImg);
        uploadCheck = true;
    }
}

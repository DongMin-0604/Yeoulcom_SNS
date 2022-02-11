package com.code.yeoulcom_sns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.yeoulcom_sns.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    Button postBtn,conferenceBtn,voteBtn,bt_write_post;
    String name,generation,key;

    TextView Post1_title,Post1_main_text;
    // 게시물 받아오는 클래스 참조
    getPost getPost;

    //오늘 날짜 가져오기 위한 코드
    long mNow;
    Date mDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        onclick();
        getPost();
    }

    public void init(){
        //기본 세팅 함수
        postBtn = (Button) findViewById(R.id.postBtn);
        conferenceBtn= (Button) findViewById(R.id.conferenceBtn);
        voteBtn = (Button) findViewById(R.id.voteBtn);
        bt_write_post = (Button) findViewById(R.id.bt_write_post);
        Post1_title = (TextView) findViewById(R.id.Post1_title);
        Post1_main_text = (TextView) findViewById(R.id.post1_main_text);


        //이전 엑티비티에서 넘어온 기수,이름 받기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        generation = intent.getStringExtra("generation");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void onclick() {
        //onclick 모아 놓는 함수
        // 컨퍼런스 클릭 시 이동
        conferenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), conference.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // 투표 클릭 시 이동
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), vote.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        //게시물 작성 클릭 시 이동
        bt_write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_view_change = new Intent(getApplicationContext(),WritePostActivity.class);
                intent_view_change.putExtra("generation",generation);
                intent_view_change.putExtra("name", name);
                startActivity(intent_view_change);
            }
        });

        // 새로고침 기능
    }
    private String getTime(){
        //현재 날짜 받아오기
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return format.format(mDate);
    }
    public void getPost(){
        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot: snapshot.getChildren()) {
                    key = Snapshot.getKey();
                    getPost = Snapshot.getValue(getPost.class);
                }
                Post1_title.setText(getPost.getTitle());
                Post1_main_text.setText(getPost.getMain_text());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child("post_save").child(getTime()).addValueEventListener(mValueEventListener);
    }
}
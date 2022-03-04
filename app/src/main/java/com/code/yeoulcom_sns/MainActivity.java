package com.code.yeoulcom_sns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.yeoulcom_sns.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {
    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    // 새로고침
    private SwipeRefreshLayout swipeRefreshLayout;

    // + 버튼 누르면 버튼 생성
    Button addBtn, addBtn2, addBtn3;

    Button postBtn, conferenceBtn, voteBtn, bt_write_post;
    String name, generation, key, Time;

    //큰 게시물 TextView
    TextView post_title, post_main_text, post_name_generation, post_time;
    String post_name, post_generation;
    // 게시물 받아오는 클래스 참조
    getPost getPost;

    //레이아웃 터치시 큰 화면으로 전환을 위한 레이아웃 정의
    LinearLayout post_short, post_long;
    LinearLayout main_layout;

    //오늘 날짜 가져오기 위한 코드
    long mNow;
    Date mDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    int a = 0;

    //recyclerView 영역
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    TextView Post1_title_1, Post1_main_text_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //기수 이름이 없을 시 첫 화면으로
//        if (name == "" || generation == ""){
//            Toast.makeText(getApplicationContext(),"승인되지 않은 사용자입니다.",Toast.LENGTH_SHORT);
//            Intent intent_view_change = new Intent(getApplicationContext(),InputInformationActivity.class);
//            startActivity(intent_view_change);
//        }
        getPost();
        onclick();

    }

    public void init() {
        //기본 세팅 함수
        postBtn = (Button) findViewById(R.id.postBtn);
        conferenceBtn = (Button) findViewById(R.id.conferenceBtn);
        voteBtn = (Button) findViewById(R.id.voteBtn);
        bt_write_post = (Button) findViewById(R.id.bt_write_post);

        post_long = (LinearLayout) findViewById(R.id.post_long);
        post_main_text = (TextView) findViewById(R.id.post_main_text);
        post_name_generation = (TextView) findViewById(R.id.post_name_generation);
        post_title = (TextView) findViewById(R.id.post_title);
        post_time = (TextView) findViewById(R.id.post_time);

        main_layout = (LinearLayout)findViewById(R.id.main_layout);

        //이전 엑티비티에서 넘어온 기수,이름 받기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        generation = intent.getStringExtra("generation");

        // + 버튼 누르면 버튼 생성
        addBtn = (Button) findViewById(R.id.about_btn);
        addBtn2 = (Button) findViewById(R.id.about_btn2);
        addBtn3 = (Button) findViewById(R.id.about_btn3);

        //새로고침
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        //recyclerView 영역
        recyclerView = findViewById(R.id.main_recyclerview);
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
                Intent intent_view_change = new Intent(getApplicationContext(), WritePostActivity.class);
                intent_view_change.putExtra("generation", generation);
                intent_view_change.putExtra("name", name);
                startActivity(intent_view_change);
            }
        });


        // + 버튼 누르면 속성 버튼 생성
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addBtn2.getVisibility() == View.GONE && addBtn3.getVisibility() == View.GONE) {
                    addBtn2.setVisibility(View.VISIBLE); // or GONE
                    addBtn3.setVisibility(View.VISIBLE); // or GONE
                    bt_write_post.setVisibility(View.VISIBLE); // or GONE
                } else {
                    addBtn2.setVisibility(View.GONE);
                    addBtn3.setVisibility(View.GONE);
                    bt_write_post.setVisibility(View.GONE);
                }
            }
        });

        // 새로고침 기능
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

//    private String getTime() {
//        //현재 날짜 받아오기
//        mNow = System.currentTimeMillis();
//        mDate = new Date(mNow);
//
//        return format.format(mDate);
//    }

    private void getPost() {
        //리스트 지정
        final List<Data> dataList = new ArrayList<>();

        //게시물 정보 파이어베이스에서 받아오기
        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    key = Snapshot.getKey();
                    getPost = Snapshot.getValue(getPost.class);
                    dataList.add(new Data(getPost.getTitle(), getPost.getMain_text()));

                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT);
            }
        };
        databaseReference.child("post_save").addValueEventListener(mValueEventListener);

        adapter = new RecyclerAdapter(dataList);

        //게시물 클릭 영역
        adapter.setOnClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View a_v, int position) {
                final Data data = dataList.get(position);
                main_layout.setVisibility(View.GONE);
                post_long.setVisibility(View.VISIBLE);
                post_title.setText(data.getTitle());
                post_main_text.setText(data.getMain_text());
            }
        });

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {

        //만약 게시물 큰 화면이 켜져있다면 뒤로가기 눌렀을때 큰 화면이 꺼지게
        int result = post_long.getVisibility();
        if (result == View.VISIBLE) {
            post_long.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
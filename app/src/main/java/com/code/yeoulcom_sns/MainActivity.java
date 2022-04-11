package com.code.yeoulcom_sns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.yeoulcom_sns.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    // 새로고침
    private SwipeRefreshLayout swipeRefreshLayout;

    // + 버튼 누르면 버튼 생성
    Button addBtn, addBtn2, addBtn3;

    Button postBtn, conferenceBtn, voteBtn, bt_write_post, Chairman_Btn;
    String name, generation, key, Time;

    //큰 게시물 TextView
    TextView post_long_title, post_long_main_text, post_long_name_generation, post_long_time;
    ImageView post_long_IV;
    String post_long_name, post_long_generation;

    // 게시물 받아오는 클래스 참조
    getPost getPost;

    ImageButton IV_onBack;

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

    //리스트 지정
    final List<Data> dataList = new ArrayList<>();

    //로딩창
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        init();
        //기수 이름이 없을 시 첫 화면으로
//        if (name == "" || generation == ""){
//            Toast.makeText(getApplicationContext(),"승인되지 않은 사용자입니다.",Toast.LENGTH_SHORT);
//            Intent intent_view_change = new Intent(getApplicationContext(),InputInformationActivity.class);
//            startActivity(intent_view_change);
//        }
        getPost();
        onclick();
        RunProgressDialog();

    }
    public void RunProgressDialog(){
        //게시물 불러올 동안 뜨는 로딩 Dialog
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("게시물 로드 중. . .");

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1500);
    }

    public void init() {
        //기본 세팅 함수
        postBtn = (Button) findViewById(R.id.postBtn);
        conferenceBtn = (Button) findViewById(R.id.conferenceBtn);
        voteBtn = (Button) findViewById(R.id.voteBtn);
        Chairman_Btn = (Button) findViewById(R.id.Chairman_Btn);
        bt_write_post = (Button) findViewById(R.id.bt_write_post);

        post_long = (LinearLayout) findViewById(R.id.post_long);
        post_long_main_text = (TextView) findViewById(R.id.post_long_main_text);
        post_long_name_generation = (TextView) findViewById(R.id.post_long_name_generation);
        post_long_title = (TextView) findViewById(R.id.post_long_title);
        post_long_time = (TextView) findViewById(R.id.post_long_time);
        post_long_IV = (ImageView)findViewById(R.id.post_long_IV);

        // 뒤로가기 버튼
        IV_onBack = (ImageButton) findViewById(R.id.IV_onBack);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);

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
        recyclerView.setItemAnimator(null);

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

        // 게시물창에서 뒤로가기 누르면 메인 화면으로 이동
        IV_onBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        // 회장페이지 버튼 클릭 시 이동 (오직 임원에게만 Active)
        Chairman_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LeadersActivity.class);
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

                updateLayoutView();

                swipeRefreshLayout.setRefreshing(false);
            }

            // 새로고침 하면 스피너 창 닫기 변경 영역
            public void updateLayoutView() {
                if (addBtn2.getVisibility() + addBtn3.getVisibility() + bt_write_post.getVisibility() == View.VISIBLE) {
                    addBtn2.setVisibility(View.GONE);
                    addBtn3.setVisibility(View.GONE);
                    bt_write_post.setVisibility(View.GONE);
                }
                // 게시물 위로 올리기
                LinearLayoutManager mlayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                recyclerView.smoothScrollToPosition(dataList.size()); // 개시물의 갯수대로 적어야 최상위로 올라가는 것 같은데 이부분은 조만간 수정해야됨.
                // 03/11(수정완료)
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

        //게시물 정보 파이어베이스에서 받아오기
        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    key = Snapshot.getKey();
                    getPost = Snapshot.getValue(getPost.class);
                    dataList.add(new Data(getPost.getTitle(),
                            getPost.getMain_text(),
                            getPost.getImgURL(),
                            getPost.getTime(),
                            getPost.getName(),
                            getPost.getGeneration()));
                }

                adapter.notifyDataSetChanged();
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
                RunProgressDialog();//게시물 클릭 시 이미지 로딩 Dialog

                final Data data = dataList.get(position);
                main_layout.setVisibility(View.GONE);
                post_long.setVisibility(View.VISIBLE);
                post_long_title.setText(data.getTitle());
                post_long_main_text.setText(data.getMain_text());
                post_long_time.setText(data.getTime());
                post_long_name_generation.setText(data.getGeneration()+" "+data.getName());
                post_long_IV.setImageResource(R.drawable.whiteimage);
                
                //게시물 안에 있는 이미지 이름으로 파이어베이스에서 가져오기
                StorageReference pathReference = storageReference.child("img/"+data.getImgUrl());
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .error(R.drawable.yeoul)//이미지 로드 실패 시 보여줄 이미지
                                .fallback(R.drawable.common_google_signin_btn_icon_dark_normal_background)//uri이 null일 때
                                .into(post_long_IV);
                    }
                });
            }
        });

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //위에서 부터 쌓기위한 코드
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
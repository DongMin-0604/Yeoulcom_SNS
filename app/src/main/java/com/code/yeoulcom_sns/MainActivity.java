package com.code.yeoulcom_sns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.app.Activity;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yeoulcom_sns.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    String name = "", generation = "", key, Time;
    Boolean adminCheck = false, OkayCheck = false;

    //큰 게시물 TextView
    TextView post_long_title, post_long_main_text, post_long_name_generation, post_long_time;
    ImageView post_long_IV;
    String post_long_name, post_long_generation;

    // 게시물 받아오는 클래스 참조
    getPost getPost;

    ImageButton IV_onBack;

    //레이아웃 터치시 큰 화면으로 전환을 위한 레이아웃 정의
    NestedScrollView post_long_scrollview;
    LinearLayout post_long;
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

    //뒤로가기 두번 입력 체크용
    private long backKeyPressedTime;
    Toast toast;

    //기수,이름 핸드폰에 저장 SharedPreferences
    SharedPreferences SP;
    SharedPreferences.Editor editor;

    //버튼 중복 클릭 막기
    private long mLastClickTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected(getApplicationContext()) == false) {
            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            finishAndRemoveTask();
            onDestroy();
            android.os.Process.killProcess(android.os.Process.myTid());
        } else {

        }
        init();
        if (name == "" || generation == "") {
            Toast.makeText(getApplicationContext(), "승인되지 않은 사용자입니다.", Toast.LENGTH_SHORT).show();
            Intent intent_view_change = new Intent(getApplicationContext(), InputInformationActivity.class);
            startActivity(intent_view_change);
        }
        getPost();
        onclick();
        RunProgressDialog();
    }


    public void RunProgressDialog() {
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
        }, 2000);
    }
    public void RunSaveDialog() {
        //게시물 불러올 동안 뜨는 로딩 Dialog
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 베이스 안정화 중 \n추후 최적화가 되면 이 Dialog는 사라질겁니다. . .");

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3500);
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
        post_long_IV = (ImageView) findViewById(R.id.post_long_IV);
        post_long_scrollview = (NestedScrollView) findViewById(R.id.post_long_scrollview);

        // 뒤로가기 버튼
        IV_onBack = (ImageButton) findViewById(R.id.IV_onBack);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        //SharedPreferences
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        editor = SP.edit();

        //SharedPreferences 기수,이름 받기
        SP = getSharedPreferences("SP", Activity.MODE_PRIVATE);
        name = SP.getString("name", "");
        generation = SP.getString("generation", "");
        adminCheck = SP.getBoolean("admin", false);

        // + 버튼 누르면 버튼 생성
        addBtn = (Button) findViewById(R.id.about_btn);
        addBtn2 = (Button) findViewById(R.id.about_btn2);
        addBtn3 = (Button) findViewById(R.id.about_btn3);

        //새로고침
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        //recyclerView 영역
        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setItemAnimator(null);
        //recyclerView 기존 성능 강화?
        recyclerView.setHasFixedSize(true);

    }

    public void onclick() {
        //onclick 모아 놓는 함수
        // 컨퍼런스 클릭 시 이동
        conferenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunSaveDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), conference.class);
                        //화면 전환, 기수,이름,권한 정보 다음 엑티비티로 넘기기
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 3500);
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
                RunSaveDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), vote.class);
                        //화면 전환, 기수,이름,권한 정보 다음 엑티비티로 넘기기
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 3500);

            }
        });

        // 회장페이지 버튼 클릭 시 이동 (오직 임원에게만 Active)
        Chairman_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunSaveDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adminCheck == true) {
                            Intent intent = new Intent(getApplicationContext(), LeadersActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else {
                            Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3500);
            }
        });

        //게시물 작성 클릭 시 이동
        bt_write_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_view_change = new Intent(getApplicationContext(), WritePostActivity.class);
                intent_view_change.putExtra("generation", generation);
                intent_view_change.putExtra("name", name);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                startActivity(intent_view_change);
            }
        });

        // 문의하기 클릭 시 이동
        addBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //화면 전환
                Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                startActivity(intent);
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
                //만약 게시물 큰 화면이 켜져있다면 새로고침 기능 막기
                int result = post_long.getVisibility();
                if (result == View.VISIBLE) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    updateLayoutView();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            // 새로고침 하면 스피너 창 닫기 변경 영역
            public void updateLayoutView() {
                if (addBtn2.getVisibility() + addBtn3.getVisibility() + bt_write_post.getVisibility() == View.VISIBLE) {
                    addBtn2.setVisibility(View.GONE);
                    addBtn3.setVisibility(View.GONE);
                    bt_write_post.setVisibility(View.GONE);
                }
                // 게시물 위로 올리기
//                LinearLayoutManager mlayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                recyclerView.smoothScrollToPosition(dataList.size()); // 개시물의 갯수대로 적어야 최상위로 올라가는 것 같은데 이부분은 조만간 수정해야됨.
                // 03/11(수정완료)
            }
        });

        // 이메일 클릭하면 이메일 띄우기
        addBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBtn2 = new Intent(Intent.ACTION_SEND);
                addBtn2.setType("Plain/text");
                String[] address = {"dongmin3147@gmail.com"};
                addBtn2.putExtra(Intent.EXTRA_EMAIL, address);
                addBtn2.putExtra(Intent.EXTRA_SUBJECT, "오류 피드백입니다.");
                addBtn2.putExtra(Intent.EXTRA_TEXT, "내용 미리보기 (미리 적을 수 있음");
                startActivity(addBtn2);
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
                    dataList.add(new Data(
                            getPost.getTitle(),
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
                Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.child("post_save").addValueEventListener(mValueEventListener);

        adapter = new RecyclerAdapter(dataList);


        //게시물 클릭 영역
        adapter.setOnClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View a_v, int position) {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    mLastClickTime = SystemClock.elapsedRealtime();
                    RunProgressDialog();//게시물 클릭 시 이미지 로딩 Dialog

                    final Data data = dataList.get(position);
                    main_layout.setVisibility(View.GONE);
                    post_long.setVisibility(View.VISIBLE);
                    post_long_scrollview.setVisibility(View.VISIBLE);

                    post_long_title.setText(data.getTitle());
                    post_long_main_text.setText(data.getMain_text());
                    post_long_time.setText(data.getTime());
                    post_long_name_generation.setText(data.getGeneration() + " " + data.getName());
                    post_long_IV.setImageResource(R.drawable.whiteimage);
                    Log.d("img", data.getImgUrl());

                    if (data.getImgUrl().equals("content://media/external/images/media/4167")) {
                        //기본 이미지는 큰 게시물 안쪽에 표시 X
                    } else {
                        //게시물 안에 있는 이미지 이름으로 파이어베이스에서 가져오기
                        StorageReference pathReference = storageReference.child("img/" + data.getImgUrl());
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
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //위에서 부터 쌓기위한 코드
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    static Boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    @Override
    public void onBackPressed() {
        //만약 게시물 큰 화면이 켜져있다면 뒤로가기 눌렀을때 큰 화면이 꺼지게
        int result = post_long.getVisibility();
        if (result == View.VISIBLE) {
            post_long.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            post_long_scrollview.setVisibility(View.GONE);
        } else {
            //super.onBackPressed();
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                moveTaskToBack(true);
                finish();
                toast.cancel();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
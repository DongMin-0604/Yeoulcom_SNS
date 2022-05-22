package com.code.yeoulcom_sns;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;
import java.util.List;

public class LeadersActivity extends AppCompatActivity {
    //회장 전용 관리 페이지

    // 게시물 받아오는 클래스 참조
    GetCheckUser GetCheckUser;
    String name = "", generation = "", key, Time;
    Boolean adminCheck = false;

    //리스트 지정
    final List<UserCheckData> dataList = new ArrayList<>();

    //파이어 베이스 연동
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    //recyclerView 영역
    RecyclerView recyclerView;
    private RecyclerAdapter_Leaders adapter;

    //로딩창
    ProgressDialog dialog;

    //새로고침
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders_page);
        init();
        getPost();

    }

    public void init() {
        //recyclerView 영역
        recyclerView = findViewById(R.id.leaders_recyclerview);
        recyclerView.setItemAnimator(null);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        // 새로고침 기능
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //만약 게시물 큰 화면이 켜져있다면 새로고침 기능 막기
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void getPost() {
        //리스트 지정

        //게시물 정보 파이어베이스에서 받아오기

        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    key = Snapshot.getKey();
                    GetCheckUser = Snapshot.getValue(GetCheckUser.class);
                    dataList.add(new UserCheckData(GetCheckUser.getGeneration(), GetCheckUser.getName()));
                    Log.d("Leaders", GetCheckUser.getGeneration() + GetCheckUser.getName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT);
            }
        };
        databaseReference.child("Check_user_List").addValueEventListener(mValueEventListener);

        adapter = new RecyclerAdapter_Leaders(dataList);

//        //게시물 클릭 영역
//        adapter.setOnClickListener(new RecyclerAdapter_Leaders.OnItemClickListener() {
//            @Override
//            public void onItemClick(View a_v, int position) {
//                RunProgressDialog();//게시물 클릭 시 이미지 로딩 Dialog
//
//                final UserCheckData userCheckData = dataList.get(position);
//                //버튼 클릭 구글링 필요
////                main_layout.setVisibility(View.GONE);
////                post_long.setVisibility(View.VISIBLE);
////                post_long_title.setText(data.getTitle());
////                post_long_main_text.setText(data.getMain_text());
////                post_long_time.setText(data.getTime());
////                post_long_name_generation.setText(data.getGeneration() + " " + data.getName());
////                post_long_IV.setImageResource(R.drawable.whiteimage);
//            }
//        });
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        //위에서 부터 쌓기위한 코드
        ((LinearLayoutManager) layoutManager2).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager2).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(adapter);
    }

    public void RunProgressDialog() {
        //게시물 불러올 동안 뜨는 로딩 Dialog
        dialog = new ProgressDialog(LeadersActivity.this);
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
}

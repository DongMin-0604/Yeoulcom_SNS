package com.code.yeoulcom_sns;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yeoulcom_sns.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{

    //리스트에 든 값 RecyclerView에 넣는 자바
    
    //파이어베이스 스토리지 접근 권한
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    //온 클릭 지정
    public interface OnItemClickListener{
        void onItemClick(View a_v, int position);
    }
    public List<Data> mList;

    private OnItemClickListener mListener = null;

    public RecyclerAdapter(List<Data> a_list){
        mList = a_list;
    }

    public void setOnClickListener(OnItemClickListener aListener){
        this.mListener = aListener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d("1","onBind 실행됨");
        final Data data = mList.get(position);
        if (data.getImgUrl() != null){
            holder.title.setText(data.getTitle());
            holder.main_text.setText(data.getMain_text());
            StorageReference pathReference = storageReference.child("img/"+data.getImgUrl());
            holder.imageView.setImageResource(R.drawable.whiteimage);
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext())
                            .load(uri)
                            .override(200,200)
                            .error(R.drawable.common_google_signin_btn_icon_dark)//이미지 로드 실패 시 보여줄 이미지
                            .fallback(R.drawable.common_google_signin_btn_icon_dark_normal_background)//uri이 null일 때
                            .into(holder.imageView);

//        Picasso.get().load(data.getImgUrl()).into(holder.imageView);//피카소 라이브러이리 이용
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView title, main_text;
        private ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // 클릭 메소드 사용
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if (mListener != null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
            title = itemView.findViewById(R.id.post_title_item);
            main_text = itemView.findViewById(R.id.post_main_text_item);
            imageView = itemView.findViewById(R.id.post_img_item);
        }
    }
}


package com.code.yeoulcom_sns;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yeoulcom_sns.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{

    //리스트에 든 값 RecyclerView에 넣는 자바

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
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Data data = mList.get(position);
        holder.title.setText(data.getTitle());
        holder.main_text.setText(data.getMain_text());
        Glide.with(holder.itemView.getContext())
                .load(data.getImgUrl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)// 이미지 로드중 잠시 띄울 이미지
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)//이미지 로드 실패 시 보여줄 이미지
                .fallback(R.drawable.common_google_signin_btn_icon_dark_normal_background)//uri가 null일 때
                .into(holder.imageView);

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


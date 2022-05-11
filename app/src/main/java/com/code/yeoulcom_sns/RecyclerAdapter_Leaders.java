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

public class RecyclerAdapter_Leaders extends RecyclerView.Adapter<RecyclerAdapter_Leaders.ItemViewHolder> {

    //리스트에 든 값 RecyclerView에 넣는 자바

    //온 클릭 지정
    public interface OnItemClickListener {
        void onItemClick(View a_v, int position);
    }

    public List<UserCheckData> mList;

    private OnItemClickListener mListener = null;

    public RecyclerAdapter_Leaders(List<UserCheckData> a_list) {
        mList = a_list;
    }

    public void setOnClickListener(OnItemClickListener aListener) {
        this.mListener = aListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new RecyclerAdapter_Leaders.ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d("1", "onBind 실행됨");
        final UserCheckData userCheckData = mList.get(position);
        holder.generation.setText(userCheckData.getGeneration());
        holder.Name.setText(userCheckData.getName());
        holder.imageView.setImageResource(R.drawable.whiteimage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView generation, Name;

        private ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // 클릭 메소드 사용
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });

            generation = itemView.findViewById(R.id.post_title_item);
            Name = itemView.findViewById(R.id.post_main_text_item);
            imageView = itemView.findViewById(R.id.post_img_item);
        }
    }
}

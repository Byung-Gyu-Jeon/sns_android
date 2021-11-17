package com.example.sns.main.ui.Myprofile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sns.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyprofileAdapter extends RecyclerView.Adapter<MyprofileAdapter.ItemViewHolder> {

    @NonNull
    @Override
    public MyprofileAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item6,parent,false);
        return new MyprofileAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyprofileAdapter.ItemViewHolder holder, int position) {
       holder.Bind();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageView imageView2;
        private ImageView imageView3;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView19);
            imageView = itemView.findViewById(R.id.imageView20);
            imageView = itemView.findViewById(R.id.imageView21);
        }

        void Bind() {

        }
    }
}

package com.example.sns.main.ui.feed.Recomment;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sns.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommentAdapter extends RecyclerView.Adapter<RecommentAdapter.ItemViewHolder> {
    ArrayList<Data5> dataa2 = new ArrayList<>();

    @NonNull
    @Override
    public RecommentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5,parent,false);
        return new RecommentAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommentAdapter.ItemViewHolder holder, int position) {
        holder.Bind();
    }

    @Override
    public int getItemCount() {
        return dataa2.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageView imageView2;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView15);
            imageView2 = itemView.findViewById(R.id.imageView16);
            textView = itemView.findViewById(R.id.textView35);
            textView2 = itemView.findViewById(R.id.textView36);
            textView3 = itemView.findViewById(R.id.textView31);
            textView3 = itemView.findViewById(R.id.textView37);
        }
        void Bind(){
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
        }
    }
}

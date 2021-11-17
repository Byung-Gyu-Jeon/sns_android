package com.example.sns.main.ui.friend;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sns.R;
import com.example.sns.main.ui.feed.CommentAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ItemViewHolder> {

    ArrayList<Data3> dataa = new ArrayList<>();

    @NonNull
    @Override
    public FriendAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, parent,false);
        return new FriendAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ItemViewHolder holder, int position) {
        holder.Bind(dataa.get(position));
    }

    @Override
    public int getItemCount() {
        return dataa.size();
    }
    void addItem(Data3 data){
        dataa.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView4);
            textView = itemView.findViewById(R.id.textView21);

        }
        void Bind(Data3 data){
            //imageView.setBackground(new ShapeDrawable(new OvalShape()));
            //imageView.setClipToOutline(true);
            textView.setText(data.getName());
        }

    }
}

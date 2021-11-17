package com.example.sns.main.ui.friend;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.sns.R;

import java.util.ArrayList;

import javax.crypto.AEADBadTagException;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

public class FriendEditAdapter extends RecyclerView.Adapter<FriendEditAdapter.ItemViewHolder> {
    ArrayList<Data4> dataa = new ArrayList<>();

    @NonNull
    @Override
    public FriendEditAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item4,parent,false);
        return new FriendEditAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendEditAdapter.ItemViewHolder holder, int position) {
        holder.Bind();
    }

    @Override
    public int getItemCount() {
        return dataa.size();
    }
    void addItem(Data4 data) {dataa.add(data);}

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageButton imageButton;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView5);
            imageButton = itemView.findViewById(R.id.imageButton3);
        }
        void Bind(){
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);

        }
    }
}

package com.example.sns.main.ui.feed;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sns.R;
import com.example.sns.main.ui.friend.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//게시글 추가화면의 리사이클러뷰 어뎁터
public class MainprofileaddAdapter extends RecyclerView.Adapter<MainprofileaddAdapter.ViewHolder>{

    public ArrayList<Uri> mData=null;
    public Context mContext = null;

    public MainprofileaddAdapter(ArrayList<Uri> list, Context context){
        mData = list;
        mContext = context;
    }

    @NonNull
    @Override
    public MainprofileaddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mainprofileadditem,parent,false);
        MainprofileaddAdapter.ViewHolder vh = new MainprofileaddAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainprofileaddAdapter.ViewHolder holder, int position) {
        Uri image_uri = mData.get(position);

        Glide.with(mContext).load(image_uri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;

        ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView66);
        }
    }

}



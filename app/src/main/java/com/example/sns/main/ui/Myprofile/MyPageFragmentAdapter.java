package com.example.sns.main.ui.Myprofile;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sns.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPageFragmentAdapter extends RecyclerView.Adapter<MyPageFragmentAdapter.ViewHolder> {

    private static final String TAG = "MyPageFragmentAdapter";
    public List<FeedimagelistDATA> data;
    public Context context = null;

    public MyPageFragmentAdapter(List<FeedimagelistDATA> data, Context context){
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public MyPageFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mypagefragmentitem,parent,false);
        MyPageFragmentAdapter.ViewHolder vh = new MyPageFragmentAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageFragmentAdapter.ViewHolder holder, int position) {

            Glide.with(context).load("http://59.13.221.12:80/sns/download2?fileName="+data.get(position).getImagename()).into(holder.imageView);

               Log.d(TAG,data.get(position).getImagename());

            //Log.d(TAG,position+"");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ViewHolder(View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView6);
//            imageView.setBackground(new ShapeDrawable(new OvalShape()));
//            imageView.setClipToOutline(true);
        }
    }

}

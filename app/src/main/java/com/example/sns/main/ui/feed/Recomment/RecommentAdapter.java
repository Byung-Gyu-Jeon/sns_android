package com.example.sns.main.ui.feed.Recomment;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.R;
import com.example.sns.main.ui.feed.item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommentAdapter extends RecyclerView.Adapter<RecommentAdapter.ItemViewHolder> {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH= 12;

    ArrayList<item> items = new ArrayList<>();
    Context context;
    private static final String TAG = "RecommentActivity2";

    public RecommentAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public RecommentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5,parent,false);
        return new RecommentAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommentAdapter.ItemViewHolder holder, int position) {
        item item = items.get(position);
        ((ItemViewHolder)holder).setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    void addItem(item item){items.add(item);}

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageView imageView2;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView15);
            textView = itemView.findViewById(R.id.textView31);
            textView2 = itemView.findViewById(R.id.textView35);
            textView3 = itemView.findViewById(R.id.textView36);

        }
       public void setItem(item item){
           Date date = null;
           String msg = null;
           long begintime=0;

           //댓글 작성시의 시간
           String Recommenttime = item.getRecommenttime();
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           try{
               date = sdf.parse(Recommenttime);
               //답글 작성한 시간
               begintime = date.getTime();
               //Log.d(TAG,"댓글 작성한 시간:"+begintime);

           }catch(Exception e){
               e.printStackTrace();
           }

           //답글 화면에 접속했을때의 시간
           String commenttimes2;
           //답글 화면에 접속한 시간 구하기
           long curTime = System.currentTimeMillis();
          // Log.d(TAG,"답글 화면 접속 시간:"+curTime);

           long diffTime = (curTime-begintime) / 1000;

           if(diffTime<SEC){
               msg = "방금전";
           }
           else if((diffTime/=SEC)<MIN){
               msg=diffTime+"분전";
           }
           else if((diffTime/=MIN)<HOUR){
               msg=diffTime+"시간 전";
           }
           else if((diffTime/=HOUR)<DAY){
               msg=diffTime+"일 전";
           }
           else if((diffTime/=DAY)<MONTH){
               msg=diffTime+"달 전";
           }
           else{
               msg=(diffTime)+"년 전";
           }

           Glide.with(context).load("http://192.168.0.2:8080/sns/download?fileName="+item.getProfilename()).apply(new RequestOptions().circleCrop()).into(imageView);
           textView.setText(item.getUserName());
           textView2.setText(msg);
           textView3.setText(item.getRecommenttext());

        }
    }
}

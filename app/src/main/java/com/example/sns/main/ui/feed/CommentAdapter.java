package com.example.sns.main.ui.feed;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.R;
import com.example.sns.main.ui.feed.Recomment.RecommentActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH= 12;

    private static final String TAG = "CommentActivity2";

    String msg = null;

    Context context;
    ArrayList<item> items = new ArrayList<>();
    public CommentAdapter(Context context) {this.context = context; }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ItemViewHolder holder, int position) {
        item item =items.get(position);
        ((ItemViewHolder)holder).setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    void addItem(item item){
        items.add(item);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView6;
        private TextView textView5;
        private Button btn;



        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            textView3 = itemView.findViewById(R.id.textview3);
            //답글 메뉴
            textView6 = itemView.findViewById(R.id.textvieww6);
            btn = itemView.findViewById(R.id.button);
            //답글 갯수
            textView5 = itemView.findViewById(R.id.textview5);

        }

        public void setItem(item item){

            Date date = null;

            long begintime=0;
            String commentsno = item.getCommentsno();

            //댓글 작성시의 시간
            String commenttime = item.getCommenttime();
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           try{
                date = sdf.parse(commenttime);
               //댓글 작성한 시간
                begintime = date.getTime();
               Log.d(TAG,"댓글 작성한 시간:"+begintime);

           }catch(Exception e){
              e.printStackTrace();
           }


            //댓글 화면에 접속했을때의 시간
            String commenttimes2;
            //댓글 화면에 접속한 시간 구하기
            long curTime = System.currentTimeMillis();
            Log.d(TAG,"댓글 화면 접속 시간:"+curTime);

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
           textView2.setText(item.getText());
           textView.setText(item.getUserName());
           textView3.setText(msg);
            Log.d(TAG,"답글갯수:"+item.getRecomment_count());
           textView5.setText(item.getRecomment_count()+"");

           //댓글 화면에서 답글 버튼을 클릭시 답글 화면으로 넘어간다 (내 이름, 내 프로필 사진 , 댓글 내용, 댓글 작성후 경과 시간등을 답글 화면으로 전송한다)
           textView6.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, RecommentActivity.class);
                   intent.putExtra("commentsno",commentsno);
                   intent.putExtra("username",item.getUserName());
                   intent.putExtra("picname",item.getProfilename());
                   intent.putExtra("msg",msg);
                   intent.putExtra("commenttext",item.getText());
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(intent);

               }
           });
        }

    }
}

package com.example.sns.main.ui.feed;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {

    private CommentAdapter adapter;
    private RetrofitService retrofitService;
    public static ApiClient ourInstance = null;
    //작성한 댓글 내용과 feed_no를 보내기 위한 url
    private final static String BASE_URL = "http://192.168.0.2:8080/sns/comments/";
    //댓글 화면에서 필요한 프로필 사진 , 이름 ,댓글 내용을 가져오기 위한 코드
    private final static String BASE_URL2 = "http://192.168.0.2:8080/sns/gett3/";
    private static final String TAG = "CommentActivity";

    //댓글 화면에서 필요한 정보들을 가져온 데이터를 저장한 리스트
    List<CommenttextDATA> dataList = new ArrayList<>();

    //댓글이 작성된 시간
    String commenttimes;

    //답글 갯수
    String recommentnum;



    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //뒤로가기 버튼
        ImageButton button = (ImageButton) findViewById(R.id.imageButtoncom);
        //댓글 작성후 확인 버튼
        ImageButton button2 = (ImageButton) findViewById(R.id.imageButton5);
        //댓글 작성하는 edit text 칸
        EditText editText = (EditText) findViewById(R.id.editText2);




        class sendrecommentcount extends BroadcastReceiver{

            @Override
            public void onReceive(Context context,Intent intent){
                recommentnum = intent.getStringExtra("recommentnum");
                Log.d(TAG,"답글화면에서 댓글화면으로 보낸 답글 갯수:"+recommentnum);
            }
        }


         //메인화면에서 보낸 feedno 정보를 받는다.
        Intent intent = getIntent();
        String feedno = intent.getStringExtra("feedno");
        //Log.d(TAG,"메인화면에서 보낸 feedno:"+feedno);





        //댓글 화면에서 필요한 프로필 사진 이름, 내 이름, 댓글 내용을 서버로부터 가져온다
        retrofitService = ourInstance.getInstance(BASE_URL2,true).create(RetrofitService.class);

        //feed_no를 보내준다
        Call<MyProfileDTO> call = retrofitService.getData3(feedno);
        call.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO> call, Response<MyProfileDTO> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"서버에서 데이터 가져오기 성공");
                    MyProfileDTO dto = new MyProfileDTO();
                    dto = response.body();

                    for(int i=0;i<dto.data.size();i++){
                        CommenttextDATA data = new CommenttextDATA();
                        data.setPicname(dto.data.get(i).getPicname());
                        Log.d(TAG,"사진이름:"+data.getPicname());
                        data.setUsername(dto.data.get(i).getUsername());
                        Log.d(TAG,"내이름:"+data.getUsername());
                        data.setText(dto.data.get(i).getText());
                        Log.d(TAG,"댓글내용:"+data.getText());
                        data.setUserno(dto.data.get(i).getUserno());
                        Log.d(TAG,"user_no:"+data.getUserno()+"");
                        data.setCommenttime(dto.data.get(i).getCommenttime());
                        Log.d(TAG,"댓글 작성시간:"+data.getCommenttime());
                        data.setCommentsno(dto.data.get(i).getCommentsno());
                        Log.d(TAG,"댓글번호:"+data.getCommentsno());
                        data.setRecomment_count(dto.data.get(i).getRecomment_count());
                        Log.d(TAG,"답글 갯수:"+data.getRecomment_count());
                        dataList.add(data);

                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    CommentAdapter adapter = new CommentAdapter(getBaseContext());

                    for(int i=0;i<dataList.size();i++){
                        adapter.addItem(new item(dataList.get(i).getUsername(),dataList.get(i).getPicname(),dataList.get(i).getText(),dataList.get(i).getUserno(),dataList.get(i).getCommenttime(),feedno,dataList.get(i).getCommentsno(),dataList.get(i).getRecomment_count()));
                    }

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {
                Log.d(TAG,"서버에서 데이터 가져오기 실패");
            }
        });





        //뒤로가기 버튼 클릭시
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //댓글 작성후 버튼을 눌렀을때 서버로 데이터 전송
        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                //댓글 작성한 시간 구하기
                long curTime = System.currentTimeMillis();
                commenttimes = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                //commenttimes = sdf.format(new Date(curTime));
                Log.d(TAG,"댓글 작성한 시간:"+commenttimes);


                String textt = editText.getText().toString();
                RequestBody text = RequestBody.create(MediaType.parse("text/plain"),textt);
                RequestBody feednoo = RequestBody.create(MediaType.parse("text/plain"),feedno);
                RequestBody commenttime = RequestBody.create(MediaType.parse("text/plain"),commenttimes);

                HashMap<String,RequestBody> requestMapp = new HashMap<>();
                requestMapp.put("text",text);
                requestMapp.put("feedno",feednoo);
                requestMapp.put("commenttime",commenttime);




                retrofitService = ourInstance.getInstance(BASE_URL,true).create(RetrofitService.class);
                Call<CommenttextDATA> call = retrofitService.postData4(requestMapp);

                call.enqueue(new Callback<CommenttextDATA>() {
                    @Override
                    public void onResponse(Call<CommenttextDATA> call, Response<CommenttextDATA> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"댓글 데이터를 서버에 보내는것 성공");
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommenttextDATA> call, Throwable t) {
                        Log.d(TAG,"댓글 데이터를 서버에 보내는것 실패");
                    }
                });
            }
        });
    }

    //메인 화면에서 보낸 feed 번호를 받기위한 코드
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

    }
}
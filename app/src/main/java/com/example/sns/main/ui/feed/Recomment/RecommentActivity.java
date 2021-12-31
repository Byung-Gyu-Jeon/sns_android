package com.example.sns.main.ui.feed.Recomment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.example.sns.main.ui.feed.CommentActivity;
import com.example.sns.main.ui.feed.CommenttextDATA;
import com.example.sns.main.ui.feed.item;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommentActivity extends AppCompatActivity {

    private RecommentAdapter adapter;
    private RetrofitService retrofitService;
    public static ApiClient ourInstance = null;
    private static final String TAG = "RecommentActivity";

    //내가 작성한 답글을 서버에 전송하기 위한 url
    private final static String BASE_URL = "http://192.168.0.2:8080/sns/recomment/";

    //답글화면에서 필요한 1.내 이름 2.프로필 사진 3.답글 내용 4.작성시간을 가져온다
    private final static String BASE_URL2 = "http://192.168.0.2:8080/sns/getrecommentdata/";

    //답글이 작성된 시간
    String recommenttime;

    //댓글 화면에서 보낸 commentsno
    String commentsno = null;

    //내가 작성한 답글을 가져온다
    String recomment;

    //답글 화면에서 필요한 정보들을 가져온 데이터를 저장한 리스트
    List<CommenttextDATA> dataList = new ArrayList<>();

    //답글화면의 답글 갯수를 세기 위한 변수
    int recommentcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomment);

        RecyclerView recyclerView = findViewById(R.id.recycler);


        //뒤로가기 버튼
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton6);
        //답글 작성후 확인 버튼
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton4);
        //답글을 작성한 에딧 텍스트
        EditText editText = (EditText) findViewById(R.id.editText33);
        //프로필 사진
        ImageView imageView = (ImageView) findViewById(R.id.imageView8);
        //유저이름
        TextView textView = (TextView) findViewById(R.id.textView28);
        //댓글 작성 시간
        TextView textView2 = (TextView) findViewById(R.id.textView29);
        //댓글 내용
        TextView textView3 = (TextView) findViewById(R.id.textView30);
        //답글의 갯수
        TextView textView4 = (TextView) findViewById(R.id.textView34);

        //댓글 화면에서 commentsno번호를 보내준다
        Intent intent = getIntent();
        //댓글 화면에서 보내준 데이터들을 받기위한 코드
        String username = null;
        String picname = null;
        String msg = null;
        String commenttext = null;

        //댓글 화면에서 프로필 사진 이름을 가져온다
        picname = intent.getStringExtra("picname");
        Glide.with(getApplicationContext()).load("http://192.168.0.2:8080/sns/download?fileName="+picname).apply(new RequestOptions().circleCrop()).into(imageView);

        //이름,댓글 작성후 경과시간,댓글 내용을 가져온다
        username = intent.getStringExtra("username");
        textView.setText(username);

        msg = intent.getStringExtra("msg");
        textView2.setText(msg);

        commenttext = intent.getStringExtra("commenttext");
        textView3.setText(commenttext);


        commentsno = intent.getStringExtra("commentsno");
        //Log.d(TAG,"댓글 화면에서 가져온 commentsno:"+commentsno);

        //답글 화면에 접속했을때 필요한 1.프로필 사진 2.이름 3.답글 내용 4. 답글 작성 시간 5.댓글 번호(comments_no)을 가져온다
        retrofitService = ourInstance.getInstance(BASE_URL2,true).create(RetrofitService.class);

        //commentsno를 보내준다
        Call<MyProfileDTO> call = retrofitService.getData4(commentsno);
        call.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO> call, Response<MyProfileDTO> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"서버에서 데이터 가져오기 성공");
                    MyProfileDTO dto = new MyProfileDTO();
                    dto = response.body();

                    for(int i=0;i<dto.recommentdata.size();i++){
                        CommenttextDATA data = new CommenttextDATA();
                        data.setPicname(dto.recommentdata.get(i).getPicname());
                       // Log.d(TAG,"사진이름:"+data.getPicname());
                        data.setUsername(dto.recommentdata.get(i).getUsername());
                       // Log.d(TAG,"내이름:"+data.getUsername());
                        data.setRecommenttext(dto.recommentdata.get(i).getRecommenttext());
                      //  Log.d(TAG,"답글 내용:"+data.getRecommenttext());
                        data.setRecommenttime(dto.recommentdata.get(i).getRecommenttime());
                      //  Log.d(TAG,"답글 작성 시간:"+data.getRecommenttime());
                        data.setComments_no(dto.recommentdata.get(i).getComments_no());
                       // Log.d(TAG,"답글 화면에서 필요한 댓글 번호:"+data.getComments_no());
                        dataList.add(data);
                    }



                    //답글의 갯수를 count한다
                    String recommentnumber = dataList.size()+"";
                    textView4.setText(recommentnumber);

                    Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
                    intent.putExtra("recommentnum",recommentnumber);
                    sendBroadcast(intent);






                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    RecommentAdapter adapter = new RecommentAdapter(getBaseContext());

                    for(int i=0;i<dataList.size();i++){
                        adapter.addItem(new item(dataList.get(i).getUsername(),dataList.get(i).getPicname(),dataList.get(i).getRecommenttext(),dataList.get(i).getRecommenttime()));
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



        //뒤로 가기 버튼을 눌렀을때 댓글 화면으로 돌아간다
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //답글 작성후 버튼 눌렀을때 서버에 내가 작성한 답글을 보낸다
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                  //내가 작성한 답글을 가져온다
                  recomment = editText.getText().toString();
                  //Log.d(TAG,"내가 작성한 답글:"+recomment);

                  //답글을 작성한 시간
                  long curTime = System.currentTimeMillis();
                  recommenttime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                  // Log.d(TAG,"댓글 작성한 시간:"+recommenttime);

                   RequestBody recommenttext = RequestBody.create(MediaType.parse("text/plain"),recomment);
                   RequestBody commentsnoo = RequestBody.create(MediaType.parse("text/plain"),commentsno);
                   RequestBody recommenttimes = RequestBody.create(MediaType.parse("text/plain"),recommenttime);

                   HashMap<String,RequestBody> recommentdata = new HashMap<>();
                   recommentdata.put("recommenttext",recommenttext);
                   recommentdata.put("commentsno",commentsnoo);
                   recommentdata.put("recommenttime",recommenttimes);

                  retrofitService = ourInstance.getInstance(BASE_URL,true).create(RetrofitService.class);
                  Call<CommenttextDATA> call = retrofitService.postData5(recommentdata);

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
}
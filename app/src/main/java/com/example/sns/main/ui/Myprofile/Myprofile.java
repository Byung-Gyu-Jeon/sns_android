package com.example.sns.main.ui.Myprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.os.Bundle;
import android.widget.TextView;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import org.json.JSONObject;

public class Myprofile extends AppCompatActivity {


    private MyprofileAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        //이름,자기소개 텍스트뷰
        TextView text = (TextView) findViewById(R.id.textView38);
        TextView text1 = (TextView) findViewById(R.id.textView39);
        //이미지 뷰

        //스프링으로부터 데이터 받아오기 유저이름,유저 소개 텍스트
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl("http://192.168.0.2:8080/Test/post/").addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = retrofit.create(RetrofitService.class);

        MyProfileDTO dto = new MyProfileDTO();
        Call<MyProfileDTO> call = service.getGets("1");
        call.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO>call, Response<MyProfileDTO> response) {
                String username = null;
                String userIntroduce= null;
                JSONObject object = new JSONObject();

                MyProfileDTO dto =  response.body();
                username = dto.getUsername();
                userIntroduce = dto.getUserIntroduce();
                text.setText(username);
                text1.setText(userIntroduce);

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {

            }
        });

        //Glide.with(this).load("http://192.168.0.2:8080/Test/download?filename=car.jpg").into(image);

        //리사이클러뷰 코드
        RecyclerView recyclerView = findViewById(R.id.recycler2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyprofileAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
}
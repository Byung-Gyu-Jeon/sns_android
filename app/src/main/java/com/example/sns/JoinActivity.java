package com.example.sns;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.sns.Model.User;
import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "main JoinActivity";
    private static final String BASE_URL = "http://192.168.56.1:80/sns/join.do/";	// 기본 Base URL
    private User user;

    private ApiClient apiClient;
    private RetrofitService retrofitService;

    EditText userId, userPassword, userName;
    Button btnJoin, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        apiClient = null;
        retrofitService = apiClient.getInstance(BASE_URL, false).create(RetrofitService.class);

        userId = (EditText) findViewById(R.id.etId);
        userPassword = (EditText) findViewById(R.id.etPasswd);
        userName = findViewById(R.id.etName);
        btnJoin = findViewById(R.id.btnJoin);
        btnCancel = findViewById(R.id.btnCancel);

        //가입 버튼
//        btnJoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (userId.getText().toString().length() != 0 &&
//                        userPassword.getText().toString().length() != 0 &&
//                        userName.getText().toString().length() != 0) {  //아이디와 비밀번호와 이름의 길이가 0이 아니면
//                    Gson gson = new Gson();
//                    user = new User();
//
//                    String id = userId.getText().toString();
//                    String password = userPassword.getText().toString();
//                    String name = userName.getText().toString();
//                    byte signType = 1;
//
//                    user.setUserId(id);
//                    user.setUserPassword(password);
//                    user.setUserName(name);
//                    user.setUserSignupType(signType);
//                    String objJson = gson.toJson(user);
//                    try {
//                        String encodeStr = URLEncoder.encode(objJson,"UTF-8");
//                        Call<ResponseBody> signUp = retrofitService.goSignUp(encodeStr);
//                        signUp.enqueue(new Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                if (response.isSuccessful()) {
//                                    // 정상적으로 통신 성공
//                                    Log.d(TAG, "통신 성공");
//                                    try {
//                                        if (response.body().string().equals("1")) {
//                                            Log.d("성공", "가입 성공");
//                                            Toast.makeText(JoinActivity.this, "가입 완료", Toast.LENGTH_SHORT).show();
//                                            Intent intent = null;
//                                            intent = new Intent(JoinActivity.this, MainLoginActivity.class);
//                                            startActivity(intent);
//                                        } else {
//                                            //로그인 정보가 맞지 않으면 토스트창 띄우고 id, pw칸 지우고 id칸에 포커스
//                                            Toast.makeText(JoinActivity.this, "가입 실패", Toast.LENGTH_SHORT).show();
//                                        }
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                } else {
//                                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
//                                    Log.d("responseFail", "Status Code : " + response.code());
//                                    Log.d(TAG, response.errorBody().toString());
//                                    Log.d(TAG, call.request().body().toString());
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
//                                Log.d("onFail", "Fail msg : " + t.getMessage());
//                                Toast.makeText(JoinActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } catch (UnsupportedEncodingException e) {
//                        Log.d("인코딩","인코딩실패 ");
//                        e.printStackTrace();
//                    }
//
//                }
//                else {
//                    Toast.makeText(JoinActivity.this, "입력하지 않은 칸이 있습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        //취소 버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
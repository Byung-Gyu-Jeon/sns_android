package com.example.sns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sns.Model.SharedPreferenceManager;
import com.example.sns.Model.TokenDTO;
import com.example.sns.Model.User;
import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.main.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sns.Network.ApiClient.ourInstance;

public class MainLoginActivity extends AppCompatActivity implements View.OnClickListener {
//    App app;
    private String token;
    private String refreshToken;

    private final String TAG = getClass().getSimpleName();
    private final static String BASE_URL = "http://218.148.48.169:80/sns/login.do/";	// 기본 Base URL

    // 로그인이 성공하면 static 로그인DTO 변수에 담아서
    // 어느곳에서나 접근할 수 있게 한다
    public static User user = null;

//    private ApiClient apiClient;
    private RetrofitService retrofitService;

    private EditText userId;
    private EditText userPassword;
    private Button btnLogin;
    private Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        app = (App) getApplicationContext();
        try {
            token = App.sharedPreferenceManager.getToken();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("token","생성한 토큰이 없음");
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        checkDangerousPermissions();

        retrofitService = ourInstance.getInstance(BASE_URL, false).create(RetrofitService.class);

        userId = (EditText) findViewById(R.id.user_id);
        userPassword = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button)findViewById(R.id.button_Login);
        btnJoin = (Button) findViewById(R.id.button_Join);
        btnLogin.setOnClickListener(this);
        btnJoin.setOnClickListener(this);

    }


        @Override
        public void onClick(View view) {
        Log.d("btn","버튼 누름");
            //로그인 버튼
            if (view == btnLogin) {
                if (userId.getText().toString().length() != 0 &&
                        userPassword.getText().toString().length() != 0) {  //아이디와 비밀번호의 길이가 0이 아니면
                    Gson gson = new Gson();
                    user = new User();
                    String id = userId.getText().toString();
                    String password = userPassword.getText().toString();

                    user.setUserId(id);
                    user.setUserPassword(password);
                    String objJson = gson.toJson(user);

                    Call<User> login = retrofitService.goLoginPost(objJson);
                    login.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                // 정상적으로 통신 성공
                                Log.d(TAG,"통신 성공");
                                user = response.body();
                                Log.d("response raw", "response raw : " + response.raw());
                                try {
//                                    response.body().string() != null && response.code() == 200
                                    if(response.code() == 200) {
                                        Log.d("성공","로그인 성공");
                                        Toast.makeText(MainLoginActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                                        try {
                                            TokenDTO tokenDTO;
                                            tokenDTO = user.getTokenDTO();
                                            token = tokenDTO.getAccessToken();
                                            refreshToken = tokenDTO.getRefreshToken();
                                            user.getUserNo();
                                            user.getUserRole();
                                            Log.d("전달받은값",token + " , " + user.getUserRole() + " , " + user.getUserNo());
                                            App.sharedPreferenceManager.setToken(token);
//                                            App.sharedPreferenceManager.setAccessTokenExpiresIn(tokenDTO.getAccessTokenExpiresIn());
                                            App.sharedPreferenceManager.setRefreshToken(refreshToken);
//                                            App.sharedPreferenceManager.setRefreshTokenExpiresIn(tokenDTO.getRefreshTokenExpireIn());
                                            try {
                                                token = App.sharedPreferenceManager.getToken();
                                                Log.d("token","pref에 저장된 토큰 값 : " + token);
//                                                Log.d("token","pref에 저장된 만료기간토큰 값 : " + App.sharedPreferenceManager.getAccessTokenExpiresIn());
//                                                Log.d("token","pref에 저장된 만료기간토큰(refresh) 값 : " + App.sharedPreferenceManager.getRefreshTokenExpiresIn());
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                                Log.d("token","토큰 불러오기 실패");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.d("json 변환 후 값","실패");
                                        }
                                        Intent intent = null;
                                        intent = new Intent(MainLoginActivity.this, MainActivity.class);
                                        intent.putExtra("token",token);
                                        startActivity(intent);
                                    } else {
                                        //로그인 정보가 맞지 않으면 토스트창 띄우고 id, pw칸 지우고 id칸에 포커스
                                        Toast.makeText(MainLoginActivity.this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                        userId.setText("");
                                        userPassword.setText("");
                                        userId.requestFocus();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                                Log.d("responseFail","Status Code : " + response.code());
                                Log.d(TAG,response.errorBody().toString());
                                Log.d(TAG,call.request().body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                            Log.d("onFail","Fail msg : " + t.getMessage());
                            Toast.makeText(MainLoginActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(MainLoginActivity.this, "아이디 또는 비밀번호를 입력 해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            //회원 가입 버튼
            else if(view == btnJoin) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        }


    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d("permssion","권한 있음");
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/
}



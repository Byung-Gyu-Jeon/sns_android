package com.example.sns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.sns.Model.TokenDTO;
import com.example.sns.Model.TokenRequestDTO;
import com.example.sns.Network.RetrofitService;
import com.example.sns.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sns.Network.ApiClient.ourInstance;

public class AppLogoActivity extends Activity {
    private RetrofitService retrofitService;
    private TokenRequestDTO tokenRequestDTO = null;
    private TokenDTO tokenDTO = null;

    private final String TAG = getClass().getSimpleName();
    private final static String BASE_URL = "http://59.13.221.12:80/sns/autologin.do/";	// 기본 Base URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //해당 액티비티만 액션바 제거
        setContentView(R.layout.activity_app_logo);

        if(App.sharedPreferenceManager.getRefreshToken() == null) {
            Intent intent;
            intent = new Intent(AppLogoActivity.this, LoginActivity.class);
            startActivity(intent);   // 로그인 화면으로...
        } else if(App.sharedPreferenceManager.getToken() != null) { //access, refresh 토큰 둘다 가지고 있으면..
            // autologin 호출
            retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);
            tokenRequestDTO = new TokenRequestDTO();
            tokenDTO = new TokenDTO();

            Call<TokenRequestDTO> goAutoLoginPost = retrofitService.goAutoLoginPost();
            goAutoLoginPost.enqueue(new Callback<TokenRequestDTO>() {
                @Override
                public void onResponse(Call<TokenRequestDTO> call, Response<TokenRequestDTO> response) {
                    if(response.isSuccessful()) {
                        tokenRequestDTO = response.body();
                        tokenDTO = tokenRequestDTO.getTokenDTO();
                        Log.d("codeStatus","" + tokenDTO.getCode() );
                        if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                            App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                            App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                            App.sharedPreferenceManager.setUserNo(tokenRequestDTO.getUserNo());

                            Intent intent;
                            intent = new Intent(AppLogoActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (tokenDTO.getCode() == 5100){
                            Log.d("responseFail","failed to reissue.." );
                            Intent intent;
                            intent = new Intent(AppLogoActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                        Log.d("responseFail","Status Code : " + response.code());
                        Log.d(TAG,response.errorBody().toString());
                        Log.d(TAG,call.request().body().toString());
                    }

                }

                @Override
                public void onFailure(Call<TokenRequestDTO> call, Throwable t) {
                    // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                    Log.d("onFail","Fail msg : " + t.getMessage());
                    Log.d("onFail","Fail msg : 인터넷 연결이 원활하지 않습니다." );
                    Toast.makeText(AppLogoActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
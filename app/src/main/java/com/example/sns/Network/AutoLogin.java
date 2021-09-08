package com.example.sns.Network;

import android.util.Log;

import com.example.sns.App;
import com.example.sns.Model.TokenDTO;
import com.example.sns.Model.TokenRequestDTO;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sns.Network.ApiClient.ourInstance;

public class AutoLogin {
    private final String TAG = getClass().getSimpleName();

    public static AutoLogin autoLoginInstance = null;
    private RetrofitService retrofitService;
    private TokenRequestDTO tokenRequestDTO = null;
    private TokenDTO tokenDTO = null;
    private boolean result = false;

    private final static String BASE_URL = "http://218.148.48.169:80/sns/autologin.do/";	// 기본 Base URL

    public static AutoLogin getInstance() {
        // AutoLogin 존재 확인, 없다면 AutoLogin 생성자 호출
        if (autoLoginInstance == null) {
            autoLoginInstance = new AutoLogin();
        }
        // AutoLogin 객체 반환, private 접근한정자로 설정되어서 getInstance() 메서드로만 접근가능
        return autoLoginInstance;
    }

    public void timing() {
            int delay = 1000; // number of milliseconds to sleep

            long start = System.currentTimeMillis();
            while(start >= System.currentTimeMillis() - delay); // do nothing

            Log.d("codeStatus","Time Slept: " + Long.toString(System.currentTimeMillis() - start));
    }

    public boolean autoLogin() {
        if (App.sharedPreferenceManager.getRefreshTokenExpiresIn() > 0) {
            long remain = App.sharedPreferenceManager.getRefreshTokenExpiresIn() - System.currentTimeMillis();
            long accessTokenExpriesIn = App.sharedPreferenceManager.getAccessTokenExpiresIn();
            long refreshTokenExpriesIn = App.sharedPreferenceManager.getRefreshTokenExpiresIn();

            if(remain <= 0 ) { // refresh token 만료ㅇ
                return result;   // 로그인 화면으로...
            } else { // refresh token 만료x
                // autologin 호출
                retrofitService = ourInstance.getInstance(BASE_URL, false).create(RetrofitService.class);
                Gson gson = new Gson();
                tokenRequestDTO = new TokenRequestDTO();
                tokenDTO = new TokenDTO();
                tokenRequestDTO.setAccessToken(App.sharedPreferenceManager.getToken());
                tokenDTO.setAccessTokenExpiresIn(accessTokenExpriesIn);
                tokenRequestDTO.setRefreshToken(App.sharedPreferenceManager.getRefreshToken());
                tokenDTO.setRefreshTokenExpireIn(refreshTokenExpriesIn);
                tokenRequestDTO.setTokenDTO(tokenDTO);

                String objJson = gson.toJson(tokenRequestDTO);
                Call<TokenRequestDTO> goAutoLoginPost = retrofitService.goReissuePost(objJson);
                goAutoLoginPost.enqueue(new Callback<TokenRequestDTO>() {
                    @Override
                    public void onResponse(Call<TokenRequestDTO> call, Response<TokenRequestDTO> response) {
                        if(response.isSuccessful()) {
                            tokenRequestDTO = response.body();
                            tokenDTO = tokenRequestDTO.getTokenDTO();
                            Log.d("codeStatus","" + tokenDTO.getCode() );
                            if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                                App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                App.sharedPreferenceManager.setAccessTokenExpiresIn(tokenDTO.getAccessTokenExpiresIn());
                                if ( tokenDTO.getRefreshToken() != null) {
                                    App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                                    App.sharedPreferenceManager.setRefreshTokenExpiresIn(tokenDTO.getRefreshTokenExpireIn());
                                }
                                result = true;
                            } else if (tokenDTO.getCode() == 5100){
                                Log.d("responseFail","failed to reissue.." );
                                result = false;
                            } else if( tokenDTO.getCode() == 4800) { //인증 성공(토큰 갱신 x)
                                result = true;
                                Log.d("codeStatus","할당된 result 값 : " + result );
                            }
                        } else {
                            // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                            Log.d("responseFail","Status Code : " + response.code());
                            Log.d(TAG,response.errorBody().toString());
                            Log.d(TAG,call.request().body().toString());
                            result = false;
                        }

                    }

                    @Override
                    public void onFailure(Call<TokenRequestDTO> call, Throwable t) {
                        // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                        Log.d("onFail","Fail msg : " + t.getMessage());
                        Log.d("onFail","Fail msg : 인터넷 연결이 원활하지 않습니다." );
                        result = false;
                    }
                });
            }
        } else { // refresh token 이 없음..
            result = false;   // 로그인 화면으로...
        }
//        timing();
        Log.d("codeStatus","return 전 result 값 : " + result );
        return result;
    }
}

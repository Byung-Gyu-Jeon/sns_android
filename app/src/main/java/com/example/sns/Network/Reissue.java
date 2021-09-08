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

public class Reissue {
    private final String TAG = getClass().getSimpleName();

    private RetrofitService retrofitService;
    public static Reissue reissueInstance = null;
    private TokenRequestDTO tokenRequestDTO = null;
    private TokenDTO tokenDTO = null;

    private final static String BASE_URL = "http://218.148.48.169:80/sns/reissue.do/";	// 기본 Base URL

    // Reissue 객체 반환 메서드, 전역함수 설정 (public static)
    public static Reissue getInstance() {
        // Reissue 존재 확인, 없다면 Reissue 생성자 호출
        if (reissueInstance == null) {
            reissueInstance = new Reissue();
        }
        // Reissue 객체 반환, private 접근한정자로 설정되어서 getInstance() 메서드로만 접근가능
        return reissueInstance;
    }

    public boolean isRefresh(Long exp, int refreshRangeMillis) {
        if (exp > 0) {
            long remain = exp - System.currentTimeMillis();
            return remain <= refreshRangeMillis;
        }
        return false;
    }

    public void reIssueToken() {
        // access token 만료 3분전
        if (reissueInstance.isRefresh(App.sharedPreferenceManager.getAccessTokenExpiresIn(), 6000 * 3)) {
            if (reissueInstance.isRefresh(App.sharedPreferenceManager.getRefreshTokenExpiresIn(), 6000 * 3)) { //refresh token도 만료일 경우..
                retrofitService = ourInstance.getInstance(BASE_URL, false).create(RetrofitService.class);
                Gson gson = new Gson();
                tokenRequestDTO = new TokenRequestDTO();
                tokenDTO = new TokenDTO();
                tokenRequestDTO.setAccessToken(App.sharedPreferenceManager.getToken());
                tokenRequestDTO.setRefreshToken(App.sharedPreferenceManager.getRefreshToken());
                tokenRequestDTO.setType(1); //refresh 토큰도 갱신

                String objJson = gson.toJson(tokenRequestDTO);
                Call<TokenRequestDTO> reissuePost = retrofitService.goReissuePost(objJson);
                reissuePost.enqueue(new Callback<TokenRequestDTO>() {
                    @Override
                    public void onResponse(Call<TokenRequestDTO> call, Response<TokenRequestDTO> response) {
                        if(response.isSuccessful()) {
                            tokenRequestDTO = response.body();
                            tokenDTO = tokenRequestDTO.getTokenDTO();
                            Log.d("codeStatus","reissue : " + tokenDTO.getCode() );
                            if(tokenDTO.getCode() == 4700) {
                                App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                App.sharedPreferenceManager.setAccessTokenExpiresIn(tokenDTO.getAccessTokenExpiresIn());
                                if ( tokenRequestDTO.getType() == 1) {
                                    App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                                    App.sharedPreferenceManager.setRefreshTokenExpiresIn(tokenDTO.getRefreshTokenExpireIn());
                                }
                            } else if (tokenDTO.getCode() == 5100){
                                Log.d("responseFail","failed to reissue.." );
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
                    }
                });
            } else {
                retrofitService = ourInstance.getInstance(BASE_URL, false).create(RetrofitService.class);
                Gson gson = new Gson();
                tokenRequestDTO = new TokenRequestDTO();
                tokenDTO = new TokenDTO();
                tokenRequestDTO.setAccessToken(App.sharedPreferenceManager.getToken());
                tokenRequestDTO.setRefreshToken(App.sharedPreferenceManager.getRefreshToken());
                tokenRequestDTO.setType(0); // access 토큰만 갱신

                String objJson = gson.toJson(tokenRequestDTO);
                Call<TokenRequestDTO> reissuePost = retrofitService.goReissuePost(objJson);
                reissuePost.enqueue(new Callback<TokenRequestDTO>() {
                    @Override
                    public void onResponse(Call<TokenRequestDTO> call, Response<TokenRequestDTO> response) {
                        if(response.isSuccessful()) {
                            tokenRequestDTO = response.body();
                            tokenDTO = tokenRequestDTO.getTokenDTO();
                            Log.d("codeStatus","reissue : " + tokenDTO.getCode() );
                            if(tokenDTO.getCode() == 4700) {
                                App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                App.sharedPreferenceManager.setAccessTokenExpiresIn(tokenDTO.getAccessTokenExpiresIn());
                            } else if (tokenDTO.getCode() == 5100){
                                Log.d("responseFail","failed to reissue.." );
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
                    }
                });

            }
        }
//        if(tokenDTO.getCode() == 4700)
//            return true;
//        return false;
    }
}

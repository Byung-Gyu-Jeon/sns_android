package com.example.sns.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.sns.App;
import com.example.sns.MainLoginActivity;
import com.example.sns.Model.RealmUser;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.Model.User;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;
import static io.realm.Realm.getApplicationContext;

public class LoginViewModel extends ViewModel {
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";

    public MutableLiveData<String> eMail = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> passWord = new MutableLiveData<>();

    public MutableLiveData<Boolean> eMailButtonEnable = new MutableLiveData<>();
    public MutableLiveData<Boolean> passWordButtonEnable = new MutableLiveData<>();
    public MutableLiveData<Boolean> nameButtonEnable = new MutableLiveData<>();

    private final static String BASE_URL_LOGIN = "http://59.13.221.12:80/sns/login.do/";	// 기본 Base URL
    private static final String BASE_URL_JOIN = "http://59.13.221.12:80/sns/singup.do/";	// 기본 Base URL
    private RetrofitService retrofitService;
    private RequestResponse requestResponse;
    private String fcmToken;
    private String refreshToken;
    private String accessToken;

    User user;

    public void SignUP(View view) {
        Log.d(TAG, "SignUP: Sign up 실행");

        retrofitService = ourInstance.getInstance(BASE_URL_JOIN, false).create(RetrofitService.class);

        Gson gson = new Gson();
        User user = new User();

        String id = eMail.getValue();
        String password = passWord.getValue();
        String userName = name.getValue();
        byte signType = 1;

        user.setUserId(id);
        user.setUserPassword(password);
        user.setUserName(userName);
        user.setUserSignupType(signType);
        String objJson = gson.toJson(user);

        Call<RequestResponse> signUp = retrofitService.goSignUp(objJson);
        signUp.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful()) {
                    // 정상적으로 통신 성공
                    requestResponse = response.body();
                    Log.d(TAG, "통신 성공");
                    Log.d(TAG, "response code : " + requestResponse.getCode());
                    if (requestResponse.getCode() == 4700) {
                        Log.d("성공", "가입 성공");
                        Toast.makeText(view.getContext(), "가입 완료", Toast.LENGTH_SHORT).show();

                        eMail.setValue("");
                        name.setValue("");
                        passWord.setValue("");
                        Navigation.findNavController(view).navigate(R.id.action_from_name_to_login);
                    } else {
                        //로그인 정보가 맞지 않으면 토스트창 띄우고 id, pw칸 지우고 id칸에 포커스
                        Toast.makeText(view.getContext(), "가입 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                    Log.d("responseFail", "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                    Log.d(TAG, call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                Log.d("onFail", "Fail msg : " + t.getMessage());
                Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Login(View view) {
        Log.d(TAG, "Login 실행");

        retrofitService = ourInstance.getInstance(BASE_URL_LOGIN, false).create(RetrofitService.class);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    fcmToken = task.getResult();
                });

        Gson gson = new Gson();
        user = new User();
        RealmUser realmUser = new RealmUser();

        String id = eMail.getValue();
        String password = passWord.getValue();

        user.setUserId(id);
        user.setUserPassword(password);
        String objJson = gson.toJson(user);

        // Log and toast
        Call<User> login = retrofitService.goLoginPost(objJson, fcmToken);
        login.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    // 정상적으로 통신 성공
                    Log.d(TAG,"통신 성공");
                    user = response.body();
                    Log.d("response raw", "response raw : " + response.raw());
                    if(response.code() == 200) {
                        Log.d("성공","로그인 성공");
                        Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_SHORT).show();
                        try {
                            TokenDTO tokenDTO;
                            tokenDTO = user.getTokenDTO();
                            accessToken = tokenDTO.getAccessToken();
                            refreshToken = tokenDTO.getRefreshToken();
                            user.getUserNo();
                            user.getUserRole();

                            realmUser.setUserId(user.getUserId()); // 10/24 서버에서 값을 보내주지 않기때문에 null값임
                            realmUser.setUserImageUrl(user.getUserImageUrl());
                            realmUser.setUserIntroduction(user.getUserIntroduction());
                            realmUser.setUserName(user.getUserName());
                            realmUser.setUserNo(user.getUserNo());

                            Log.d("전달받은값", accessToken + " , " + user.getUserRole() + " , " + user.getUserNo());
                            App.sharedPreferenceManager.setToken(accessToken);
//                                           App.sharedPreferenceManager.setAccessTokenExpiresIn(tokenDTO.getAccessTokenExpiresIn());
                            App.sharedPreferenceManager.setRefreshToken(refreshToken);
//                                           App.sharedPreferenceManager.setRefreshTokenExpiresIn(tokenDTO.getRefreshTokenExpireIn());
                            App.sharedPreferenceManager.setUserNo(user.getUserNo());

                            if (App.sharedPreferenceManager.getFcmToken() == null)
                                App.sharedPreferenceManager.setFcmToken(fcmToken);

                            RealmUser loginUser = App.realm.where(RealmUser.class).findFirst();
                            if(loginUser == null) {
                                App.realm.executeTransactionAsync(transactionRealm -> {
                                    transactionRealm.insert(realmUser);
                                });
                            }
//                                            App.realm.beginTransaction();
//                                            RealmUser manageUser = App.realm.copyToRealm(realmUser);
//                                            App.realm.commitTransaction();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("json 변환 후 값","실패");
                        }
                        Intent intent = null;
                        intent = new Intent(view.getContext(), MainActivity.class);
                        intent.putExtra("token", accessToken);
                        view.getContext().startActivity(intent);
                    } else {
                        //로그인 정보가 맞지 않으면 토스트창 띄우고 id, pw칸 지우고 id칸에 포커스
                        Toast.makeText(view.getContext(), "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enableButton(boolean enable, String type) {
        switch (type) {
            case EMAIL :
                eMailButtonEnable.setValue(enable);
                break;
            case PASSWORD :
                passWordButtonEnable.setValue(enable);
                break;
            case NAME :
                nameButtonEnable.setValue(enable);
                break;
        }
    }

}

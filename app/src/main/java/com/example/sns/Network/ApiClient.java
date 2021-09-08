package com.example.sns.Network;

import com.example.sns.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static ApiClient ourInstance = null;
    private static Retrofit retrofit = null;	// private 접근한정자로 외부에서 직접 접근 방지
    private static String baseUrl;
    private static boolean type = true;

    // ApiClient 생성자
    private ApiClient(String BASE_URL, boolean type) {
        // ApiClient 타입의 ourInstance 존재 확인
        if (ourInstance == null || !ourInstance.getBaseUrl().equals(BASE_URL)) {
            // Null이라면 Retrofit 객체 생성
            if (type) {//true
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getClient())    // REST 요청 로그확인 위해 HttpLoggingInterceptor 등록
                        .build();
                baseUrl = BASE_URL;
            } else {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                baseUrl = BASE_URL;
            }
        }
    }

    // Retrofit 객체 반환 메서드, 전역함수 설정 (public static)
    public static Retrofit getInstance(String BASE_URL, boolean type) {
        // ourInstance 존재 확인, 없다면 ApiClient 생성자 호출, 토큰을 보낼 필요가 없을 경우 type false, default true
        if (ourInstance == null || !ourInstance.getBaseUrl().equals(BASE_URL) || (ourInstance.isType() ^ type)) {
            ourInstance = new ApiClient(BASE_URL, type);
        }
        // Retrofit 객체 반환, private 접근한정자로 설정되어서 getInstance() 메서드로만 접근가능
        return retrofit;
    }

    public Response headerInterceptor(Interceptor.Chain chain) throws IOException {
        String accessToken = App.sharedPreferenceManager.getToken();
        String refreshToken = App.sharedPreferenceManager.getRefreshToken();
        String accessHeader = "Bearer " + accessToken;
        String refreshHeader = "Bearer " + refreshToken;
        Request newRequest;
        newRequest = chain.request().newBuilder()
                .addHeader("Authorization", accessHeader + ", " + refreshHeader)
                .build();
        return chain.proceed(newRequest);
    }

    // REST API 요청 로그확인을 위해 LoggingInterceptor 생성
    public HttpLoggingInterceptor getIntercepter() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
                .setLevel(HttpLoggingInterceptor.Level.HEADERS);

        return interceptor;
    }


    public OkHttpClient getClient() {
//        OkHttpClient builder = new OkHttpClient.Builder()
//                .addInterceptor(getIntercepter())	// LoggingInterceptor 등록
//                .build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(this::headerInterceptor);
        builder.interceptors().add(getIntercepter());

        OkHttpClient client = builder.build();

        return client;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static boolean isType() { return type; }
}

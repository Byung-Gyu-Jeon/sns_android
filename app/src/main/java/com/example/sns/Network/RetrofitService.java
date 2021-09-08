package com.example.sns.Network;

import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenRequestDTO;
import com.example.sns.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    // @GET( EndPoint-자원위치(URI) )
    @GET("posts/{post}")
    Call<User> getPosts(@Path("post") String post);

    /*  POST 방식 - @Body
     *   @Body 사용, Java Object 전송 - Gson컨버터가 직렬화 담당
     *
     *   전체 URI
     *       : https://jsonplaceholder.typicode.com/posts
     *   posts에 새로운 데이터 추가하는 메서드
     */

    @GET("gets")
    Call<RequestResponse> getFriendRequestList();

    @GET("gets")
    Call<RequestResponse> getSearchList(@Query("searchText") String searchText);

    @FormUrlEncoded
    @POST("posts")
    Call<RequestResponse> actionRequest(@Field("requestedUserNo") Long requestedUserNo, @Field("type") int type);

    @POST("posts")
    Call<User> setPostBody(@Body User post);

    @FormUrlEncoded
    @POST("posts")
    Call<User> goLoginPost(@Field("objJson") String objJson);

    @FormUrlEncoded
    @POST("posts")
    Call<TokenRequestDTO> goReissuePost(@Field("objJson") String objJson);

    @POST("posts")
    Call<TokenRequestDTO> goAutoLoginPost();

    @FormUrlEncoded
    @POST("posts")
    Call<ResponseBody> goPost(@Field("objJson") String objJson);
}

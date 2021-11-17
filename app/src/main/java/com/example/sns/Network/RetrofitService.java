package com.example.sns.Network;

import com.example.sns.Model.ChatMsgDTO;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenRequestDTO;
import com.example.sns.Model.User;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.example.sns.main.ui.feed.MyFeedaddDATA;

import java.util.List;


import io.reactivex.Completable;
import io.reactivex.Flowable;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Call<User> goLoginPost(@Field("objJson") String objJson, @Field("fcmToken") String fcmToken);

    @FormUrlEncoded
    @POST("posts")
    Call<TokenRequestDTO> goReissuePost(@Field("objJson") String objJson);

    @POST("posts")
    Call<TokenRequestDTO> goAutoLoginPost();

    @FormUrlEncoded
    @POST("posts")

    Call<RequestResponse> goSignUp(@Field("objJson") String objJson);

    //9/29 임시 테스트 추가
//    @POST("hello-convert-and-send")
//    Completable sendRestEcho(@Query("msg") String message, @Query("roomId") String roomId);
    @FormUrlEncoded
    @POST("hello-convert-and-send")
    Completable sendRestEcho(@Field("objJson") String objJson);


    @FormUrlEncoded
    @POST("posts")
    Call<RequestResponse> setChatRoom(@Field("objJson") String objJson);

    @FormUrlEncoded
    @POST("posts")
    Call<RequestResponse> sendFcmTokenToServer(@Field("token") String token, @Field("userNo") Long userNo);

    Call<ResponseBody> goPost(@Field("objJson") String objJson);

    // 내가 새롭게 추가한 코드(병)
    // 데이터 수정화면에서 내가 저장한 이름,소개글 가져오기
    @GET("gets/{post}")
    Call<MyProfileDTO> getGets(@Path("post") String number);

    //데이터 수정화면에서 내가 바꾼 이름,소개글을 spring으로 전송
    @FormUrlEncoded
    @POST("post")
    Call<MyProfileDTO> postData(@Field("objJson") String objJson);

    //데이터 수정화면에서 선택한 이미지와 이름,자기소개등을 한번에 모아서 spring으로 보낸다 (내 프로필 수정화면에서 사용)
    @Multipart
    @POST("post")
    Call<MyProfileDTO> postData2(@PartMap Map<String,RequestBody> texfile,@Part MultipartBody.Part imfile);

    //게시글 추가 화면에서 사용할 레트로핏
    @Multipart
    @POST("post2")
    Call<MyFeedaddDATA> postData3(@PartMap Map<String,RequestBody> feedtext,@Part List<MultipartBody.Part> feedptos);

    //내 프로필 화면에서 서버에서 사진 이름받기 위한 코드
    @FormUrlEncoded
    @POST("gets2")
    Call<MyProfileDTO> getData(@Field("num") String num);

    //메인 화면에서 서버에서 1.feed_no , 2. 이미지 이름들 3. feed comment , 4. 내 이름, 5. 내 프로필 사진 가져오기 위한 코드
    @FormUrlEncoded
    @POST("gets")
    Call<MyProfileDTO> getData2(@Field("num") String num);

}

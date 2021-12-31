package com.example.sns.Network;

import com.example.sns.Model.ChatMsgDTO;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenRequestDTO;
import com.example.sns.Model.User;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.example.sns.main.ui.feed.CommenttextDATA;
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

    //댓글 화면에서 댓글 작성시 1.댓글의 내용을 서버로 보내기 위한 코드 2.댓글이 작성된 시간
    @Multipart
    @POST("post3")
    Call<CommenttextDATA> postData4(@PartMap Map<String,RequestBody> requestMapp);

    //댓글화면에서 필요한 1.프로필 사진이름 2. 내 이름 3.댓글 내용 4.댓글 작성 시간을 가져온다
    @FormUrlEncoded
    @POST("gets")
    Call<MyProfileDTO> getData3(@Field("num") String num);

    //답글 화면에서 내가 작성한 답글을 서버에 보내기 위해서 사용 1. 댓글 내용, 2.comments_no, 3.답글 작성한 시각
    @Multipart
    @POST("post4")
    Call<CommenttextDATA> postData5(@PartMap Map<String,RequestBody> recommentdata);

    //답글화면에서 필요한 1.프로필사진 2. 내 이름 3. 답글 내용과 4. 답글 작성시간을 가져온다
    @FormUrlEncoded
    @POST("gets")
    Call<MyProfileDTO> getData4(@Field("commentsno") String commentsno);

    //메인 화면에서 좋아요 버튼을 눌렀을때 서버로 feedno 전송
    @FormUrlEncoded
    @POST("feedlike")
    Call<MyProfileDTO> postData6(@Field("feedno") String feedno,@Field("like_btn_state") String like_btn_State);

}

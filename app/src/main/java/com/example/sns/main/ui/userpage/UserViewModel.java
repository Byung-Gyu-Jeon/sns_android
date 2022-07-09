package com.example.sns.main.ui.userpage;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns.App;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.FeedimagelistDATA;
import com.example.sns.main.ui.Myprofile.MyPageFragmentAdapter;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class UserViewModel extends ViewModel {
    public static final String ACCEPT = "accept";
    public static final String CANCEL = "cancel";
    public static final String REQUEST = "request";
    public static final String FRIEND = "friend";

    public MutableLiveData<String> userImageUrl = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> introduction = new MutableLiveData<>();
    public MutableLiveData<String> numberOfFeed = new MutableLiveData<>();
    public MutableLiveData<Integer> numberOfFriend = new MutableLiveData<>();

//    public MutableLiveData<FeedimagelistDATA> feedImageData = new MutableLiveData<>();
    public MutableLiveData<List<FeedimagelistDATA>> feedImageDataList = new MutableLiveData<>();

    public MutableLiveData<Boolean> acceptButtonEnable = new MutableLiveData<>();
    public MutableLiveData<Boolean> cancelButtonEnable = new MutableLiveData<>();
    public MutableLiveData<Boolean> requestButtonEnable = new MutableLiveData<>();
    public MutableLiveData<Boolean> friendButtonEnable = new MutableLiveData<>();

    private final static String RESPONSE_FRIEND_ACTION_BASE_URL = "http://59.13.221.12:80/sns/actionFriendRequest.do/";
    private final static String FRIEND_ACTION_BASE_URL="http://59.13.221.12:80/sns/post/friend/";
    //프로필 사진이랑 이름,소개글 db에서 가져오기
    private final static String USER_INFO_BASE_URL="http://59.13.221.12:80/sns/get/";
    //게시글 추가할때 올린 사진들 가져오기
    private final static String FEED_IMAGE_INFO_BASE_URL="http://59.13.221.12:80/sns/gett/";
    public static ApiClient ourInstance = null;
    private RetrofitService retrofitService;
    private RequestResponse requestResponse;

    public void loadUserInfo(View view, RecyclerView recyclerView, Long userNo) {
        //프로필 사진이랑 자기 소개 가져오기
        retrofitService = ourInstance.getInstance(USER_INFO_BASE_URL,true).create(RetrofitService.class);
        MyProfileDTO dto = new MyProfileDTO();
        Call<MyProfileDTO> call = retrofitService.getGets(userNo);

        call.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO>call, Response<MyProfileDTO> response) {
                Gson gson = new Gson();
                if(response.isSuccessful()){
                    MyProfileDTO dto = response.body();
                    userName.setValue(dto.getUsername());
                    introduction.setValue(dto.getUserIntroduce());
                    userImageUrl.setValue(dto.getPtoname());
                    numberOfFriend.setValue(dto.getNumberOfFriend());

                    Log.d(TAG, "onResponse: 서버에서 정보 받음(code 확인 전)" + dto.toString());
                    int code = dto.getCode();
                    if(code == 4600) { // 친구관계
                        enableButton(true, FRIEND);
                    } else if(code == 4700) { //내가 친구요청 함
                        enableButton(true, CANCEL);
                    } else if(code == 4800) { // 친구 요청 받음
                        enableButton(true, ACCEPT);
                    } else { // 아무 관계 아님
                        enableButton(true, REQUEST);
                    }
                    Log.d(TAG, "onResponse: 서버에서 정보 받음(code 확인 후)" + dto.toString());
//                    view.findViewById(R.id.textView38);
//                    text.setText(username);
//                    text1.setText(userIntroduce);
                    Log.d(TAG,"사진이름:"+userImageUrl.getValue());

                    //사진 불러오기
//                    Glide.with(view.getContext()).load("http://59.13.221.12:80/sns/download?fileName=" + userImageUrl.getValue()).apply(new RequestOptions().circleCrop())
//                            .into(imageView);
                }
                else{
                    Toast.makeText(view.getContext(),"내 프로필 화면에서 서버에서 응답을 제대로 받지 못했습니다", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {

            }
        });

        //게시글 추가화면에서 올린 사진이름들 가져오기 - (gilde를 이용해서 받아온 사진이름을 뒤에 붙여서 사진을 가져온다)
        retrofitService = ourInstance.getInstance(FEED_IMAGE_INFO_BASE_URL,true).create(RetrofitService.class);

        Call<MyProfileDTO> call2 = retrofitService.getData(userNo);
        call2.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO> call, Response<MyProfileDTO> response) {


                if(response.isSuccessful()){
                    MyProfileDTO dto2 = response.body();

                    Log.d(TAG,"서버에서 보낸 데이터"+dto2.list.toString());
                    for(int i=0;i<dto2.list.size();i++){

                        if(i==0){
//                            feedImageData.getValue().setFeedno(dto2.list.get(i).getFeedno());
//                            feedImageData.getValue().setImagename(dto2.list.get(i).getImagename());

                            FeedimagelistDATA data = new FeedimagelistDATA();
                            data.setFeedno(dto2.list.get(i).getFeedno());
                            // Log.d(TAG,data.getFeedno()+"");
                            data.setImagename(dto2.list.get(i).getImagename());
                            Log.d(TAG,data.getImagename());
                            feedImageDataList.getValue().add(data);
                        }

                        else if(i>=1 && (dto2.list.get(i).getFeedno() != dto2.list.get(i-1).getFeedno())){
                            FeedimagelistDATA data = new FeedimagelistDATA();
                            data.setFeedno(dto2.list.get(i).getFeedno());
                            // Log.d(TAG,data.getFeedno()+"");
                            data.setImagename(dto2.list.get(i).getImagename());
                            // Log.d(TAG,data.getImagename());
                            feedImageDataList.getValue().add(data);

                        }
                        for(FeedimagelistDATA data: feedImageDataList.getValue()){
                            Log.d(TAG,data.getFeedno()+"");
                            Log.d(TAG,data.getImagename());
                        }

                    }
                    //게시글 갯수를 표현 해준다
                    if (feedImageDataList.getValue() == null)
                        numberOfFeed.setValue(0+"");
                    else
                        numberOfFeed.setValue(feedImageDataList.getValue().size() + "");
//                    textView2.setText(dataList.size()+"");
                    if (feedImageDataList.getValue() != null) {
                        MyPageFragmentAdapter myPageFragmentAdapter = new MyPageFragmentAdapter(feedImageDataList.getValue(), view.getContext());
                        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
                        recyclerView.setAdapter(myPageFragmentAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {
                Toast.makeText(view.getContext(),"서버로부터 응답을 받지 못하였습니다",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFriendAction(View view, Long userNo) {
        if(view == view.findViewById(R.id.accept_button)) { //수락 버튼
            int type = 1;
            retrofitService = ourInstance.getInstance(RESPONSE_FRIEND_ACTION_BASE_URL, true).create(RetrofitService.class);
            Call<RequestResponse> requestAction = retrofitService.actionResponse(userNo, type);
            requestAction.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.isSuccessful()) {
                        requestResponse = response.body();
//                        tokenDTO = requestResponse.getTokenDTO();
//                        if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
//                            App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
//                            App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
//                        }
                        if(requestResponse.getCode() == 4700) { // Action 수행 성공
                            Toast.makeText(view.getContext(), "요청 성공! 이제부터 친구입니다.", Toast.LENGTH_SHORT).show();
                            enableButton(true, FRIEND);
                            numberOfFriend.setValue(numberOfFriend.getValue() + 1);
                        } else if (requestResponse.getCode() == 4900) { // Action 수행 실패
                            Toast.makeText(view.getContext(), "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        } else if (requestResponse.getCode() == 5100) { //spring security principal 변환 실패
                            Toast.makeText(view.getContext(), "요청에 실패하였습니다.(principal)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                        Log.d("responseFail","Status Code : " + response.code());
                        Log.d(TAG,response.errorBody().toString());
                        Log.d(TAG,call.request().body().toString());
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                    Log.d("onFail","Fail msg : " + t.getMessage());
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(view == view.findViewById(R.id.friend_button)) { //거절 버튼
            Toast.makeText(view.getContext(), "친구 버튼 Click", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestfriendAction(View view, Long userNo) {
        retrofitService = ourInstance.getInstance(FRIEND_ACTION_BASE_URL,true).create(RetrofitService.class);
        if(view == view.findViewById(R.id.request_button)) { //요청 버튼
            int type = 0;
            Call<RequestResponse> requestAction = retrofitService.actionRequest(userNo, type);
            requestAction.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.isSuccessful()) {
                        requestResponse = response.body();
//                        tokenDTO = requestResponse.getTokenDTO();
//                        if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
//                            App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
//                            App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
//                        }
                        if(requestResponse.getCode() == 4700) { // Action 수행 성공
                            Toast.makeText(view.getContext(), "요청 성공!", Toast.LENGTH_SHORT).show();
                            enableButton(true, CANCEL);
                        } else if (requestResponse.getCode() == 5100) { // Action 수행 실패
                            Toast.makeText(view.getContext(), "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                        Log.d("responseFail","Status Code : " + response.code());
                        Log.d(TAG,response.errorBody().toString());
                        Log.d(TAG,call.request().body().toString());
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                    Log.d("onFail","Fail msg : " + t.getMessage());
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(view == view.findViewById(R.id.cancel_button)) { //취소 버튼
            int type = 1;
            Call<RequestResponse> requestAction = retrofitService.actionRequest(userNo, type);
            requestAction.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.isSuccessful()) {
                        requestResponse = response.body();
//                        tokenDTO = requestResponse.getTokenDTO();
//                        if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
//                            App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
//                            App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
//                        }
                        if(requestResponse.getCode() == 4700) { // Action 수행 성공
                            Toast.makeText(view.getContext(), "요청 성공! 친구 요청을 취소합니다.", Toast.LENGTH_SHORT).show();
                            enableButton(true, REQUEST);
                        } else if (requestResponse.getCode() == 5100) { // Action 수행 실패
                            Toast.makeText(view.getContext(), "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                        Log.d("responseFail","Status Code : " + response.code());
                        Log.d(TAG,response.errorBody().toString());
                        Log.d(TAG,call.request().body().toString());
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                    Log.d("onFail","Fail msg : " + t.getMessage());
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void enableButton(boolean enable, String type) {
        switch (type) {
            case ACCEPT :
                acceptButtonEnable.setValue(enable);
                break;
            case CANCEL :
                cancelButtonEnable.setValue(enable);
                break;
            case REQUEST :
                requestButtonEnable.setValue(enable);
                break;
            case FRIEND :
                friendButtonEnable.setValue(enable);
                break;
        }
    }
}

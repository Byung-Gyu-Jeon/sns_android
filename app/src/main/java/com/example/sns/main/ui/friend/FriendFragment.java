package com.example.sns.main.ui.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.userpage.UserPageActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class FriendFragment extends Fragment {

    private final static String BASE_URL = "http://59.13.221.12:80/sns/getlist.do/";	// 기본 Base URL

    private final static String BYUNG_BASE_URL = "http://192.168.0.2:8080/sns/getlist.do/";	// 기본 Base URL


    private RetrofitService retrofitService;
    private TokenDTO tokenDTO = null;

    private RequestResponse requestResponse;
    private List<RequestResponse.FriendRequestListEntity> list = new ArrayList<>();
    private List<RequestResponse.FriendRequestListEntity> friendRequestListEntity = new ArrayList<>();

    private FriendViewModel friendViewModel;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    TextView textInfo;
    ImageButton searchBtn;
    Button myFriendBtn;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static FriendFragment newInstance() {
        return new FriendFragment() ;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friend, container, false);

        textInfo = root.findViewById(R.id.text_info);
        myFriendBtn = root.findViewById(R.id.my_friend_button);
        searchBtn = (ImageButton)root.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(v -> {
            Log.d("검색 누름","onclick 실행");
            Intent intent = null;
            intent = new Intent(getContext(), FriendSearchActivity.class);
//                intent.putExtra("token",token);
            startActivity(intent);
        });

        myFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendListActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);
        tokenDTO = new TokenDTO();
        Call<RequestResponse> getList = retrofitService.getFriendRequestList();
        Log.d("친구목록 요청 base url : ",ourInstance.getBaseUrl());

        getList.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if(response.isSuccessful()){
                    // 정상적으로 통신 성공
                    Log.d(TAG,"통신 성공");

                    requestResponse = response.body();
                    list = requestResponse.getFriendRequestListEntity();
                    tokenDTO = requestResponse.getTokenDTO();

                    Log.d("response raw", "response raw : " + response.raw());
                    Log.d("message 및 code : " ,"" + requestResponse.getMessage() + " " + requestResponse.getCode());

                    if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                        App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                        App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                    }

                    Log.d(TAG, "onResponse: 응답 코드 : " + requestResponse.getCode());
                    if (requestResponse.getCode() == 4700) {
                        textInfo.setVisibility(View.GONE);
                        adapter = new RecyclerAdapter(getContext(), GlideApp.with(getContext()));
                        adapter.addItem(new item(0));

                        for (RequestResponse.FriendRequestListEntity requestList : list) {
                            adapter.addItem(new item(requestList.getUserNo(), requestList.getUserName(), requestList.getUserImageUrl(), 1));
                            Log.d("조회한 목록 ", "" + requestList.getUserName() + "  " + requestList.getUserImageUrl());
                        }

                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickEventListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final item item = adapter.getItem().get(position);
                                Long userNo = item.getUserNo();
                                Intent intent = new Intent(getContext(), UserPageActivity.class);

                                intent.putExtra("userNo", userNo);
                                startActivity(intent);
                                Toast.makeText(getContext(), item.getUserName() + " Click event", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (requestResponse.getCode() == 4900) {
                        textInfo.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                    Toast.makeText(getContext(), "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                    Log.d("responseFail","Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                Log.d("onFail","Fail msg : " + t.getMessage());
                if(getContext() != null)
                    Toast.makeText(getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
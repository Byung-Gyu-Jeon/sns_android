package com.example.sns.main.ui.friend;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns.R;
import com.example.sns.GlideApp;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Network.RetrofitService;
import com.example.sns.main.ui.userpage.UserPageActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class FriendViewModel extends ViewModel {
    public static final String SEARCH = "search";
    public static final String FRIEND = "friend";

    private MutableLiveData<String> mText;

    private Long userNo;
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> userImageUrl = new MutableLiveData<>();
    public MutableLiveData<Byte> friendStatus = new MutableLiveData<>();

    private RetrofitService retrofitService;
    private RequestResponse requestResponse;
    private List<RequestResponse.FriendListEntity> friendList = new ArrayList<>();

    private final static String FRIEND_GET_ACTION_BASE_URL ="http://59.13.221.12:80/sns/GET/friends/";
    private final static String FRIEND_DELETE_ACTION_BASE_URL="http://59.13.221.12:80/sns/DELETE/friends/";

    public MutableLiveData<Boolean> friendListRecyclerViewEnable = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchListRecyclerViewEnable = new MutableLiveData<>();

    public FriendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }

    public void loadFriendList(View view, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        enableView(true, FRIEND);
        retrofitService = ourInstance.getInstance(FRIEND_GET_ACTION_BASE_URL,true).create(RetrofitService.class);
        Call<RequestResponse> call = retrofitService.getFriendList();

        call.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if(response.isSuccessful()) {
                    requestResponse = response.body();
                    int numberOfFriend = requestResponse.getNumberOfFriend();
                    friendList = requestResponse.getFriendList();
                    if (requestResponse.getCode() == 4900) { // 결과 없음 혹은 오류
                        Log.d(TAG, "onResponse: 검색결과 없음 혹은 오류");
                        view.findViewById(R.id.text_info).setVisibility(View.VISIBLE);
                    } else if(requestResponse.getCode() == 4700) { //조회 성공
                        view.findViewById(R.id.text_info).setVisibility(View.GONE);
                        FriendAdapter adapter = new FriendAdapter(GlideApp.with(view.getContext()) ,view.getContext());
                        adapter.addItem(new item(numberOfFriend, 0));
                        for(RequestResponse.FriendListEntity entity : friendList) {
                            adapter.addItem(new item(entity.getUserNo(), entity.getUserName(), entity.getUserImageUrl(), entity.getFriendStatus(),1));
                            Log.d( TAG ,""+ entity.getUserName() + "  " + entity.getUserImageUrl());
                        }

                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickEventListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final item item = adapter.getItem().get(position);
                                Long userNo = item.getUserNo();
                                Intent intent = new Intent(view.getContext(), UserPageActivity.class);

                                intent.putExtra("userNo", userNo);
                                view.getContext().startActivity(intent);
                                Toast.makeText(view.getContext(), item.getUserName() + " Click event", Toast.LENGTH_SHORT).show();
                            }
                        });

                        adapter.setOnMenuItemClickListener(new FriendAdapter.OnMenuItemClickEventListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                final item item = adapter.getItem().get(position);
                                Long userNo = item.getUserNo();
                                String userName = item.getUserName();
                                Log.d(TAG, "onMenuItemClick:" + userName +" unfriend clicked : " + userNo);
                                deleteFriend(view, adapter, position, userNo);
                            }
                        });
                    }
                }else {
                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                    Log.d(TAG, "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                Log.d(TAG, "Fail msg : " + t.getMessage());
                Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void search(View view, RecyclerView recyclerView, String searchText) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        enableView(true, SEARCH);
        retrofitService = ourInstance.getInstance(FRIEND_GET_ACTION_BASE_URL,true).create(RetrofitService.class);
        Call<RequestResponse> call = retrofitService.getSearchFriendList(searchText);

        call.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if(response.isSuccessful()) {
                    requestResponse = response.body();
                    friendList = requestResponse.getFriendList();
                    if (requestResponse.getCode() == 4900) { // 결과 없음 혹은 오류
                        Log.d(TAG, "onResponse: 검색결과 없음 혹은 오류");
                        recyclerView.setVisibility(View.GONE);
                        view.findViewById(R.id.text_info).setVisibility(View.VISIBLE);
                    } else if(requestResponse.getCode() == 4700) { //조회 성공
                        recyclerView.setVisibility(View.VISIBLE);
                        view.findViewById(R.id.text_info).setVisibility(View.GONE);
                        FriendAdapter adapter = new FriendAdapter(GlideApp.with(view.getContext()), view.getContext());
                        for (RequestResponse.FriendListEntity entity : friendList) {
                            adapter.addItem(new item(entity.getUserNo(), entity.getUserName(), entity.getUserImageUrl(), entity.getFriendStatus(), 1));
                            Log.d(TAG, "" + entity.getUserName() + "  " + entity.getUserImageUrl());
                        }
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickEventListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final item item = adapter.getItem().get(position);
                                Long userNo = item.getUserNo();
                                Intent intent = new Intent(view.getContext(), UserPageActivity.class);

                                intent.putExtra("userNo", userNo);
                                view.getContext().startActivity(intent);
                                Toast.makeText(view.getContext(), item.getUserName() + " Click event", Toast.LENGTH_SHORT).show();
                            }
                        });

                        adapter.setOnMenuItemClickListener(new FriendAdapter.OnMenuItemClickEventListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                final item item = adapter.getItem().get(position);
                                Long userNo = item.getUserNo();
                                String userName = item.getUserName();
                                Log.d(TAG, "onMenuItemClick:" + userName +" unfriend clicked : " + userNo);
                                deleteFriend(view, adapter, position, userNo);
                            }
                        });
                    }
                }else {
                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                    Log.d(TAG, "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                Log.d(TAG, "Fail msg : " + t.getMessage());
                Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFriend(View view, FriendAdapter adapter, int position, Long userNo) {
        retrofitService = ourInstance.getInstance(FRIEND_DELETE_ACTION_BASE_URL,true).create(RetrofitService.class);
        Call<RequestResponse> call = retrofitService.deleteFriend(userNo);

        call.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if(response.isSuccessful()) {
                    requestResponse = response.body();
                    if (requestResponse.getCode() == 4900) { // 요청 실패
                        Log.d(TAG, "onResponse: 요청 실패");
                        Toast.makeText(view.getContext(), "요청 실패! 나중에 다시 시도하세요", Toast.LENGTH_LONG).show();
                    } else if(requestResponse.getCode() == 4700) { //요청 성공
                        Toast.makeText(view.getContext(), "요청 성공!", Toast.LENGTH_SHORT).show();
                        adapter.getItem().remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                    Log.d(TAG, "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                    Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                Log.d(TAG, "Fail msg : " + t.getMessage());
                Toast.makeText(view.getContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enableView(boolean enable, String type) {
        switch (type) {
            case FRIEND :
                friendListRecyclerViewEnable.setValue(enable);
                break;
            case SEARCH :
                searchListRecyclerViewEnable.setValue(enable);
                break;
        }
    }
}
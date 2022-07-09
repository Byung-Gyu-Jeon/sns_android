package com.example.sns.main.ui.friend;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.MyToolbar;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.MainActivity;
import com.example.sns.main.ui.userpage.UserPageActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class FriendSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private final String TAG = getClass().getSimpleName();

    private ImageButton goBackButton;
    private ImageButton searchButton;
    private SearchView searchView;
    private EditText searchEditText;
    private TextView textInfo;

    RecyclerView recyclerView;

    private final static String BASE_URL = "http://59.13.221.12:80/sns/getSearchList.do/";	// 기본 Base URL

    private final static String BYUNG_BASE_URL = "http://192.168.0.2:8080/sns/getSearchList.do/";	// 기본 Base URL


    private RetrofitService retrofitService;

    private RequestResponse requestResponse;
    private List<RequestResponse.SearchListEntity> list = new ArrayList<>();
    private List<RequestResponse.SearchListEntity> searchListEntity = new ArrayList<>();
    private TokenDTO tokenDTO = null;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_friend_search);

//        searchEditText = findViewById(R.id.editText_search);
            goBackButton = findViewById(R.id.go_back_btn);
//        searchButton = findViewById(R.id.search_btn);
            searchView = findViewById(R.id.friend_searchView);
            textInfo = findViewById(R.id.text_info);

            recyclerView = (RecyclerView)findViewById(R.id.recyclerview3);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);

            goBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("btn","버튼 누름");
                    if(view == goBackButton) {
                        finish();
//            findViewById(R.id.text_home).setVisibility(View.VISIBLE);
                    }
                }
            });
//        searchButton.setOnClickListener(this);
            searchView.setOnQueryTextListener(this);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_toolbar_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//
//        SearchManager searchManager = (SearchManager) FriendSearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(FriendSearchActivity.this.getComponentName()));
//        }
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("검색 단어 : ", newText);
        if(newText.length() > 0) {
            Call<RequestResponse> getSearchList = retrofitService.getSearchList(newText);
            Log.d("검색목록 요청 base url : ", ourInstance.getBaseUrl());
            tokenDTO = new TokenDTO();
            getSearchList.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.isSuccessful()) {
                        requestResponse = response.body();
                        tokenDTO = requestResponse.getTokenDTO();
                        if (tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                            App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                            App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                        }
                        if (requestResponse.getCode() == 4700) {
                            list = requestResponse.getSearchListEntity();

                            recyclerView.setVisibility(View.VISIBLE);
                            findViewById(R.id.text_home).setVisibility(View.INVISIBLE);
                            textInfo.setVisibility(View.GONE);

                            SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(FriendSearchActivity.this, GlideApp.with(FriendSearchActivity.this));
                            adapter.addItem(new item(0));

                            for (RequestResponse.SearchListEntity SearchList : list) {
                                adapter.addItem(new item(SearchList.getUserNo(), SearchList.getUserName(), SearchList.getUserImageUrl(), SearchList.getIsFriend(), 1));
                                Log.d("조회한 목록 ", "" + SearchList.getUserName() + "  " + SearchList.getUserImageUrl());
                            }

                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new SearchRecyclerAdapter.OnItemClickEventListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    final item item = adapter.getItem().get(position);
                                    Long userNo = item.getUserNo();
                                    Long myUserNo = App.sharedPreferenceManager.getUserNo();

                                    if (userNo.equals(myUserNo)) { // 검색 목록 클릭시 본인계정일 경우(아직 수정 안함)
                                        Intent intent = new Intent(FriendSearchActivity.this, UserPageActivity.class);

                                        Log.d("ContentValues", "넘길 유저 번호 : " + userNo);
                                        intent.putExtra("userNo", userNo);
                                        startActivity(intent);
                                        Toast.makeText(FriendSearchActivity.this, item.getUserName() + " Click event", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Intent intent = new Intent(FriendSearchActivity.this, UserPageActivity.class);

                                        Log.d("ContentValues", "넘길 유저 번호 : " + userNo);
                                        intent.putExtra("userNo", userNo);
                                        startActivity(intent);
                                        Toast.makeText(FriendSearchActivity.this, item.getUserName() + " Click event", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else if( requestResponse.getCode() == 4900) {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.text_home).setVisibility(View.GONE);
                            textInfo.setVisibility(View.VISIBLE);
                        }

                    } else {
                        // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                        Log.d("responseFail", "Status Code : " + response.code());
                        Log.d(TAG, response.errorBody().toString());
                        Toast.makeText(FriendSearchActivity.this, "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                    Log.d("onFail", "Fail msg : " + t.getMessage());
                    Toast.makeText(FriendSearchActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else if (newText.equals("")) {
            Log.d(TAG, "onCreate: x button clicked");
            recyclerView.setVisibility(View.VISIBLE);
            textInfo.setVisibility(View.GONE);
        }
        return true;
    }

//    @Override
//    public void onClick(View view) {
//        Log.d("btn","버튼 누름");
//        if(view == goBackButton) {
//            finish();
////            findViewById(R.id.text_home).setVisibility(View.VISIBLE);
//        } else if(view == searchButton){
//            if(searchEditText.getText().toString().length() != 0) {
//                Call<RequestResponse> getSearchList = retrofitService.getSearchList(searchEditText.getText().toString());
//                Log.d("검색목록 요청 base url : ",ourInstance.getBaseUrl());
//                tokenDTO = new TokenDTO();
//                getSearchList.enqueue(new Callback<RequestResponse>() {
//                    @Override
//                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
//                        if (response.isSuccessful()) {
//                            requestResponse = response.body();
//                            tokenDTO = requestResponse.getTokenDTO();
//                            if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
//                                App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
//                                App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
//                            }
//                            if(requestResponse.getCode() == 4700) {
//                                list = requestResponse.getSearchListEntity();
//
//                                findViewById(R.id.text_home).setVisibility(View.INVISIBLE);
//
//                                SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(FriendSearchActivity.this, GlideApp.with(FriendSearchActivity.this));
//                                adapter.addItem(new item(0));
//
//                                for(RequestResponse.SearchListEntity SearchList : list) {
//                                    adapter.addItem(new item(SearchList.getUserNo(), SearchList.getUserName(), SearchList.getUserImageUrl(), SearchList.getIsFriend(),1));
//                                    Log.d("조회한 목록 " ,""+ SearchList.getUserName() + "  " + SearchList.getUserImageUrl());
//                                }
//
//                                recyclerView.setAdapter(adapter);
//                            }
//
//                        } else {
//                            // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
//                            Log.d("responseFail","Status Code : " + response.code());
//                            Log.d(TAG,response.errorBody().toString());
//                            Toast.makeText(FriendSearchActivity.this, "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<RequestResponse> call, Throwable t) {
//                        // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
//                        Log.d("onFail","Fail msg : " + t.getMessage());
//                        Toast.makeText(FriendSearchActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }else {
//                Toast.makeText(FriendSearchActivity.this, "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
//            }
//        } // searchButton endPoint..
//
//    } // onClick endPoint..
}
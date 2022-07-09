package com.example.sns.main.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.RealmUser;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class CreateChatroomActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private final String BROADCAST_MESSAGE = "com.example.sns.main.ui.chat";
    private BroadcastReceiver mReceiver = null;

    private ImageButton goBackButton;
    private Button okButton;
    private SearchView searchView;
    private TextView textInfo;

    RecyclerView pickerRecyclerView;
    RecyclerView searchListRecyclerView;
    private final static String BASE_URL = "http://59.13.221.12:80/sns/getSearchList.do/";	// 기본 Base URL
    private final static String BASE_URL_SET_ROOM = "http://59.13.221.12:80/sns/setRoom.do/";	// 기본 Base URL
    private RetrofitService retrofitService;
    private RequestResponse requestResponse;
    private RequestResponse createChatRoomResponse;
    private List<RequestResponse.SearchListEntity> list = new ArrayList<>();
    private List<RequestResponse.SearchListEntity> searchListEntity = new ArrayList<>();
    private TokenDTO tokenDTO = null;
    private List<CheckBoxData> checkBoxDataList;
    private ArrayList<ChatRoomDTO> checkedDataList = new ArrayList<ChatRoomDTO>();
    private int checkedNum;
    private String roomName = "";
    private String myRoomName = "";
    private List<String> roomNameCreated;
    private List<Long> userNoList;
    List<List<String>> userImageList;
    List<String> imageUrlLists;
    List<List<Long>> noList;
    List<Long> userNumLists;
    int addedNameCount;
    private int countNum = 1; // 본인 포함..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);
        registerReceiver();

        goBackButton = findViewById(R.id.chatroom_back_btn);
        okButton = findViewById(R.id.ok_button);
        searchView = findViewById(R.id.create_chatroom_searchview);
        textInfo = findViewById(R.id.text_info);

        pickerRecyclerView = (RecyclerView) findViewById(R.id.user_picker);
        LinearLayoutManager pickerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        pickerRecyclerView.setLayoutManager(pickerLayoutManager);

        searchListRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        LinearLayoutManager searchListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchListRecyclerView.setLayoutManager(searchListLayoutManager);

        retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("btn","버튼 누름");
                if(view == goBackButton) {
                    finish();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                String roomNo = UUID.randomUUID().toString();
                Long myNo = App.sharedPreferenceManager.getUserNo();
                Log.d("체크박스"," myNo : "+ myNo);
                RealmUser rs = App.realm.where(RealmUser.class).equalTo("userNo",myNo).findFirst();
                try {
                    Log.d("체크박스","불러온 user : "+ rs);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("체크박스","불러온 user : "+ rs);
                }
                
                for (CheckBoxData checkBoxData : checkBoxDataList) {
                    if (checkBoxData.getChecked()) {
                        checkedDataList.add(new ChatRoomDTO(roomNo, checkBoxData.getImageUrl(), checkBoxData.getUserName(), checkBoxData.getUserNo())); // chatroom에 넘겨주려는 값
                    }
                }
//                for (int i = 0; i < checkedDataList.size(); i++) {
//                        countNum++;
//                        roomName = roomName + checkedDataList.get(i).getUserName();
//                        Log.d("체크박스", "만들어진 checkedDataList : " + checkedDataList.get(i).getUserName());
//                        if (checkedDataList.size() == i + 1) break;
//                        roomName = roomName + ",";
//                }
//                Log.d("체크박스","만들어진 roomName : "+ roomName);
                chatRoomDTO.setRoomNo(roomNo);
                chatRoomDTO.setUserNo(rs.getUserNo());
                chatRoomDTO.setUserName(rs.getUserName());
                chatRoomDTO.setMyImageUrl(rs.getUserImageUrl());
                checkedDataList.add(chatRoomDTO); // 본인도 추가..

                // 초대된 사람들에게 맞는 채팅방 이름, 사진이름 생성..
                roomNameCreated = new ArrayList<>();
                userNoList = new ArrayList<>();
                userImageList = new ArrayList<>();
                imageUrlLists = new ArrayList<>();
                noList = new ArrayList<>();
                userNumLists = new ArrayList<>();
                RealmList<RealmList<String>> realmUserImageList = new RealmList<>();
                RealmList<String> realmImageUrlList = new RealmList<>();
                RealmList<RealmList<Long>> realmNoLists = new RealmList<>();
                RealmList<Long> realmUserNumList = new RealmList<>();
                for(int i = 0; i < checkedDataList.size(); i++) {
                    Long key = checkedDataList.get(i).getUserNo();
                    addedNameCount = 0;
                    roomName = "";
                    realmImageUrlList.clear();
                    realmUserNumList.clear();
                    imageUrlLists.clear();
                    userNumLists.clear();
                    for (int j = 0; j < checkedDataList.size(); j++) {
                            if(!key.equals(checkedDataList.get(j).getUserNo())) {
                                roomName = roomName.concat(checkedDataList.get(j).getUserName());
                                realmImageUrlList.add(checkedDataList.get(j).getMyImageUrl());
                                imageUrlLists.add(checkedDataList.get(j).getMyImageUrl());
                                realmUserNumList.add(checkedDataList.get(j).getUserNo());
                                userNumLists.add(checkedDataList.get(j).getUserNo());
                                Log.d("체크박스","붙여진 이름 : "+ roomName);
                                Log.d("체크박스","붙여진 사진 : "+ checkedDataList.get(j).getMyImageUrl());
                                addedNameCount++;
                            }
                            if (checkedDataList.size() == addedNameCount + 1) break;
                            if(roomName.endsWith(",")) continue;
                            if(!roomName.equals(""))
                                roomName = roomName.concat(",");
                        }
                    Log.d("체크박스","완성된 이름 : "+ roomName);
                    roomNameCreated.add(roomName);
                    userNoList.add(key);

                    Log.d("체크박스","완성된 imageList : "+ imageUrlLists);
                    realmUserImageList.add(realmImageUrlList);
                    userImageList.add(imageUrlLists);

                    Log.d("체크박스","완성된 userNoList : "+ userNumLists);
                    realmNoLists.add(realmUserNumList);
                    noList.add(userNumLists);
//                    Log.d("체크박스","완성된 사진 : "+ realmUserImageList.get(i));

                    if (myNo == key) myRoomName = roomName;
                }

                 //각 유저에 맞는 채팅방 이름 ,유저 번호들 등 정보 매칭(서버에 저장하기 위한..)
                for(int i = 0; i < checkedDataList.size(); i++) {
                    for(int j = 0; j < checkedDataList.size(); j++) {
                        if (checkedDataList.get(j).getUserNo() == userNoList.get(i)) {
                            checkedDataList.get(j).setRoomName(roomNameCreated.get(i));
                            checkedDataList.get(j).setUserImageUrl(realmUserImageList.get(i));
                            checkedDataList.get(j).setUserNumbers(realmNoLists.get(i));
                            checkedDataList.get(j).setParticipantNumbers(addedNameCount + 1);
                            break;
                        }
                    }
                }


//                App.realm.executeTransactionAsync(transactionRealm ->{
//                    transactionRealm.insert(chatRoomDTO);
//                });

//                App.realm.beginTransaction();
//                ChatRoomDTO manageChatRoomDTO = App.realm.copyToRealm(chatRoomDTO);
//                manageChatRoomDTO.getChatRoomDTORealmList().add(new ChatRoomDTO(roomNo, rs.getUserImageUrl(), rs.getUserName(), rs.getUserNo(), roomName, countNum));
//                for (ChatRoomDTO checkedBoxData : checkedDataList) {
//                    manageChatRoomDTO.getChatRoomDTORealmList().add(new ChatRoomDTO(roomNo, checkedBoxData.getUserImageUrl(), checkedBoxData.getUserName(), checkedBoxData.getUserNo(), roomName, countNum));
//                }
//                App.realm.commitTransaction();

                Gson gson = new Gson();
                String objJson = gson.toJson(checkedDataList);
                retrofitService = ourInstance.getInstance(BASE_URL_SET_ROOM, true).create(RetrofitService.class);
                Call<RequestResponse> setRoom = retrofitService.setChatRoom(objJson);
                setRoom.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.isSuccessful()) {
                            createChatRoomResponse = response.body();
                            tokenDTO = createChatRoomResponse.getTokenDTO();
                            if (tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                                App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                            }
                            if (createChatRoomResponse.getCode() == 4700) {
                                ChatRoomDTO manageChatRoomDTO = new ChatRoomDTO();
                                for (ChatRoomDTO checkedBoxData : checkedDataList) {
                                    if ( checkedBoxData.getUserNo() == App.sharedPreferenceManager.getUserNo()) {
                                        manageChatRoomDTO.setRoomNo(roomNo);
                                        manageChatRoomDTO.setRoomName(checkedBoxData.getRoomName());
                                        manageChatRoomDTO.setParticipantNumbers(checkedBoxData.getParticipantNumbers());
                                        manageChatRoomDTO.setUserNo(checkedBoxData.getUserNo());
                                        manageChatRoomDTO.setUserName(checkedBoxData.getUserName());
                                        manageChatRoomDTO.setMyImageUrl(checkedBoxData.getMyImageUrl());
                                        manageChatRoomDTO.setUserImageUrl(checkedBoxData.getUserImageUrl());
                                        manageChatRoomDTO.setUserNumbers(checkedBoxData.getUserNumbers());
                                    }
                                }

                                App.realm.executeTransactionAsync(transactionRealm ->{
                                    transactionRealm.insert(manageChatRoomDTO);
                                });
//                                List<Long> userNoList = new ArrayList<>();
//                                List<String> imageUrlList = new ArrayList<>();
//                                for (Long userNo : manageChatRoomDTO.getUserNumbers()) {
//                                    userNoList.add(userNo);
//                                }
//                                for (String url : manageChatRoomDTO.getUserImageUrl()) {
//                                    imageUrlList.add(url);
//                                }



//                                userNoList = (List<Long>) manageChatRoomDTO.getUserNumbers();
//                                imageUrlList = (List<String>) manageChatRoomDTO.getUserImageUrl();
                                Intent broadCastIntent = new Intent(ChatBroadCastReceiver.BROADCAST_TOPIC_MESSAGE);
//                                broadCastIntent.putExtra("topic",checkedDataList);
                                broadCastIntent.putExtra("key", (Serializable) userNoList);
                                broadCastIntent.putExtra("roomNo", roomNo);
                                broadCastIntent.putExtra("roomName", (Serializable) roomNameCreated);
                                broadCastIntent.putExtra("partNum", addedNameCount + 1);
                                broadCastIntent.putExtra("userNoList", (Serializable) noList);
                                broadCastIntent.putExtra("imageUrl", (Serializable) userImageList);
//                                sendBroadcast(broadCastIntent);

                                Intent intent = new Intent(CreateChatroomActivity.this, ChatroomActivity.class);
                                intent.putExtra("roomId",roomNo);
                                intent.putExtra("roomName",myRoomName);
                                intent.putExtra("isRoomCreated",true);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                            Log.d("responseFail", "Status Code : " + response.code());
                            Log.d("responseFail", response.errorBody().toString());
                            Toast.makeText(CreateChatroomActivity.this, "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                        Log.d("onFail", "Fail msg : " + t.getMessage());
                        Toast.makeText(CreateChatroomActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

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
                            searchListRecyclerView.setVisibility(View.VISIBLE);
                            textInfo.setVisibility(View.GONE);
                            list = requestResponse.getSearchListEntity();

                            SearchListAdapter adapter = new SearchListAdapter(CreateChatroomActivity.this, GlideApp.with(CreateChatroomActivity.this));
                            adapter.addItem(new SearchListItem(0));

                            for (RequestResponse.SearchListEntity SearchList : list) {
                                if(SearchList.getUserNo() == App.sharedPreferenceManager.getUserNo()) continue;
                                adapter.addItem(new SearchListItem(SearchList.getUserNo(), SearchList.getUserName(), SearchList.getUserImageUrl(), SearchList.getIsFriend(), 1));
                                Log.d("조회한 목록 ", "" + SearchList.getUserName() + "  " + SearchList.getUserImageUrl());
                            }

                            searchListRecyclerView.setAdapter(adapter);
                            checkBoxDataList = adapter.getCheckBoxData();
                            checkedNum = adapter.getCheckedNum();
                        }else if(requestResponse.getCode() == 4900) {
                            searchListRecyclerView.setVisibility(View.GONE);
                            textInfo.setVisibility(View.VISIBLE);
                        }

                    } else {
                        // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                        Log.d("responseFail", "Status Code : " + response.code());
                        Log.d("responseFail", response.errorBody().toString());
                        Toast.makeText(CreateChatroomActivity.this, "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                    Log.d("onFail", "Fail msg : " + t.getMessage());
                    Toast.makeText(CreateChatroomActivity.this, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else if (newText.equals("")) {
            Log.d(TAG, "onCreate: x button clicked");
            searchListRecyclerView.setVisibility(View.VISIBLE);
            textInfo.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    /** 동적으로(코드상으로) 브로드 캐스트를 등록한다. **/
    private void registerReceiver(){
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if(mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(BROADCAST_MESSAGE);

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                PickerAdapter adapter = new PickerAdapter(CreateChatroomActivity.this, GlideApp.with(CreateChatroomActivity.this));
                checkedNum = intent.getIntExtra("num",0);
                checkBoxDataList = (ArrayList<CheckBoxData>)intent.getSerializableExtra("value");
                Animation fadeOut = AnimationUtils.loadAnimation(context,R.anim.fade_out);
                if(intent.getAction().equals(BROADCAST_MESSAGE)){
                    if(checkedNum != 0) {
                        findViewById(R.id.user_picker).setVisibility(View.VISIBLE);
//                        findViewById(R.id.user_picker).startAnimation(fadeOut);
                        if(!okButton.isEnabled()) {
                            findViewById(R.id.ok_button).setEnabled(!okButton.isEnabled());
                            okButton.setTextColor(Color.BLACK);
                        }
                        Log.d("체크박스"," visible : "+ checkedNum);
                    } else {
                        findViewById(R.id.user_picker).setVisibility(View.GONE);
//                        findViewById(R.id.user_picker).startAnimation(fadeOut);
                        findViewById(R.id.ok_button).setEnabled(!okButton.isEnabled());
                        okButton.setTextColor(Color.GRAY);
                        Log.d("체크박스"," gone : "+ checkedNum);
                    }
                    for (CheckBoxData checkBoxData : checkBoxDataList) {
                        Log.d("체크박스"," 전달된 checked : "+checkBoxData.getUserName() + "  " + checkBoxData.getChecked());
                        if(checkBoxData.getChecked()){
                            adapter.addItem(new CheckBoxData(checkBoxData.getUserName(), checkBoxData.getImageUrl()));
                            pickerRecyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    Log.d("체크박스","전달된 checked Num : "+ checkedNum);
                }
            }
        };

        this.registerReceiver(this.mReceiver, theFilter);

    }

    /** 동적으로(코드상으로) 브로드 캐스트를 종료한다. **/
    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
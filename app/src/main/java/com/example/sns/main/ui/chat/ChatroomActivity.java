package com.example.sns.main.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.ChatMsgDTO;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.EchoModel;
import com.example.sns.Model.RealmUser;
import com.example.sns.MyToolbar;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.friend.FriendSearchActivity;
import com.example.sns.main.ui.friend.SearchRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class ChatroomActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChatroomActivity";
    private final static String BASE_URL = "http://59.13.221.12:80/sns/";	// 기본 Base URL
    private Context context;
    private EditText sendEditText;
    private ImageButton sendBtn;
    RecyclerView recyclerView;
    ChatMsgAdapter adapter;
    private String roomId;
    private Long myNo;
    private RetrofitService retrofitService;
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private Disposable mRestPingDisposable;
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm", Locale.getDefault());
    private final SimpleDateFormat mTimeFormatText = new SimpleDateFormat("yyyy년 MMM dd일 E요일", Locale.getDefault());
    private Gson mGson = new GsonBuilder().create();

    private List<ChatRoomDTO> checkedDataList = new ArrayList<ChatRoomDTO>();
    private ChatRoomDTO chatRoomDTO;

    private ChatMsgDTO chatMsgDTO;
    private List<ChatRoomDTO> roomDataList;
    private boolean isClickedNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatroom);
        myNo = App.sharedPreferenceManager.getUserNo();
        Intent intent = getIntent();
        // 채팅방 생성 시 넘어오는 값들
        roomId = intent.getStringExtra("roomId");
        String roomName = intent.getStringExtra("roomName");
        boolean isRoomCreated = intent.getBooleanExtra("isRoomCreated", false);

        // recyler view 아이템 클릭시 넘어오는 데이터 값들
        boolean isClickedView = intent.getBooleanExtra("isViewClicked", false);
        String viewRoomId = intent.getStringExtra("roomNo");
        String viewRoomName = intent.getStringExtra("viewRoomName");
        List<Long> userNoList = (List<Long>) intent.getSerializableExtra("userNoList");
        List<String> imageList = (List<String>)intent.getSerializableExtra("imageList");
        int partNum = intent.getIntExtra("partNum", 0);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && (!isRoomCreated && !isClickedView)) {
            isClickedNotification = true;
            String msgDTOJson = bundle.getString("data");
            String roomListJson = bundle.getString("roomList");

            Gson gson = new Gson();
            chatMsgDTO = new ChatMsgDTO();
            roomDataList = new ArrayList<ChatRoomDTO>();

            try {
                Type listType = new TypeToken<ArrayList<ChatRoomDTO>>(){}.getType();
                roomDataList = gson.fromJson(roomListJson, listType);
                chatMsgDTO = gson.fromJson(msgDTOJson, ChatMsgDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            makeAndSaveChatInfo(chatMsgDTO, roomDataList);
        }

        if(isClickedView){
            roomId = viewRoomId;
            roomName = viewRoomName;
        } else if(isClickedNotification) {
            ChatRoomDTO chatRoomDTO = App.realm.where(ChatRoomDTO.class).equalTo("roomNo",chatMsgDTO.getRoomId()).findFirst();
            roomId = chatMsgDTO.getRoomId();
            roomName = chatRoomDTO.getRoomName();
        }

//        ChatRoomItem chatRoomItem = (ChatRoomItem) intent.getSerializableExtra("roomInfo");

        chatRoomDTO = App.realm.where(ChatRoomDTO.class).equalTo("roomNo", roomId).findFirst();


        // 10/04 임시 툴바 테스트
        MyToolbar.show(this,roomName,true);

        recyclerView = (RecyclerView)findViewById(R.id.chatroom_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatMsgAdapter(GlideApp.with(ChatroomActivity.this),ChatroomActivity.this);

        retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);

        sendEditText = findViewById(R.id.send_editText);
        sendBtn = findViewById(R.id.send_Btn);
        sendBtn.setOnClickListener(this);

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + "59.13.221.12"
                + ":" + "80" + "/sns/stomp-chat/websocket");
        connectStomp();

        //받은 채팅있으면 표시
        RealmResults<ChatMsgDTO> rs = App.realm.where(ChatMsgDTO.class).equalTo("roomId", roomId).findAll();
        for (ChatMsgDTO chatMsgDTO : rs) {
            if (rs != null)
                add(chatMsgDTO, 1); // TYPE은 방금 보낸 메시지인지 db에 저장된 메시지인지 구분하기위한 값
        }
//        adapter.addItem(new ChatMsgItem(mTimeFormatText.format(new Date()), 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chatroom_menu :
                Toast.makeText(this, "Menu click", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home :
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == sendBtn) {
            if(sendEditText.getText().toString().length() != 0) {
                ChatMsgDTO chatMsgDTO = new ChatMsgDTO(chatRoomDTO.getRoomNo(), chatRoomDTO.getUserNo(), chatRoomDTO.getUserName(), chatRoomDTO.getMyImageUrl(), sendEditText.getText().toString());
//                Toast.makeText(this, "보내기 버튼 클릭됨.", Toast.LENGTH_SHORT).show();
                sendEchoViaRest(chatMsgDTO);
                sendEditText.setText("");

//                sendEchoViaStomp(sendEditText.getText().toString());
            }else {
                Toast.makeText(this, "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void disconnectStomp(View view) {
        mStompClient.disconnect();
    }

    public static final String LOGIN = "login";

    public static final String PASSCODE = "passcode";

    public void connectStomp() {

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            toast("Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            toast("Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        // Receive greetings
        Disposable dispTopic = mStompClient.topic("/topic/" + roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    Log.d(TAG, "Destination " + headers.get(1).getValue());
                    add(mGson.fromJson(topicMessage.getPayload(), ChatMsgDTO.class), 0);
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);

        mStompClient.connect(headers);
    }

    public void sendEchoViaStomp(String msg) {
//        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(mStompClient.send("/topic/hello-msg-mapping", msg)
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                }));
    }

    public void sendEchoViaRest(ChatMsgDTO msg) {
        Gson gson = new Gson();
        String objJson = gson.toJson(msg);
        mRestPingDisposable = retrofitService
                .sendRestEcho(objJson)
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "REST echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send REST echo", throwable);
                    toast(throwable.getMessage());
                });
    }

    private void add(ChatMsgDTO echoModel, int type) {
        Intent intent = new Intent(ChatBroadCastReceiver.BROADCAST_SEND_MESSAGE);
        intent.putExtra("sendRoomNo", echoModel.getRoomId());
        intent.putExtra("message", echoModel.getMessage());
        intent.putExtra("time", echoModel.getTime());
        Long userNo = echoModel.getUserNo();
//        Calendar calendar = Calendar.getInstance();
//        Calendar prevCalendar = Calendar.getInstance();
        // 오늘 날짜 표시
//        if (App.realm.where(ChatRoomDTO.class).equalTo("userNo",myNo).equalTo("roomNo", roomId).findFirst().getReceivedMsg() == null)
//            adapter.addItem(new ChatMsgItem(mTimeFormatText.format(new Date()), 0));
//        else {
//            Boolean sameData = calendar[Calendar.YEAR] == prevCalendar[Calendar.YEAR] && calendar[Calendar.MONTH] == prevCalendar[Calendar.MONTH] && calendar[Calendar.DATE] == prevCalendar[Calendar.DATE];
//        }
        if(myNo == userNo)
            adapter.addItem(new ChatMsgItem(echoModel.getRoomId(), echoModel.getUserNo(), echoModel.getUserName(), echoModel.getImageUrl(), echoModel.getMessage(), echoModel.getTime(), 2 ));
        else
            adapter.addItem(new ChatMsgItem(echoModel.getRoomId(), echoModel.getUserNo(), echoModel.getUserName(), echoModel.getImageUrl(), echoModel.getMessage(), echoModel.getTime(), 1 ));

        // 시간 저장 후 db에 메시지 저장
        if (type == 0)
            App.realm.executeTransactionAsync(transactionRealm -> {
                ChatRoomDTO chatRoomDTO = transactionRealm.where(ChatRoomDTO.class).equalTo("roomNo",echoModel.getRoomId()).findFirst();
                chatRoomDTO.setReceivedMsg(echoModel.getMessage());
                chatRoomDTO.setReceivedMsgTime(echoModel.getTime());
                transactionRealm.insert(echoModel);
                transactionRealm.insertOrUpdate(chatRoomDTO);
            });
        sendBroadcast(intent);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(ChatroomActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }

    private void makeAndSaveChatInfo(ChatMsgDTO chatMsgDTO, List<ChatRoomDTO> roomDataList) {
        Long myNo = App.sharedPreferenceManager.getUserNo();
        RealmUser realmUser = App.realm.where(RealmUser.class).findFirst();
        ChatRoomDTO roomDTO = new ChatRoomDTO();
        for (ChatRoomDTO chatRoomDTO : roomDataList) {
            if (myNo == chatRoomDTO.getUserNo()) {
                for (Iterator<Long> itr = chatRoomDTO.getUserNumbers().iterator(); itr.hasNext(); ) {
                    if (myNo == itr.next()) {
                        itr.remove();
                    }
                }
                for (Iterator<String> itr = chatRoomDTO.getUserImageUrl().iterator(); itr.hasNext(); ) {
                    if (realmUser.getUserImageUrl() == itr.next()) {
                        itr.remove();
                    }
                }
                roomDTO = chatRoomDTO;
            }
        }
        ChatRoomDTO finalRoomDTO = roomDTO;
        App.realm.executeTransaction(realm -> {
            realm.insertOrUpdate(chatMsgDTO);
            realm.insertOrUpdate(finalRoomDTO);
        });
    }
}
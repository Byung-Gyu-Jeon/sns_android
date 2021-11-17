package com.example.sns.main.ui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.EchoModel;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.MainActivity;
import com.example.sns.main.ui.friend.FriendSearchActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.StompClient;

import static android.app.Activity.RESULT_OK;
import static com.example.sns.Network.ApiClient.ourInstance;

public class ChatFragment extends Fragment {
    public static final String BROADCAST_RECEIVE_MESSAGE = "com.example.sns.broadcastreceiver.receive";
    private static final String TAG = "ChatFragment";
    private final static String BASE_URL = "http://59.13.221.12:80/sns/";	// 기본 Base URL
    private Context context;
    private BroadcastReceiver mReceiver = null;

    private RetrofitService retrofitService;

    private ChatViewModel chatViewModel;
    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    RealmResults<ChatRoomDTO> chatRoomIdList;

    private SimpleAdapter mAdapter;
    private List<String> mDataSet = new ArrayList<>();
    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault());
    private RecyclerView mRecyclerView;
    private Gson mGson = new GsonBuilder().create();

    private CompositeDisposable compositeDisposable;

    ArrayList<ChatRoomItem> items = new ArrayList<ChatRoomItem>();
    ImageButton imageButton;
    Button button1;
    Button button2;
    Button button3;
    Button button4;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        context = container.getContext();
        // 여기에 실제코드 작성

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ChatFragment.BROADCAST_RECEIVE_MESSAGE);
        registerReceiver(mReceiver, theFilter);

        imageButton = (ImageButton)root.findViewById(R.id.open_chat_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("초대하기 버튼 누름","onclick 실행");
                Intent intent = null;
                intent = new Intent(getContext(), CreateChatroomActivity.class);
//                intent.putExtra("token",token);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatRoomAdapter(GlideApp.with(getContext()), getContext());

//        mRecyclerView = root.findViewById(R.id.recycler_view);
//        mAdapter = new SimpleAdapter(mDataSet);
//        mAdapter.setHasStableIds(true);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));

//        button1 = (Button) root.findViewById(R.id.test_btn1);
//        button2 = (Button) root.findViewById(R.id.test_btn2);
//        button3 = (Button) root.findViewById(R.id.test_btn3);
//        button4 = (Button) root.findViewById(R.id.test_btn4);

//        button1.setOnClickListener(this::connectStomp);
//        button2.setOnClickListener(this::disconnectStomp);
//        button3.setOnClickListener(this::sendEchoViaStomp);
//        button4.setOnClickListener(this::sendEchoViaRest);

        Long myNo = App.sharedPreferenceManager.getUserNo();
        chatRoomIdList = App.realm.where(ChatRoomDTO.class).equalTo("userNo",myNo).findAll();
        retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);

//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + "59.13.221.12"
//                + ":" + "80" + "/sns/stomp-chat/websocket");

//        resetSubscriptions();
//        connectStomp(chatRoomIdList);

        for (ChatRoomDTO chatRoomDTO : chatRoomIdList) {
//            if(chatRoomDTO.getReceivedMsg() != null) {
                // 채팅 목록 가져와서 아이템뷰에  출력
                int numberOfUsers = chatRoomDTO.getParticipantNumbers();
                adapter.addItem(new ChatRoomItem(chatRoomDTO.getRoomNo(), chatRoomDTO.getRoomName(), chatRoomDTO.getUserNumbers(), chatRoomDTO.getUserImageUrl(), numberOfUsers, chatRoomDTO.getReceivedMsg(), chatRoomDTO.getReceivedMsgTime(), chatRoomDTO.getReceivedMsgNumbers(), numberOfUsers - 1 ));
        }
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ChatRoomAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ChatRoomItem item = adapter.getItem().get(position);
                Intent intent = new Intent(getContext(), ChatroomActivity.class);
                List<Long> userNoList = new ArrayList<>();
                List<String> imageList = new ArrayList<>();
                for (Long userNo : item.getUserNo()) { userNoList.add(userNo); }
                for (String url : item.getUserImageUrl()) { imageList.add(url); }

                intent.putExtra("isViewClicked",true);
                intent.putExtra("roomNo", item.getRoomNo());
                intent.putExtra("viewRoomName", item.getRoomName());
                intent.putExtra("userNoList",(Serializable) userNoList);
                intent.putExtra("imageList",(Serializable)imageList);
                intent.putExtra("partNum", item.getParticipantNumbers());
//                intent.putExtra("roomInfo", item);
                startActivity(intent);
                Toast.makeText(context, item.getRoomName() + " Click event", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemLongClickListener(new ChatRoomAdapter.OnItemLongClickEventListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final ChatRoomItem item = adapter.getItem().get(position);
                Toast.makeText(context, item.getRoomName() + " Long click event", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("testresult", "onActivityResult: 호출되었음");
        if (requestCode == 0)
            if (resultCode == RESULT_OK) {
                if (adapter.getItemCount() == 0) {
                    for (ChatRoomDTO chatRoomDTO : chatRoomIdList) {
//            if(chatRoomDTO.getReceivedMsg() != null) {
                        // 채팅 목록 가져와서 아이템뷰에  출력
                        int numberOfUsers = chatRoomDTO.getParticipantNumbers();
                        adapter.addItem(new ChatRoomItem(chatRoomDTO.getRoomNo(), chatRoomDTO.getRoomName(), chatRoomDTO.getUserNumbers(), chatRoomDTO.getUserImageUrl(), numberOfUsers, chatRoomDTO.getReceivedMsg(), chatRoomDTO.getReceivedMsgTime(), chatRoomDTO.getReceivedMsgNumbers(), numberOfUsers - 1));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
    }

    public void disconnectStomp(View view) {
        mStompClient.disconnect();
    }

    public static final String LOGIN = "login";

    public static final String PASSCODE = "passcode";

    public void connectStomp(RealmResults chatRoomDTO) {

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
                            Log.e(TAG, "Stomp connection open");
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
        Disposable dispTopic = mStompClient.topic("/topic/greetings")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);

        mStompClient.connect(headers);
    }

    public void sendEchoViaStomp(View v) {
//        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(mStompClient.send("/topic/hello-msg-mapping", "Echo STOMP " + mTimeFormat.format(new Date()))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                }));
    }

//    public void sendEchoViaRest(View v) {
//        mRestPingDisposable = retrofitService
//                .sendRestEcho("Echo REST " + mTimeFormat.format(new Date()))
//                .compose(applySchedulers())
//                .subscribe(() -> {
//                    Log.d(TAG, "REST echo send successfully");
//                }, throwable -> {
//                    Log.e(TAG, "Error send REST echo", throwable);
//                    toast(throwable.getMessage());
//                });
//    }

    private void addItem(EchoModel echoModel) {
        mDataSet.add(echoModel.getEcho() + " - " + mTimeFormat.format(new Date()));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mDataSet.size() - 1);
    }

    private void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onDestroy() {
//        mStompClient.disconnect();
//
//        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
//        if (compositeDisposable != null) compositeDisposable.dispose();
//        super.onDestroy();
//    }

    private void registerReceiver(BroadcastReceiver mReceiver, IntentFilter theFilter){
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if(mReceiver != null) return;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actionName = intent.getAction();
                Log.d(TAG, "onReceive: chatfragment invoked");
                if (actionName.equals(ChatFragment.BROADCAST_RECEIVE_MESSAGE)) {
                    String lastRoomId = intent.getStringExtra("sendRoomNo");
                    // 현재 보여지고있는 fragment 가져오기
                    for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible()) {
                            if (fragment instanceof ChatFragment) {
                                ChatRoomDTO chatRoomDTO = App.realm.where(ChatRoomDTO.class).equalTo("roomNo", lastRoomId).findFirst();
                                int numberOfUsers = chatRoomDTO.getParticipantNumbers();
                                adapter.addItem(new ChatRoomItem(chatRoomDTO.getRoomNo(), chatRoomDTO.getRoomName(), chatRoomDTO.getUserNumbers(), chatRoomDTO.getUserImageUrl(), numberOfUsers, chatRoomDTO.getReceivedMsg(), chatRoomDTO.getReceivedMsgTime(), chatRoomDTO.getReceivedMsgNumbers(), numberOfUsers - 1 ));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        };

        this.registerReceiver(mReceiver, theFilter);
        Log.d("myreceiver", "onReceive: registered");
    }

    private void unregisterReceiver(BroadcastReceiver mReceiver) {
        if(this.mReceiver != null){
            this.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }
}
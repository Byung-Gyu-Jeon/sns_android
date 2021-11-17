package com.example.sns.main.ui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.sns.App;
import com.example.sns.Model.ChatMsgDTO;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.RealmUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ChatBroadCastReceiver extends BroadcastReceiver {
    public static final String BROADCAST_TOPIC_MESSAGE = "com.example.sns.broadcastreceiver.topic";
    public static final String BROADCAST_SEND_MESSAGE = "com.example.sns.broadcastreceiver.send";
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";
    private static final String TAG = "ChatReceiver";

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Gson mGson = new GsonBuilder().create();

    private String lastRoomId;
    private String roomName;
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Long myNo = App.sharedPreferenceManager.getUserNo();

        RealmList<Long> RealmUserNoList = new RealmList<>();
        RealmList<String> RealmImageUrlList = new RealmList<>();

        List<Long> keyList = (ArrayList<Long>) intent.getSerializableExtra("key");
        String roomNo = intent.getStringExtra("roomNo");
        List<String> roomNameList = (ArrayList<String>) intent.getSerializableExtra("roomName");
        int partNum = intent.getIntExtra("partNum", 0);
        List<List<Long>> userNoLists = (ArrayList<List<Long>>) intent.getSerializableExtra("userNoList");
        List<List<String>> imageUrlLists = (ArrayList<List<String>>) intent.getSerializableExtra("imageUrl");

//        lastRoomId = roomNo;

        if (actionName.equals(BROADCAST_TOPIC_MESSAGE)) {
            List<Long> userNoList = new ArrayList<>();
            List<String> imageUrlList = new ArrayList<>();

            for (int j = 0; j < keyList.size(); j++) {
                if (myNo == keyList.get(j)) {
                    roomName = roomNameList.get(j);
                    imageUrlList = imageUrlLists.get(j);
                    userNoList = userNoLists.get(j);
                    break;
                }
            }

            for (Long userNo : userNoList) {
                RealmUserNoList.add(userNo);
            }
            for (String url : imageUrlList) {
                RealmImageUrlList.add(url);
            }

            App.realm.executeTransactionAsync(transactionRealm -> {
                RealmUser realmUser = transactionRealm.where(RealmUser.class).findFirst();
                ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                chatRoomDTO.setRoomNo(roomNo);
                chatRoomDTO.setRoomName(roomName);
                chatRoomDTO.setParticipantNumbers(partNum);
                chatRoomDTO.setUserNumbers(RealmUserNoList);
                chatRoomDTO.setUserImageUrl(RealmImageUrlList);
                chatRoomDTO.setUserNo(realmUser.getUserNo());
                chatRoomDTO.setUserName(realmUser.getUserName());
                transactionRealm.insert(chatRoomDTO);
            });
        }

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));

            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + "59.13.221.12"
                    + ":" + "80" + "/sns/stomp-chat/websocket");
            mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

            Disposable dispLifecycle = mStompClient.lifecycle()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lifecycleEvent -> {
                        switch (lifecycleEvent.getType()) {
                            case OPENED:
                                Log.d(TAG, "Stomp connection open" );
                                break;
                            case ERROR:
                                Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                                Toast.makeText(context, "Stomp connection error", Toast.LENGTH_SHORT).show();
                                break;
                            case CLOSED:
                                Toast.makeText(context, "Stomp connection closed", Toast.LENGTH_SHORT).show();
                                resetSubscriptions();
                                break;
                            case FAILED_SERVER_HEARTBEAT:
                                Toast.makeText(context, "Stomp failed server heartbeat", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
            compositeDisposable.add(dispLifecycle);

            List<Disposable> disposableList = new ArrayList<>();
            if (actionName.equals(BROADCAST_SEND_MESSAGE) || actionName.equals(BROADCAST_TOPIC_MESSAGE)) {
                if (actionName.equals(BROADCAST_SEND_MESSAGE)) lastRoomId = intent.getStringExtra("sendRoomNo");
                else if (actionName.equals(BROADCAST_TOPIC_MESSAGE)) lastRoomId = roomNo;
                Log.d(TAG, "onReceive lastRoomId : " + lastRoomId);
                Disposable dispTopic = mStompClient.topic("/topic/" + lastRoomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stompMessage -> {
                            ChatMsgDTO chatMsgDTO = mGson.fromJson(stompMessage.getPayload(), ChatMsgDTO.class);
                            Log.d(TAG, "onReceive: msg" + chatMsgDTO.getMessage());
                            Log.d(TAG, "onReceive: time" + chatMsgDTO.getTime());
                            if (actionName.equals(BROADCAST_SEND_MESSAGE)) {
                                String message = intent.getStringExtra("message");
                                String time = intent.getStringExtra("time");
                                Log.d(TAG, "onReceive intent: msg" + message);
                                Log.d(TAG, "onReceive intent: time" + time);
                                App.realm.executeTransactionAsync(transactionRealm -> {
                                    ChatRoomDTO chatRoomDTO = transactionRealm.where(ChatRoomDTO.class).equalTo("roomNo",lastRoomId).findFirst();
                                    int num = chatRoomDTO.getReceivedMsgNumbers();
                                    chatRoomDTO.setReceivedMsg(message);
                                    chatRoomDTO.setReceivedMsgTime(time);
                                    chatRoomDTO.setReceivedMsgNumbers(++num);
                                    transactionRealm.insertOrUpdate(chatRoomDTO);
                                });
                                Intent sendIntent = new Intent(ChatFragment.BROADCAST_RECEIVE_MESSAGE);
                                sendIntent.putExtra("lastRoomNo", lastRoomId);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(sendIntent);
                            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast();
                        }, throwable -> {
                            Log.e(TAG, "Error on subscribe topic at receiver", throwable);
                        });

                compositeDisposable.add(dispTopic);
                mStompClient.connect(headers);
            }
//            Toast.makeText(context, "받은 액션 : " + actionName, Toast.LENGTH_SHORT).show();

            mStompClient.disconnect();
            if (compositeDisposable != null) compositeDisposable.dispose();

    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}

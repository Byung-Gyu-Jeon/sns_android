package com.example.sns.Network;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sns.Model.EchoModel;
import com.example.sns.main.ui.chat.ChatMsgItem;
import com.example.sns.main.ui.chat.ChatroomActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ChatSTOMP {
    private Context context;
    private static final String TAG = "ChatSTOMP";
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private Disposable mRestPingDisposable;
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm", Locale.getDefault());
    private final SimpleDateFormat mTimeFormatText = new SimpleDateFormat("yyyy년 MMM dd일 E요일", Locale.getDefault());
    private Gson mGson = new GsonBuilder().create();

    public ChatSTOMP() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + "218.148.48.242"
                + ":" + "80" + "/sns/stomp-chat/websocket");
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
        Disposable dispTopic = mStompClient.topic("/topic/greetings")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    Log.d(TAG, "Destination " + headers.get(1).getValue());
//                    add(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
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

//    public void sendEchoViaRest(String msg) {
//        mRestPingDisposable = retrofitService
//                .sendRestEcho(msg)
//                .compose(applySchedulers())
//                .subscribe(() -> {
//                    Log.d(TAG, "REST echo send successfully");
//                }, throwable -> {
//                    Log.e(TAG, "Error send REST echo", throwable);
//                    toast(throwable.getMessage());
//                });
//    }

//    private void add(EchoModel echoModel, Ada) {
//        Log.d(TAG, "add 실행 " + echoModel.getEcho());
//        adapter.addItem(new ChatMsgItem(1L, 3L, "hhh", "lyne.2", echoModel.getEcho(), mTimeFormat.format(new Date()), 2 ));
//        adapter.addItem(new ChatMsgItem(1L, 3L, "hhh", "lyne.2", echoModel.getEcho(), mTimeFormat.format(new Date()), 1 ));
//
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
//    }

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

    public void onDestroyResource() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
}

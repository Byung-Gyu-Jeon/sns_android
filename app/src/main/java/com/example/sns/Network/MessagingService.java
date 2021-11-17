package com.example.sns.Network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.ChatMsgDTO;
import com.example.sns.Model.ChatRoomDTO;
import com.example.sns.Model.RealmUser;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.R;
import com.example.sns.main.MainActivity;
import com.example.sns.main.ui.chat.CreateChatroomActivity;
import com.example.sns.main.ui.friend.RecyclerAdapter;
import com.example.sns.main.ui.friend.item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class MessagingService extends FirebaseMessagingService {
    private final static String BASE_URL = "http://59.13.221.12:80/sns/setFcmToken.do/";	// 기본 Base URL
    private RetrofitService retrofitService;
    private RequestResponse requestResponse;

    private static final String[] topics = {"/topics/custom", "/topics/notify"};
    private String token;

    public MessagingService() {
        super();
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        makeNotification(remoteMessage);
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            String msgDTOJson = remoteMessage.getData().get("data");
//            String roomListJson = remoteMessage.getData().get("roomList");
//            Gson gson = new Gson();
//            ChatMsgDTO chatMsgDTO = new ChatMsgDTO();
//            List<ChatRoomDTO> roomDataList = new ArrayList<ChatRoomDTO>();
//            try {
//                Type listType = new TypeToken<ArrayList<ChatRoomDTO>>(){}.getType();
//                roomDataList = gson.fromJson(roomListJson, listType);
//                chatMsgDTO = gson.fromJson(msgDTOJson, ChatMsgDTO.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        if(App.sharedPreferenceManager.getFcmToken() != null)
            sendRegistrationToServer(s);
        App.sharedPreferenceManager.setFcmToken(s);
        Log.d(TAG, "Refreshed token: " + s);
        super.onNewToken(s);
    }

    private void sendRegistrationToServer(String token) {

        retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);
        Call<RequestResponse> sendToken = retrofitService.sendFcmTokenToServer(token, App.sharedPreferenceManager.getUserNo());
        sendToken.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if(response.isSuccessful()){
                    // 정상적으로 통신 성공
                    Log.d(TAG,"통신 성공");

                    requestResponse = response.body();
                    TokenDTO tokenDTO;
                    tokenDTO = requestResponse.getTokenDTO();

                    Log.d("response raw", "response raw : " + response.raw());
                    Log.d("message 및 code : " ,"" + requestResponse.getMessage() + " " + requestResponse.getCode());

                    if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                        App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                        App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                    }else
                        Toast.makeText(getApplicationContext(), "토큰을 저장하지 못했습니다. 앱을 재설치 해주세요.", Toast.LENGTH_SHORT).show();

                }
                else {
                    // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                    Toast.makeText(getApplicationContext(), "인터넷 연결이 원활하지 않습니다.( " + response.code() + " )", Toast.LENGTH_SHORT).show();
                    Log.d("responseFail","Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                Log.d("onFail", "Fail msg : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void makeNotification(RemoteMessage remoteMessage) {
        try {
            int notificationId = -1;
            Context mContext = getApplicationContext();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                String msgDTOJson = remoteMessage.getData().get("data");
                String roomListJson = remoteMessage.getData().get("roomList");
                Gson gson = new Gson();
                ChatMsgDTO chatMsgDTO = new ChatMsgDTO();
                List<ChatRoomDTO> roomDataList = new ArrayList<ChatRoomDTO>();

                Type listType = new TypeToken<ArrayList<ChatRoomDTO>>(){}.getType();
                roomDataList = gson.fromJson(roomListJson, listType);
                chatMsgDTO = gson.fromJson(msgDTOJson, ChatMsgDTO.class);

                for (ChatRoomDTO chatRoomDTO : roomDataList)
                    Log.d(TAG,""+chatRoomDTO.getUserNo());
                Log.d(TAG, "makeNotification: " + chatMsgDTO.getMessage());

//                makeAndSaveChatInfo(chatMsgDTO, roomDataList);
            }



            String title = remoteMessage.getData().get("title");
//            String message = remoteMessage.getData().get("body");
            String message = remoteMessage.getData().get("message");
            String topic = remoteMessage.getFrom();
            Log.d(TAG, "received topic: " + topic);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setVibrate(new long[]{200, 100, 200});
            }
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setContentText(message);

            if (topic.equals(topics[0])) {
                notificationId = 0;
            } else if (topic.equals(topics[1])) {
                notificationId = 1;
            }else
                notificationId = 0;


            if (notificationId >= 0) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(notificationId, builder.build());
            }

        } catch (NullPointerException nullException) {
            Toast.makeText(getApplicationContext(), "알림에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("error Notify", nullException.toString());
        }
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
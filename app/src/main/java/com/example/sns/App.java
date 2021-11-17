package com.example.sns;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sns.Model.SharedPreferenceManager;
import com.example.sns.main.MainActivity;
import com.example.sns.main.ui.chat.ChatBroadCastReceiver;
import com.example.sns.main.ui.chat.CheckBoxData;
import com.example.sns.main.ui.chat.CreateChatroomActivity;
import com.example.sns.main.ui.chat.PickerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ua.naiksoftware.stomp.Stomp;

import static android.content.ContentValues.TAG;

public class App extends Application {
    public static SharedPreferenceManager sharedPreferenceManager;
    public static Realm realm;

    private static final int SCHEMA_V_PREV = 1;// previous schema version
    private static final int SCHEMA_V_NOW = 2;// change schema version if any change happened in schema
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());

        Realm.init(this); // context, usually an Activity or Application
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .name("SNS_project.realm") //생성할 realm파일 이름 지정
                .schemaVersion(SCHEMA_V_NOW)
                .deleteRealmIfMigrationNeeded()// if migration needed then this methoud will remove the existing database and will create new database
                .build();

        //Realm에 셋팅한 정보 값을 지정
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();
        createNotificationChannel(getApplicationContext());
//        registerReceiver();

        super.onCreate();
    }

    private void registerReceiver(){
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if(mReceiver != null) return;
        mReceiver = new ChatBroadCastReceiver();

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ChatBroadCastReceiver.BROADCAST_TOPIC_MESSAGE);
        theFilter.addAction(ChatBroadCastReceiver.BROADCAST_SEND_MESSAGE);
        this.registerReceiver(this.mReceiver, theFilter);
        Log.d("myreceiver", "onReceive: registered");
    }

    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private void createNotificationChannel(Context context) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("10001", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // Configure the notification channel
                notificationChannel.setDescription("Firebase 푸시알림");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{200, 100, 200});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            FirebaseMessaging.getInstance().subscribeToTopic("custom").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"custom topic 구독",Toast.LENGTH_SHORT).show();
                }
            });
            FirebaseMessaging.getInstance().subscribeToTopic("notify").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"notify topic 구독",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 채널 생성에 실패했습니다. 앱을 재실행하거나 재설치해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }
}

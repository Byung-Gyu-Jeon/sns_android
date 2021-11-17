package com.example.sns.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.sns.App;
import com.example.sns.MyToolbar;
import com.example.sns.Network.MessagingService;
import com.example.sns.R;
import com.example.sns.main.ui.chat.ChatBroadCastReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    private StompClient mStompClient;
//    App app;
//    private String token;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Intent intent = getIntent();
//        token = (String)intent.getSerializableExtra("token");

        Bundle bundle = new Bundle();
//        bundle.putString("token",token);

        // 10/12 임시 테스트 채팅
//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + "218.148.48.242"
//                + ":" + "80" + "/sns/stomp-chat/websocket");

        // fcm 서비스 실행
        Intent fcm = new Intent(getApplicationContext(), MessagingService.class);
        startService(fcm);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver();

        // 10/04 임시 툴바 테스트
        MyToolbar.show(this,"SNS",false);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_friend, R.id.navigation_write, R.id.navigation_chat, R.id.navigation_mypage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.mobile_navigation, bundle);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//        Navigation.findNavController(findViewById(R.id.container)).navigate(R.id.nav_host_fragment, bundle);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
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
        theFilter.addAction(ChatBroadCastReceiver.BROADCAST_SEND_MESSAGE);
        theFilter.addAction(ChatBroadCastReceiver.BROADCAST_TOPIC_MESSAGE);
        this.registerReceiver(this.mReceiver, theFilter);
        Log.d("myreceiver", "onReceive: registered");
    }

    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onDestroy() {
        Log.d("myreceiver", "onReceive: unregistered");
        unregisterReceiver();
        super.onDestroy();
    }
}


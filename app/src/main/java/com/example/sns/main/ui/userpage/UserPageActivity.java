package com.example.sns.main.ui.userpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.MyToolbar;
import com.example.sns.R;

public class UserPageActivity extends AppCompatActivity {
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_user_page,null);
        setContentView(view);

        Long userNo = getIntent().getLongExtra("userNo",0);
        Log.d("contentvalues", "받은 유저 번호 : " + userNo);
        ImageView userProfile = findViewById(R.id.imageView2);
        userProfile.setBackground(new ShapeDrawable(new OvalShape()));
        userProfile.setClipToOutline(true);
        TextView userName = findViewById(R.id.textView38);
        TextView introduction = findViewById(R.id.textView39);
        TextView countOfFriend = findViewById(R.id.textView9);
        TextView countOfFeed = findViewById(R.id.textView7);
        Button acceptBtn = findViewById(R.id.accept_button);
        Button requestBtn = findViewById(R.id.request_button);
        Button cancelBtn = findViewById(R.id.cancel_button);
        Button friendBtn = findViewById(R.id.friend_button);

        acceptBtn.setOnClickListener(v -> { viewModel.addFriendAction(v, userNo);});
        requestBtn.setOnClickListener(v -> { viewModel.requestfriendAction(v, userNo);});
        cancelBtn.setOnClickListener(v -> { viewModel.requestfriendAction(v, userNo);});
        friendBtn.setOnClickListener(v -> { viewModel.addFriendAction(v, userNo);});

        RecyclerView recyclerView = findViewById(R.id.recycler2);

        viewModel = new ViewModelProvider(UserPageActivity.this).get(UserViewModel.class);
        viewModel.loadUserInfo(view, recyclerView, userNo);

        viewModel.userImageUrl.observe(this, s -> {
            Glide.with(view.getContext()).load("http://59.13.221.12:80/sns/download?fileName=" + s).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut).into(userProfile);
        });
        viewModel.userName.observe(this, s -> {
            MyToolbar.show(UserPageActivity.this, s,true);
            userName.setText(s);
        });
        viewModel.introduction.observe(this, s -> {introduction.setText(s);});
        viewModel.numberOfFeed.observe(this, s -> {countOfFeed.setText(s);});
        viewModel.numberOfFriend.observe(this, integer -> {countOfFriend.setText(integer.toString());});
        viewModel.acceptButtonEnable.observe(this, aBoolean -> {
            if (aBoolean) {
                acceptBtn.setVisibility(View.VISIBLE);
                acceptBtn.setEnabled(true);
                requestBtn.setVisibility(View.GONE);
                requestBtn.setEnabled(false);
                cancelBtn.setVisibility(View.GONE);
                cancelBtn.setEnabled(false);
                friendBtn.setVisibility(View.GONE);
                friendBtn.setEnabled(false);
//                viewModel.acceptButtonEnable.setValue(false);
                Log.d("ContentValues", "accept.observe 호출");
            }
        });
        viewModel.requestButtonEnable.observe(this, aBoolean -> {
            if (aBoolean) {
                acceptBtn.setVisibility(View.GONE);
                acceptBtn.setEnabled(false);
                requestBtn.setVisibility(View.VISIBLE);
                requestBtn.setEnabled(true);
                cancelBtn.setVisibility(View.GONE);
                cancelBtn.setEnabled(false);
                friendBtn.setVisibility(View.GONE);
                friendBtn.setEnabled(false);
//                viewModel.requestButtonEnable.setValue(false);
                Log.d("ContentValues", "request.observe 호출");
            }
        });
        viewModel.cancelButtonEnable.observe(this, aBoolean -> {
            if (aBoolean) {
                acceptBtn.setVisibility(View.GONE);
                acceptBtn.setEnabled(false);
                requestBtn.setVisibility(View.GONE);
                requestBtn.setEnabled(false);
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setEnabled(true);
                friendBtn.setVisibility(View.GONE);
                friendBtn.setEnabled(false);
//                viewModel.cancelButtonEnable.setValue(false);
                Log.d("ContentValues", "cancel.observe 호출");
            }
        });
        viewModel.friendButtonEnable.observe(this, aBoolean -> {
            if (aBoolean) {
                acceptBtn.setVisibility(View.GONE);
                acceptBtn.setEnabled(false);
                requestBtn.setVisibility(View.GONE);
                requestBtn.setEnabled(false);
                cancelBtn.setVisibility(View.GONE);
                cancelBtn.setEnabled(false);
                friendBtn.setVisibility(View.VISIBLE);
                friendBtn.setEnabled(true);
//                viewModel.friendButtonEnable.setValue(false);
                Log.d("ContentValues", "friend.observe 호출");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chatroom_menu :
                Toast.makeText(this, "Menu click", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home :
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
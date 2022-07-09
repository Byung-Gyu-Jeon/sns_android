package com.example.sns.main.ui.friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.sns.App;
import com.example.sns.GlideApp;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.MyToolbar;
import com.example.sns.R;
import com.example.sns.main.ui.userpage.UserPageActivity;
import com.example.sns.main.ui.userpage.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sns.Network.ApiClient.ourInstance;

public class FriendListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private FriendViewModel viewModel;
    private RecyclerView searchRecyclerView;
    private SearchView friendSearchView;
    private View view;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().from(this).inflate(R.layout.activity_friend_list,null);
        setContentView(view);

        MyToolbar.show(FriendListActivity.this, "내 친구",true);

        friendSearchView = findViewById(R.id.friend_searchview);
        RecyclerView friendRecyclerView = findViewById(R.id.friend_recyclerview);
        searchRecyclerView = findViewById(R.id.search_recyclerview);
        textInfo = findViewById(R.id.text_info);

        friendSearchView.setOnQueryTextListener(this);
        friendSearchView.setOnCloseListener(() -> {
            Log.d(TAG, "onCreate: x button clicked");
//            friendSearchView.setQuery("", false);
            viewModel.enableView(true, viewModel.FRIEND);
            return true;
        });

        LinearLayoutManager friendLinearLayoutManager = new LinearLayoutManager(this);
        friendRecyclerView.setLayoutManager(friendLinearLayoutManager);
        LinearLayoutManager searchLinearLayoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(searchLinearLayoutManager);

        viewModel = new ViewModelProvider(FriendListActivity.this).get(FriendViewModel.class);
        viewModel.loadFriendList(view, friendRecyclerView);

        viewModel.friendListRecyclerViewEnable.observe(this, aBoolean -> {
            if (aBoolean) {
                Log.d(TAG, "onCreate: friendRecyclerview visible");
                friendRecyclerView.setVisibility(View.VISIBLE);
                searchRecyclerView.setVisibility(View.GONE);
                viewModel.enableView(false, viewModel.FRIEND);
            }
        });
        viewModel.searchListRecyclerViewEnable.observe(this, aBoolean -> {
            if (aBoolean) {
                Log.d(TAG, "onCreate: searchRecyclerview visible");
                friendRecyclerView.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);
                viewModel.enableView(false, viewModel.SEARCH);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG ,"검색 단어 : " + newText);
        if(newText.length() > 0) {
            viewModel.search(view, searchRecyclerView, newText);
        } else if (newText.equals("")) {
            Log.d(TAG, "onCreate: x button clicked");
            textInfo.setVisibility(View.GONE);
            viewModel.enableView(true, viewModel.FRIEND);
        }
        return true;
    }
}
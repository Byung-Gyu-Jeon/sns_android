package com.example.sns.main.ui.friend;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import com.example.sns.App;
import com.example.sns.MainLoginActivity;
import com.example.sns.Model.RequestResponse;
import com.example.sns.Model.TokenDTO;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sns.Network.ApiClient.ourInstance;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RequestManager glide;
    private TokenDTO tokenDTO = null;
    Context context;
    ArrayList<item> items = new ArrayList<item>();

    public RecyclerAdapter(Context context, RequestManager glide) {
        this.context = context;
        this.glide = glide;
        tokenDTO = new TokenDTO();
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).viewType == 1)
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0 :
                return new RecyclerAdapter.ViewHolderFriendRequestText(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item_view1,parent,false));
            default :
                return new RecyclerAdapter.ViewHolderFriendRequestList(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item_view2,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        item item = items.get(position);
        Log.d("vindeing ?????? ???","" + item.getUserImageUrl());
        switch (holder.getItemViewType()) {
            case 0 :
                ((RecyclerAdapter.ViewHolderFriendRequestText)holder).setItem(item);
                break;
            default:
                ((RecyclerAdapter.ViewHolderFriendRequestList)holder).setItem(item);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(item item) {
        items.add(item);
        for (item list : items) {
            Log.d("add??? ?????????", "" + list.getUserImageUrl());
        }

    }

    public item getItem(int position) { return items.get(position);}

        public class ViewHolderFriendRequestList extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final String TAG = getClass().getSimpleName();
            private final static String BASE_URL = "http://218.148.48.169:80/sns/actionFriendRequest.do/";	// ?????? Base URL
            private RetrofitService retrofitService;
            private RequestResponse requestResponse;

            Long userNo;
            TextView userName;
            ImageView profileImage;
            Button acceptButton;
            Button deleteButton;

            public ViewHolderFriendRequestList(View itemView) {
                super(itemView);

                retrofitService = ourInstance.getInstance(BASE_URL, true).create(RetrofitService.class);

                userName = itemView.findViewById(R.id.textView1);
                profileImage = itemView.findViewById(R.id.img_profile_friend_request);
                acceptButton = itemView.findViewById(R.id.button1);
                deleteButton = itemView.findViewById(R.id.button2);
            }

            public void setItem(item item) {
                userNo = item.getUserNo();
                Log.d("setItem ?????? ???","" + item.getUserImageUrl());
                userName.setText(item.getUserName());
                acceptButton.setOnClickListener(this);
                deleteButton.setOnClickListener(this);
//                profileImage.setBackground(new ShapeDrawable(new OvalShape()));
//                profileImage.setClipToOutline(true);
//                profileImage.setImageResource(item.getProfileImage());
//                GlideApp.with(context).load(item.getUserImageUrl()).apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation())).into(profileImage);
//                glide.load(item.getUserImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                glide.load("http://218.148.48.169:80/sns/download?fileName="+item.getUserImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Glide loading failed : ","" + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(profileImage);
                Log.d("binding ??? ????????? : ",""+item.getUserName() + "  " + item.getProfileImage());
            }

            @Override
            public void onClick(View view) {
                Log.d("btn","?????? ??????");
                if(view == acceptButton) { //?????? ??????
                    int type = 1;
                    Call<RequestResponse> requestAction = retrofitService.actionRequest(userNo, type);
                    requestAction.enqueue(new Callback<RequestResponse>() {
                        @Override
                        public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                            if (response.isSuccessful()) {
                                requestResponse = response.body();
                                tokenDTO = requestResponse.getTokenDTO();
                                if(tokenDTO.getCode() == 4700) { //?????? ?????? (?????? ?????? ???)
                                    App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                    App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                                }
                                if(requestResponse.getCode() == 4700) { // Action ?????? ??????
                                    Toast.makeText(context, "?????? ??????! ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                                    items.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(),items.size());
//                                      notifyDataSetChanged(); //????????? ????????? ?????? ?????? ex)?????????????????? ??????
                                } else if (requestResponse.getCode() == 4900) { // Action ?????? ??????
                                    Toast.makeText(context, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                } else if (requestResponse.getCode() == 5100) { //spring security principal ?????? ??????
                                    Toast.makeText(context, "????????? ?????????????????????.(principal)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // ?????? ????????? ??????(?????? ?????? 3xx, 4xx ???)
                                Log.d("responseFail","Status Code : " + response.code());
                                Log.d(TAG,response.errorBody().toString());
                                Log.d(TAG,call.request().body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestResponse> call, Throwable t) {
                            // ?????? ??????(????????? ??????, ?????? ?????? ??? ???????????? ??????)
                            Log.d("onFail","Fail msg : " + t.getMessage());
                            Toast.makeText(context, "????????? ????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(view == deleteButton) { //?????? ??????
                    int type = 0;
                    Call<RequestResponse> requestAction = retrofitService.actionRequest(userNo, type);
                    requestAction.enqueue(new Callback<RequestResponse>() {
                        @Override
                        public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                            if (response.isSuccessful()) {
                                requestResponse = response.body();
                                tokenDTO = requestResponse.getTokenDTO();
                                if(tokenDTO.getCode() == 4700) { //?????? ?????? (?????? ?????? ???)
                                    App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                    App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                                }
                                if(requestResponse.getCode() == 4700) { // Action ?????? ??????
                                    Toast.makeText(context, "?????? ??????! ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                                    items.remove(getAdapterPosition());
//                                    notifyItemRemoved(getAdapterPosition());
//                                    notifyItemRangeChanged(getAdapterPosition(),items.size());
                                    notifyDataSetChanged(); //????????? ????????? ?????? ?????? ex)?????????????????? ??????
                                } else if (requestResponse.getCode() == 4900) { // Action ?????? ??????
                                    Toast.makeText(context, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                } else if (requestResponse.getCode() == 5100) { //spring security principal ?????? ??????
                                    Toast.makeText(context, "????????? ?????????????????????.(principal)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // ?????? ????????? ??????(?????? ?????? 3xx, 4xx ???)
                                Log.d("responseFail","Status Code : " + response.code());
                                Log.d(TAG,response.errorBody().toString());
                                Log.d(TAG,call.request().body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestResponse> call, Throwable t) {
                            // ?????? ??????(????????? ??????, ?????? ?????? ??? ???????????? ??????)
                            Log.d("onFail","Fail msg : " + t.getMessage());
                            Toast.makeText(context, "????????? ????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }

        public class ViewHolderFriendRequestText extends RecyclerView.ViewHolder {

            public ViewHolderFriendRequestText(View itemView) {
                super(itemView);
            }

            public void setItem(item item) {
                //   expandedMenuButton.setImageResource(item.getExpandedMenuButton());
            }
        }
}

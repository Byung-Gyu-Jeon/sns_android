package com.example.sns.main.ui.friend;

import android.content.Context;

import android.graphics.drawable.Drawable;
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
        Log.d("vindeing 하기 전","" + item.getUserImageUrl());
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
            Log.d("add된 아이템", "" + list.getUserImageUrl());
        }

    }

    public item getItem(int position) { return items.get(position);}

        public class ViewHolderFriendRequestList extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final String TAG = getClass().getSimpleName();
            private final static String BASE_URL = "http://59.13.221.12:80/sns/actionFriendRequest.do/";	// 기본 Base URL
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

                userName = itemView.findViewById(R.id.user_name_text);
                profileImage = itemView.findViewById(R.id.img_profile_friend_request);
                acceptButton = itemView.findViewById(R.id.button1);
                deleteButton = itemView.findViewById(R.id.button2);
            }

            public void setItem(item item) {
                userNo = item.getUserNo();
                Log.d("setItem 하기 전","" + item.getUserImageUrl());
                userName.setText(item.getUserName());
                acceptButton.setOnClickListener(this);
                deleteButton.setOnClickListener(this);
//                profileImage.setBackground(new ShapeDrawable(new OvalShape()));
//                profileImage.setClipToOutline(true);
//                profileImage.setImageResource(item.getProfileImage());
//                GlideApp.with(context).load(item.getUserImageUrl()).apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation())).into(profileImage);
//                glide.load(item.getUserImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                glide.load("http://59.13.221.12:80/sns/download?fileName="+item.getUserImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
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
                Log.d("binding 된 아이템 : ",""+item.getUserName() + "  " + item.getProfileImage());
            }

            @Override
            public void onClick(View view) {
                Log.d("btn","버튼 누름");
                if(view == acceptButton) { //수락 버튼
                    int type = 1;
                    Call<RequestResponse> requestAction = retrofitService.actionRequest(userNo, type);
                    requestAction.enqueue(new Callback<RequestResponse>() {
                        @Override
                        public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                            if (response.isSuccessful()) {
                                requestResponse = response.body();
                                tokenDTO = requestResponse.getTokenDTO();
                                if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                                    App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                    App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                                }
                                if(requestResponse.getCode() == 4700) { // Action 수행 성공
                                    Toast.makeText(context, "요청 성공! 이제부터 친구입니다.", Toast.LENGTH_SHORT).show();
                                    items.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(),items.size());
//                                      notifyDataSetChanged(); //하나로 지우고 설정 가능 ex)채팅같은경우 쓰임
                                } else if (requestResponse.getCode() == 4900) { // Action 수행 실패
                                    Toast.makeText(context, "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                } else if (requestResponse.getCode() == 5100) { //spring security principal 변환 실패
                                    Toast.makeText(context, "요청에 실패하였습니다.(principal)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                                Log.d("responseFail","Status Code : " + response.code());
                                Log.d(TAG,response.errorBody().toString());
                                Log.d(TAG,call.request().body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestResponse> call, Throwable t) {
                            // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                            Log.d("onFail","Fail msg : " + t.getMessage());
                            Toast.makeText(context, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(view == deleteButton) { //거절 버튼
                    int type = 0;
                    Call<RequestResponse> requestAction = retrofitService.actionRequest(userNo, type);
                    requestAction.enqueue(new Callback<RequestResponse>() {
                        @Override
                        public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                            if (response.isSuccessful()) {
                                requestResponse = response.body();
                                tokenDTO = requestResponse.getTokenDTO();
                                if(tokenDTO.getCode() == 4700) { //인증 성공 (토큰 갱신 ㅇ)
                                    App.sharedPreferenceManager.setToken(tokenDTO.getAccessToken());
                                    App.sharedPreferenceManager.setRefreshToken(tokenDTO.getRefreshToken());
                                }
                                if(requestResponse.getCode() == 4700) { // Action 수행 성공
                                    Toast.makeText(context, "요청 성공! 친구 요청을 거절합니다.", Toast.LENGTH_SHORT).show();
                                    items.remove(getAdapterPosition());
//                                    notifyItemRemoved(getAdapterPosition());
//                                    notifyItemRangeChanged(getAdapterPosition(),items.size());
                                    notifyDataSetChanged(); //하나로 지우고 설정 가능 ex)채팅같은경우 쓰임
                                } else if (requestResponse.getCode() == 4900) { // Action 수행 실패
                                    Toast.makeText(context, "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                } else if (requestResponse.getCode() == 5100) { //spring security principal 변환 실패
                                    Toast.makeText(context, "요청에 실패하였습니다.(principal)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // 통신 실패한 경우(응답 코드 3xx, 4xx 등)
                                Log.d("responseFail","Status Code : " + response.code());
                                Log.d(TAG,response.errorBody().toString());
                                Log.d(TAG,call.request().body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestResponse> call, Throwable t) {
                            // 통신 실패(인터넷 끊김, 예외 발생 등 시스템적 이유)
                            Log.d("onFail","Fail msg : " + t.getMessage());
                            Toast.makeText(context, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
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

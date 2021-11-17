package com.example.sns.main.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sns.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String BROADCAST_MESSAGE = "com.example.sns.main.ui.chat";
    private final RequestManager glide;
    Context context;
    ArrayList<SearchListItem> items = new ArrayList<SearchListItem>();
    ArrayList<CheckBoxData> checkBoxDataList = new ArrayList<CheckBoxData>();
    int checkedNum;

    public SearchListAdapter(Context context, RequestManager glide) {
        this.context = context;
        this.glide = glide;
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
                return new SearchListAdapter.ViewHolderSearchListText(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_chatroom_search_item_view,parent,false));
            default :
                return new SearchListAdapter.ViewHolderSearchList(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_chatroom_itemlist_view,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchListItem item = items.get(position);
        Log.d("vindeing 하기 전","" + item.getUserImageUrl());
        switch (holder.getItemViewType()) {
            case 0 :
                ((SearchListAdapter.ViewHolderSearchListText)holder).setItem(item);
                break;
            default:
                ((SearchListAdapter.ViewHolderSearchList)holder).setItem(item, position);
        }

    }

    @Override
    public int getItemCount() { return items.size(); }

    public int getCheckedNum() {return checkedNum;}

    public List<CheckBoxData> getCheckBoxData() { return checkBoxDataList;}

    public void addItem(SearchListItem item) {
        items.add(item);
        for (SearchListItem list : items) {
            Log.d("add된 아이템", "" + list.getUserImageUrl());
        }
    }

    public SearchListItem getItem(int position) { return items.get(position);}

    public class ViewHolderSearchList extends RecyclerView.ViewHolder {
        Long userNo;
        String pickedName;
        String pickedImageUrl;
        TextView userName;
        ImageView profileImage;
        CheckBox checkBox;
        CheckBoxData checkBoxData;
//        TextView isFriend;

        public ViewHolderSearchList(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.picker_userName);
//            isFriend = itemView.findViewById(R.id.search_list_is_friend);
            profileImage = itemView.findViewById(R.id.picker_profile_img);
            checkBox = itemView.findViewById(R.id.picker_checkBox);

            checkBoxData = new CheckBoxData();
        }

        public void setItem(SearchListItem item, int position) {
            Log.d("setItem 하기 전","" + item.getUserImageUrl());
            userNo = item.getUserNo();
            pickedName = item.getUserName();
            pickedImageUrl = item.getUserImageUrl();
            userName.setText(item.getUserName());
//            if(item.getIsFriend() != null)
//                isFriend.setText(item.getIsFriend());
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
            profileImage.setClipToOutline(true);
//                GlideApp.with(context).load(item.getUserImageUrl()).apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation())).into(profileImage);
//                glide.load(item.getUserImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
            if(item.getUserImageUrl() == null)
                profileImage.setImageResource(R.drawable.doughnut);
            else
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
            Log.d("binding 된 아이템 : ",""+item.getUserName() + "  " + item.getUserImageUrl());


            Intent intent = new Intent(BROADCAST_MESSAGE);

            checkBoxData.setUserNo(userNo);
            checkBoxData.setUserName(pickedName);
            checkBoxData.setImageUrl(pickedImageUrl);
            checkBoxData.setChecked(false);

            if(position >= checkBoxDataList.size())
                checkBoxDataList.add(position - 1, checkBoxData);
            Log.d("체크박스","postion : "+position);

            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()){
                    checkedNum++;
                    checkBoxData.setChecked(true);
                    checkBoxDataList.set(position - 1, checkBoxData);
                    intent.putExtra("value",checkBoxDataList);
                    intent.putExtra("num", checkedNum);
                    context.sendBroadcast(intent);
                    Log.d("체크박스","checked : "+item.getUserName() + "  " + checkBoxDataList.get(position- 1).getChecked());
                    Log.d("체크박스","checked Num : "+ checkedNum);
                }
                else {
                    checkedNum--;
                    checkBoxData.setChecked(false);
                    checkBoxDataList.set(position - 1, checkBoxData);
                    intent.putExtra("value",checkBoxDataList);
                    intent.putExtra("num", checkedNum);
                    context.sendBroadcast(intent);
                    Log.d("체크박스","unchecked : "+item.getUserName() + "  " + checkBoxDataList.get(position - 1).getChecked());
                    Log.d("체크박스","checked Num : "+ checkedNum);
                }
            });

        }
    }

    public class ViewHolderSearchListText extends RecyclerView.ViewHolder {

        public ViewHolderSearchListText(View itemView) {
            super(itemView);
        }

        public void setItem(SearchListItem item) {
            //   expandedMenuButton.setImageResource(item.getExpandedMenuButton());
        }
    }
}

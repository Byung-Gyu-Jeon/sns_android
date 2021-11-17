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

public class PickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String BROADCAST_MESSAGE = "com.example.sns.main.ui.chat";
    private final RequestManager glide;
    Context context;
    ArrayList<CheckBoxData> checkBoxDataList = new ArrayList<CheckBoxData>();
    int checkedNum;

    public PickerAdapter(Context context, RequestManager glide) {
        this.context = context;
        this.glide = glide;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PickerAdapter.ViewHolderPickerList(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_picker_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CheckBoxData item = checkBoxDataList.get(position);
        ((PickerAdapter.ViewHolderPickerList)holder).setItem(item, position);
    }

    @Override
    public int getItemCount() { return checkBoxDataList.size(); }

    public void addItem(CheckBoxData item) { checkBoxDataList.add(item); }

    public class ViewHolderPickerList extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        ImageView profileImage;
        CheckBoxData checkBoxData;

        public ViewHolderPickerList(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.picker_user_name);
            profileImage = itemView.findViewById(R.id.picker_profile_img);
            checkBoxData = new CheckBoxData();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BROADCAST_MESSAGE);

        }

        public void setItem(CheckBoxData item, int position) {
            userName.setText(item.getUserName());
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
            profileImage.setClipToOutline(true);
//                GlideApp.with(context).load(item.getUserImageUrl()).apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation())).into(profileImage);
//                glide.load(item.getUserImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
            if(item.getImageUrl() == null)
                profileImage.setImageResource(R.drawable.doughnut);
            else
                glide.load("http://59.13.221.12:80/sns/download?fileName="+item.getImageUrl()).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
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
            Log.d("binding 된 아이템 : ",""+item.getUserName() + "  " + item.getImageUrl());

//            checkBoxData.setUserNo(userNo);
//            checkBoxData.setUserName(pickedName);
//            checkBoxData.setImageUrl(pickedImageUrl);
//            checkBoxData.setChecked(false);
//
//            if(position >= checkBoxDataList.size())
//                checkBoxDataList.add(position - 1, checkBoxData);
//            Log.d("체크박스","postion : "+position);

//            checkBox.setOnClickListener(v -> {
//                if (checkBox.isChecked()){
//                    checkedNum++;
//                    checkBoxData.setChecked(true);
//                    checkBoxDataList.set(position - 1, checkBoxData);
//                    intent.putExtra("value",checkBoxDataList);
//                    intent.putExtra("num", checkedNum);
//                    context.sendBroadcast(intent);
//                    Log.d("체크박스","checked : "+item.getUserName() + "  " + checkBoxDataList.get(position- 1).getChecked());
//                    Log.d("체크박스","checked Num : "+ checkedNum);
//                }
//                else {
//                    checkedNum--;
//                    checkBoxData.setChecked(false);
//                    checkBoxDataList.set(position - 1, checkBoxData);
//                    intent.putExtra("value",checkBoxDataList);
//                    intent.putExtra("num", checkedNum);
//                    context.sendBroadcast(intent);
//                    Log.d("체크박스","unchecked : "+item.getUserName() + "  " + checkBoxDataList.get(position - 1).getChecked());
//                    Log.d("체크박스","checked Num : "+ checkedNum);
//                }
//            });

        }
    }
}

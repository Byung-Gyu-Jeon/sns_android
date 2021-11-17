package com.example.sns.main.ui.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.jetbrains.annotations.Nullable;

import me.relex.circleindicator.CircleIndicator;

public class ChatMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RequestManager glide;
    Context context;
    ArrayList<ChatMsgItem> items = new ArrayList<ChatMsgItem>();

    public ChatMsgAdapter(RequestManager glide, Context context) {
        this.glide = glide;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).viewType == 1)
            return 1;
        else if(items.get(position).viewType == 2)
            return 2;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0 :
                return new ChatMsgAdapter.ViewHolderDayText(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_center_item_view,parent,false));
            case 1 :
                return new ChatMsgAdapter.ViewHolderLeftChatMsg(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_left_item_view,parent,false));
            default :
                return new ChatMsgAdapter.ViewHolderRightChatMsg(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_right_item_view,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMsgItem item = items.get(position);
        switch (holder.getItemViewType()) {
            case 0 :
                ((ChatMsgAdapter.ViewHolderDayText)holder).setItem(item);
                break;
            case 1 :
                ((ChatMsgAdapter.ViewHolderLeftChatMsg)holder).setItem(item);
                break;
            default:
                ((ChatMsgAdapter.ViewHolderRightChatMsg)holder).setItem(item);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(ChatMsgItem item) { items.add(item); }

    public class ViewHolderLeftChatMsg extends RecyclerView.ViewHolder {
        String roomNo;
        Long userNo;
        TextView userName;
        ImageView profileImage;
        TextView receivedMsg;
        TextView receivedMsgTime;

        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm", Locale.getDefault());

        public ViewHolderLeftChatMsg(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.chatroom_left_username);
            profileImage = itemView.findViewById(R.id.chatroom_left_profile);
            receivedMsg = itemView.findViewById(R.id.chatroom_left_msg);
            receivedMsgTime = itemView.findViewById(R.id.chatroom_left_time);
        }

        public void setItem(ChatMsgItem item){
            roomNo = item.getRoomNo();
            userNo = item.getUserNo();
            userName.setText(item.getUserName());
            receivedMsg.setText(item.getMessage());
            receivedMsgTime.setText(item.getTime());
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
            profileImage.setClipToOutline(true);
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

        }
    }

    public class ViewHolderRightChatMsg extends RecyclerView.ViewHolder {
        String roomNo;
        Long userNo;
        TextView userName;
        ImageView profileImage;
        TextView receivedMsg;
        TextView receivedMsgTime;

        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault());

        public ViewHolderRightChatMsg(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.chatroom_right_username);
            profileImage = itemView.findViewById(R.id.chatroom_right_profile);
            receivedMsg = itemView.findViewById(R.id.chatroom_right_msg);
            receivedMsgTime = itemView.findViewById(R.id.chatroom_right_time);
        }

        public void setItem(ChatMsgItem item){
            roomNo = item.getRoomNo();
            userNo = item.getUserNo();
            userName.setText(item.getUserName());
            receivedMsg.setText(item.getMessage());
            receivedMsgTime.setText(item.getTime());
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
            profileImage.setClipToOutline(true);
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

        }

    }

    public class ViewHolderDayText extends RecyclerView.ViewHolder {
        TextView dayText;

        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("yyyy년 MMM dd일 E요일", Locale.getDefault());

        public ViewHolderDayText(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.day_textview);
        }

        public void setItem(ChatMsgItem item){
            dayText.setText(item.getTime());
        }

    }
}

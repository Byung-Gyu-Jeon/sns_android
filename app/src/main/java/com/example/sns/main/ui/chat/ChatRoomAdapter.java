package com.example.sns.main.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ChatRoomAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RequestManager glide;
    private OnItemClickEventListener mItemClickListener;
    private OnItemLongClickEventListener mItemLongClickListener;
    private int numberOfUsers;
    Context context;
    ArrayList<ChatRoomItem> items = new ArrayList<ChatRoomItem>();

    public ChatRoomAdapter(RequestManager glide, Context context) {
        this.glide = glide;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        numberOfUsers = items.get(position).viewType + 1;
        if(items.get(position).viewType == 1)
            return 1;
        else if (items.get(position).viewType == 2)
            return 2;
        else if (items.get(position).viewType == 3)
            return 3;
        else
            return 4;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1 :
                return new ChatRoomAdapter.ViewHolderChatRoom1(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_item_view,parent, false),mItemClickListener, mItemLongClickListener);
            case 2 :
                return new ChatRoomAdapter.ViewHolderChatRoom2(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_item_view2,parent, false),mItemClickListener, mItemLongClickListener);
            case 3:
                return new ChatRoomAdapter.ViewHolderChatRoom3(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_item_view3,parent, false),mItemClickListener, mItemLongClickListener);
            default :
                return new ChatRoomAdapter.ViewHolderChatRoom4(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_item_view4,parent, false),mItemClickListener, mItemLongClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRoomItem item = items.get(position);
        switch (holder.getItemViewType()) {
            case 1 :
                ((ChatRoomAdapter.ViewHolderChatRoom1)holder).setItem(item);
                break;
            case 2 :
                ((ChatRoomAdapter.ViewHolderChatRoom2)holder).setItem(item);
                break;
            case 3 :
                ((ChatRoomAdapter.ViewHolderChatRoom3)holder).setItem(item);
                break;
            default:
                ((ChatRoomAdapter.ViewHolderChatRoom4)holder).setItem(item);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(ChatRoomItem item) { items.add(item); }

    public ArrayList<ChatRoomItem> getItem() {return items;}

    public class ViewHolderChatRoom1 extends RecyclerView.ViewHolder {
        String roomNo;
        TextView roomName;
        List<Long> userNo;
//        ImageView profileImage;
        List<ImageView> profileImage;
        TextView participantNumbers;
        TextView receivedMsg;
        TextView receivedMsgTime;
        TextView receivedMsgNumbers;

//        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault());

        public ViewHolderChatRoom1(@NonNull View itemView, final OnItemClickEventListener itemClickListener, final OnItemLongClickEventListener itemLongClickListener)  {
            super(itemView);
            int userNum = numberOfUsers;
            userNo = new ArrayList<>();
            profileImage = new ArrayList<>(userNum - 1);
//            ((ArrayList<ImageView>) profileImage).ensureCapacity(userNum - 1);
//            profileImage = new ArrayList<>();


            roomName = itemView.findViewById(R.id.user_name_text);
            Log.d("chatroomadapter", "뷰홀더 생성자 호출1 : " + userNum);

//            int cnt = 1;
            for (int cnt = 0; cnt < userNum - 1; cnt++) {
                String imageViewID = "chat_profile_img" + (cnt + 1);
                int resID = itemView.getResources().getIdentifier(imageViewID, "id", context.getPackageName());
                ImageView imageView = itemView.findViewById(resID);
                imageView.setBackground(new ShapeDrawable(new OvalShape()));
                imageView.setClipToOutline(true);
                profileImage.add(imageView);
                Log.d("chatroomadapter", "resID1 : " + profileImage.get(cnt));
            }
//            profileImage = itemView.findViewById(R.id.chat_profile_img);
            participantNumbers = itemView.findViewById(R.id.participant_numbers_text);
            receivedMsg = itemView.findViewById(R.id.chat_content_text);
            receivedMsgTime = itemView.findViewById(R.id.time_text);
            receivedMsgNumbers = itemView.findViewById(R.id.chat_numbers_received_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemLongClickListener.onItemLongClick(view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(ChatRoomItem item) {
            roomNo = item.getRoomNo();
            roomName.setText(item.getRoomName());
            userNo = item.getUserNo();
            if (item.getParticipantNumbers() >= 3)
                participantNumbers.setText(Integer.toString(item.getParticipantNumbers()));
            else
                participantNumbers.setVisibility(View.GONE);
            if (item.getReceivedMsg() == null)
                receivedMsg.setText("상대방과 채팅을 시작해보세요!");
            else
                receivedMsg.setText(item.getReceivedMsg());
            receivedMsgTime.setText(item.getReceivedMsgTime());
//            receivedMsgTime.setText(mTimeFormat.format(new Date()));
            if (item.getReceivedMsgNumbers() >= 1)
                receivedMsgNumbers.setText(Integer.toString(item.getReceivedMsgNumbers()));
            else if (item.getReceivedMsg() == null)
                receivedMsgNumbers.setText("New!");
            else
                receivedMsgNumbers.setVisibility(View.INVISIBLE);
            int cnt = 0;
            for (ImageView imageView : profileImage) {
                Log.d("chatroomadapter", "image name : " + item.getUserImageUrl().get(cnt));
                glide.load("http://59.13.221.12:80/sns/download?fileName=" + item.getUserImageUrl().get(cnt)).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Glide loading failed : ", "" + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(imageView);
                cnt++;
            }
        }
    }

    public class ViewHolderChatRoom2 extends RecyclerView.ViewHolder {
        String roomNo;
        TextView roomName;
        List<Long> userNo;
        List<ImageView> profileImage;
        TextView participantNumbers;
        TextView receivedMsg;
        TextView receivedMsgTime;
        TextView receivedMsgNumbers;

//        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault());

        public ViewHolderChatRoom2(@NonNull View itemView, final OnItemClickEventListener itemClickListener, final OnItemLongClickEventListener itemLongClickListener) {
            super(itemView);
            int userNum = numberOfUsers;
            userNo = new ArrayList<>();
            profileImage = new ArrayList<>(userNum - 1);

            roomName = itemView.findViewById(R.id.user_name_text);
            Log.d("chatroomadapter", "뷰홀더 생성자 호출2 : " + userNum);
            for (int cnt = 0; cnt < userNum - 1; cnt++) {
                String imageViewID = "chat_profile_img" + (cnt + 1);
                int resID = itemView.getResources().getIdentifier(imageViewID, "id", context.getPackageName());
                ImageView imageView = itemView.findViewById(resID);
                imageView.setBackground(new ShapeDrawable(new OvalShape()));
                imageView.setClipToOutline(true);
                profileImage.add(imageView);
                Log.d("chatroomadapter", "cnt2 : " + cnt);
                Log.d("chatroomadapter", "resID2 : " + profileImage.get(cnt));
            }
//            profileImage = itemView.findViewById(R.id.chat_profile_img);
            participantNumbers = itemView.findViewById(R.id.participant_numbers_text);
            receivedMsg = itemView.findViewById(R.id.chat_content_text);
            receivedMsgTime = itemView.findViewById(R.id.time_text);
            receivedMsgNumbers = itemView.findViewById(R.id.chat_numbers_received_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemLongClickListener.onItemLongClick(view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(ChatRoomItem item) {
            roomNo = item.getRoomNo();
            roomName.setText(item.getRoomName());
            userNo = item.getUserNo();
            if (item.getParticipantNumbers() >= 3)
                participantNumbers.setText(Integer.toString(item.getParticipantNumbers()));
            else
                participantNumbers.setVisibility(View.GONE);
            if (item.getReceivedMsg() == null)
                receivedMsg.setText("상대방과 채팅을 시작해보세요!");
            else
                receivedMsg.setText(item.getReceivedMsg());
            receivedMsgTime.setText(item.getReceivedMsgTime());
//            receivedMsgTime.setText(mTimeFormat.format(new Date()));
            if (item.getReceivedMsgNumbers() >= 1)
                receivedMsgNumbers.setText(Integer.toString(item.getReceivedMsgNumbers()));
            else if (item.getReceivedMsg() == null)
                receivedMsgNumbers.setText("New!");
            else
                receivedMsgNumbers.setVisibility(View.INVISIBLE);
            int cnt = 0;
            for (ImageView imageView : profileImage) {
                Log.d("chatroomadapter", "image name : " + item.getUserImageUrl().get(cnt));
                glide.load("http://59.13.221.12:80/sns/download?fileName=" + item.getUserImageUrl().get(cnt)).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Glide loading failed : ", "" + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(imageView);
                cnt++;
            }
        }
    }

    public class ViewHolderChatRoom3 extends RecyclerView.ViewHolder {
        String roomNo;
        TextView roomName;
        List<Long> userNo;
        //        ImageView profileImage;
        List<ImageView> profileImage;
        TextView participantNumbers;
        TextView receivedMsg;
        TextView receivedMsgTime;
        TextView receivedMsgNumbers;

//        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault());

        public ViewHolderChatRoom3(@NonNull View itemView, final OnItemClickEventListener itemClickListener, final OnItemLongClickEventListener itemLongClickListener) {
            super(itemView);
            int userNum = numberOfUsers;
            userNo = new ArrayList<>();
            profileImage = new ArrayList<>(userNum - 1);

            roomName = itemView.findViewById(R.id.user_name_text);
            Log.d("chatroomadapter", "뷰홀더 생성자 호출3 : " + userNum);
            for (int cnt = 0; cnt < userNum - 1; cnt++) {
                String imageViewID = "chat_profile_img" + (cnt + 1);
                int resID = itemView.getResources().getIdentifier(imageViewID, "id", context.getPackageName());
                ImageView imageView = itemView.findViewById(resID);
                imageView.setBackground(new ShapeDrawable(new OvalShape()));
                imageView.setClipToOutline(true);
                profileImage.add(imageView);
                Log.d("chatroomadapter", "cnt3 : " + cnt);
                Log.d("chatroomadapter", "resID3 : " + profileImage.get(cnt));
            }
//            profileImage = itemView.findViewById(R.id.chat_profile_img);
            participantNumbers = itemView.findViewById(R.id.participant_numbers_text);
            receivedMsg = itemView.findViewById(R.id.chat_content_text);
            receivedMsgTime = itemView.findViewById(R.id.time_text);
            receivedMsgNumbers = itemView.findViewById(R.id.chat_numbers_received_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemLongClickListener.onItemLongClick(view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(ChatRoomItem item) {
            roomNo = item.getRoomNo();
            roomName.setText(item.getRoomName());
            userNo = item.getUserNo();
            if (item.getParticipantNumbers() >= 3)
                participantNumbers.setText(Integer.toString(item.getParticipantNumbers()));
            else
                participantNumbers.setVisibility(View.GONE);
            if (item.getReceivedMsg() == null)
                receivedMsg.setText("상대방과 채팅을 시작해보세요!");
            else
                receivedMsg.setText(item.getReceivedMsg());
            receivedMsgTime.setText(item.getReceivedMsgTime());
//            receivedMsgTime.setText(mTimeFormat.format(new Date()));
            if (item.getReceivedMsgNumbers() >= 1)
                receivedMsgNumbers.setText(Integer.toString(item.getReceivedMsgNumbers()));
            else if (item.getReceivedMsg() == null)
                receivedMsgNumbers.setText("New!");
            else
                receivedMsgNumbers.setVisibility(View.INVISIBLE);
            int cnt = 0;
            for (ImageView imageView : profileImage) {
                Log.d("chatroomadapter", "image name : " + item.getUserImageUrl().get(cnt));
                glide.load("http://59.13.221.12:80/sns/download?fileName=" + item.getUserImageUrl().get(cnt)).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Glide loading failed : ", "" + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(imageView);
                cnt++;
            }
        }
    }

    public class ViewHolderChatRoom4 extends RecyclerView.ViewHolder {
        String roomNo;
        TextView roomName;
        List<Long> userNo;
        //        ImageView profileImage;
        List<ImageView> profileImage;
        TextView participantNumbers;
        TextView receivedMsg;
        TextView receivedMsgTime;
        TextView receivedMsgNumbers;

//        private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("a hh:mm:ss", Locale.getDefault());

        public ViewHolderChatRoom4(@NonNull View itemView, final OnItemClickEventListener itemClickListener, final OnItemLongClickEventListener itemLongClickListener) {
            super(itemView);
            int userNum = numberOfUsers;
            userNo = new ArrayList<>();
            profileImage = new ArrayList<>(userNum - 1);

            roomName = itemView.findViewById(R.id.user_name_text);
            Log.d("chatroomadapter", "뷰홀더 생성자 호출4 : " + userNum);
            for (int cnt = 0; cnt < userNum - 1; cnt++) {
                String imageViewID = "chat_profile_img" + (cnt + 1);
                int resID = itemView.getResources().getIdentifier(imageViewID, "id", context.getPackageName());
                ImageView imageView = itemView.findViewById(resID);
                imageView.setBackground(new ShapeDrawable(new OvalShape()));
                imageView.setClipToOutline(true);
                profileImage.add(imageView);
                Log.d("chatroomadapter", "cnt4 : " + cnt);
                Log.d("chatroomadapter", "resID4 : " + profileImage.get(cnt));
            }
//            profileImage = itemView.findViewById(R.id.chat_profile_img);
            participantNumbers = itemView.findViewById(R.id.participant_numbers_text);
            receivedMsg = itemView.findViewById(R.id.chat_content_text);
            receivedMsgTime = itemView.findViewById(R.id.time_text);
            receivedMsgNumbers = itemView.findViewById(R.id.chat_numbers_received_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemLongClickListener.onItemLongClick(view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(ChatRoomItem item) {
            roomNo = item.getRoomNo();
            roomName.setText(item.getRoomName());
            userNo = item.getUserNo();
            if (item.getParticipantNumbers() >= 3)
                participantNumbers.setText(Integer.toString(item.getParticipantNumbers()));
            else
                participantNumbers.setVisibility(View.GONE);
            if (item.getReceivedMsg() == null)
                receivedMsg.setText("상대방과 채팅을 시작해보세요!");
            else
                receivedMsg.setText(item.getReceivedMsg());
            receivedMsgTime.setText(item.getReceivedMsgTime());
//            receivedMsgTime.setText(mTimeFormat.format(new Date()));
            if (item.getReceivedMsgNumbers() >= 1)
                receivedMsgNumbers.setText(Integer.toString(item.getReceivedMsgNumbers()));
            else if (item.getReceivedMsg() == null)
                receivedMsgNumbers.setText("New!");
            else
                receivedMsgNumbers.setVisibility(View.INVISIBLE);
            int cnt = 0;
            for (ImageView imageView : profileImage) {
                Log.d("chatroomadapter", "image name : " + item.getUserImageUrl().get(cnt));
                glide.load("http://59.13.221.12:80/sns/download?fileName=" + item.getUserImageUrl().get(cnt)).apply(new RequestOptions().circleCrop()).error(R.drawable.doughnut)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("Glide loading failed : ", "" + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(imageView);
                cnt++;
            }
        }
    }

    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        mItemClickListener = listener;
    }

    public interface OnItemLongClickEventListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickEventListener listener) {
        mItemLongClickListener = listener;
    }
}

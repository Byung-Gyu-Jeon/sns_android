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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sns.App;
import com.example.sns.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SearchRecyclerAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RequestManager glide;
    private OnItemClickEventListener mItemClickListener;
    Context context;
    ArrayList<item> items = new ArrayList<item>();

    public SearchRecyclerAdapter(Context context, RequestManager glide) {
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
                return new SearchRecyclerAdapter.ViewHolderFriendListText(LayoutInflater.from(parent.getContext()).inflate(R.layout.frined_serach_text_view,parent,false));
            default :
                return new SearchRecyclerAdapter.ViewHolderFriendSearchList(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_search_item_view,parent,false), mItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        item item = items.get(position);
        Log.d("vindeing 하기 전","" + item.getUserImageUrl());
        switch (holder.getItemViewType()) {
            case 0 :
                ((SearchRecyclerAdapter.ViewHolderFriendListText)holder).setItem(item);
                break;
            default:
                ((SearchRecyclerAdapter.ViewHolderFriendSearchList)holder).setItem(item);
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
    public ArrayList<item> getItem() { return items;}

    public class ViewHolderFriendSearchList extends RecyclerView.ViewHolder {
        Long userNo;
        TextView userName;
        ImageView profileImage;
        TextView isFriend;

        public ViewHolderFriendSearchList(View itemView, final OnItemClickEventListener itemClickListener) {
            super(itemView);

            userName = itemView.findViewById(R.id.search_list_name);
            isFriend = itemView.findViewById(R.id.search_list_is_friend);
            profileImage = itemView.findViewById(R.id.img_profile_friend_search);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });
        }

        public void setItem(item item) {
            Log.d("setItem 하기 전","" + item.getUserImageUrl());
            userNo = item.getUserNo();
            userName.setText(item.getUserName());
            if(item.getIsFriend() != null)
                isFriend.setText(item.getIsFriend());
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
            Log.d("binding 된 아이템 : ",""+item.getUserName() + "  " + item.getProfileImage());
        }
    }

    public class ViewHolderFriendListText extends RecyclerView.ViewHolder {

        public ViewHolderFriendListText(View itemView) {
            super(itemView);
        }

        public void setItem(item item) {
            //   expandedMenuButton.setImageResource(item.getExpandedMenuButton());
        }
    }

    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        mItemClickListener = listener;
    }
}

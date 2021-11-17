package com.example.sns.main.ui.feed;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.FeedimagelistDATA;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class recyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<item> items = new ArrayList<item>();

    public recyclerviewAdapter(Context context) {
        this.context = context;
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
                return new ViewHolderOnePhoto(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false));
            default :
                return new ViewHolderMultiplePhotos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view2,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        item item = items.get(position);
        switch (holder.getItemViewType()) {
            case 0 :
                ((ViewHolderOnePhoto)holder).setItem(item);
                break;
            default:
                ((ViewHolderMultiplePhotos)holder).setItem(item);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(item item) {
        items.add(item);
    }

    public item getItem(int position) {
        return items.get(position);
    }

    public class ViewHolderMultiplePhotos extends RecyclerView.ViewHolder {
        TextView userName;
        TextView textContents;
        ImageView profileImage;
        TextView photoOrderText;
        ImageContentsViewPager imageContentsViewPager;
        CircleIndicator circleIndicator;
        ImageButton expandedMenuButton;
        Button likeButton;
        Button commentsButton;
        Button shareButton;

        ViewPageAdapter viewPageAdapter;
        ViewPageChangeListener viewPageChangeListener;
        Handler handler;
        Animation fadeOut;

        public ViewHolderMultiplePhotos(View itemView) {
            super(itemView);

            imageContentsViewPager = itemView.findViewById(R.id.photo_viewpager);
            circleIndicator = itemView.findViewById(R.id.photo_indicator);
            fadeOut = AnimationUtils.loadAnimation(context,R.anim.fade_out);

            userName = itemView.findViewById(R.id.user_name);
            textContents = itemView.findViewById(R.id.text_contents);
            profileImage = itemView.findViewById(R.id.img_profile);
            photoOrderText = itemView.findViewById(R.id.photo_order_text);
            expandedMenuButton = itemView.findViewById(R.id.expanded_menuButton);
            likeButton = itemView.findViewById(R.id.like_button);
            commentsButton = itemView.findViewById(R.id.comments_button);
            shareButton = itemView.findViewById(R.id.share_button);

            handler = new Handler(Looper.getMainLooper());
        }

        public void setItem(item item) {

            viewPageAdapter = new ViewPageAdapter(context,item);

            userName.setText(item.getUserName());
            textContents.setText(item.getTextContents());
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
            profileImage.setClipToOutline(true);
            //프로필 사진 gilde로 불러오기
            Glide.with(context).load("http://192.168.0.2:8080/sns/download?fileName="+item.getProfileImage()).apply(new RequestOptions().circleCrop()).into(profileImage);
            imageContentsViewPager.setAdapter(viewPageAdapter);
            circleIndicator.setViewPager(imageContentsViewPager);
            viewPageChangeListener = new ViewPageChangeListener();
            imageContentsViewPager.addOnPageChangeListener(viewPageChangeListener);
            //   expandedMenuButton.setImageResource(item.getExpandedMenuButton());

            photoOrderText.setText("1/" + viewPageAdapter.getCount());
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override public void run() {
                    photoOrderText.setVisibility(View.GONE);
                    photoOrderText.startAnimation(fadeOut);
                }
            }, 2000);
            handler.removeCallbacksAndMessages(null);
        }

        public class ViewPageChangeListener implements ViewPager.OnPageChangeListener {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // onpagescrolled 시작시 textview가 보여야 함
                // 일정 시간흐르면 textview가 사라져야 함

                if(positionOffset == 0.0) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override public void run() {
                            photoOrderText.setVisibility(View.GONE);
                            photoOrderText.startAnimation(fadeOut);
                        }
                    }, 2000);
                }
                else {
                     photoOrderText.setVisibility(View.VISIBLE);
                     handler.removeCallbacksAndMessages(null);
                }

                Log.e("scrooled",position + "/" + viewPageAdapter.getCount());
                Log.e("offset", positionOffset + "");
                Log.e("pix",positionOffsetPixels + "");
            }

            @Override
            public void onPageSelected(int position) {
                photoOrderText.setText(position + 1 + "/" + viewPageAdapter.getCount());
                Log.e("scrooled2",position + "/" + viewPageAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("scrooled3",state + "/" + viewPageAdapter.getCount());
            }
        }
    }
    // context menu를 사용하려면 RecyclerView.ViewHolder를 상속받은 클래스에서 OnCreateContextMenuListener를 구현해야함
    public class ViewHolderOnePhoto extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView userName;
        TextView textContents;
        ImageView profileImage;
        ImageView imageContents;
        ImageButton expandedMenuButton;
        Button likeButton;
        Button commentsButton;
        Button shareButton;

        public ViewHolderOnePhoto(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            textContents = itemView.findViewById(R.id.text_contents);
            profileImage = itemView.findViewById(R.id.img_profile);
            imageContents = itemView.findViewById(R.id.img_contents);
            expandedMenuButton = itemView.findViewById(R.id.expanded_menuButton);
            likeButton = itemView.findViewById(R.id.like_button);
            commentsButton = itemView.findViewById(R.id.comments_button);
            shareButton = itemView.findViewById(R.id.share_button);

            itemView.setOnCreateContextMenuListener(this); //리스너를 현재 클래스에서 구현한다고 설정

        }

        //contextmenu를 생성하고 메뉴항목선택시 호출되는 리스너 등록 ID 1001, 1002로  어떤 메뉴를 선택했는지 리스너에서 구분함
        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        MenuItem Edit = menu.add(Menu.NONE,1001,1,"수정");
                        MenuItem Delete = menu.add(Menu.NONE,1002,2,"삭제");
                        Edit.setOnMenuItemClickListener(onMenu);
                        Delete.setOnMenuItemClickListener(onMenu);
                    }

                    private final MenuItem.OnMenuItemClickListener onMenu = new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case 1001 :
                                    Intent intent = new Intent(context, ModifyFeedActivity.class);
                                    context.startActivity(intent);
                                    break;
                                case 1002 :
                                    items.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(),items.size());
                                    //  notifyDataSetChanged(); 하나로 지우고 설정 가능 ex)채팅같은경우 쓰임

                        break;
                }
                return true;
            }
        };

        public void setItem(item item) {
            userName.setText(item.getUserName());
            textContents.setText(item.getTextContents());
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
            profileImage.setClipToOutline(true);
            //프로필 사진 gilde로 불러오기
            Glide.with(context).load("http://192.168.0.2:8080/sns/download?fileName="+item.getProfileImage()).apply(new RequestOptions().circleCrop()).into(profileImage);
            //메인 사진화면에 나타낼 사진이 1개일때 List형태로 저장된 사진을 꺼낸다
            List<FeedimagelistDATA> imageContentss= item.getImageContents();
            Glide.with(context).load("http://192.168.0.2:8080/sns/download2?fileName="+imageContentss.get(0).getImagename()).into(imageContents);
         //   expandedMenuButton.setImageResource(item.getExpandedMenuButton());
        }
    }
}

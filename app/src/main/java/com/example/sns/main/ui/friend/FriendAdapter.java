package com.example.sns.main.ui.friend;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sns.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import static android.content.ContentValues.TAG;

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RequestManager glide;
    private RecyclerAdapter.OnItemClickEventListener mItemClickListener;
    private OnMenuItemClickEventListener mMenuItemClickListener;
    Context context;
    private MenuInflater menuInflater;
    ArrayList<item> items = new ArrayList<item>();

    public FriendAdapter(RequestManager glide, Context context) {
        this.glide = glide;
        this.context = context;
        this.menuInflater = new MenuInflater(context);
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
                return new FriendAdapter.ViewHolderFriendText(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friend_list_text,parent,false));
            default :
                return new FriendAdapter.ViewHolderFriendList(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friend_list_item_view,parent,false), mItemClickListener, mMenuItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        item item = items.get(position);
        Log.d("vindeing 하기 전","" + item.getUserImageUrl());
        switch (holder.getItemViewType()) {
            case 0 :
                ((FriendAdapter.ViewHolderFriendText)holder).setItem(item);
                break;
            default:
                ((FriendAdapter.ViewHolderFriendList)holder).setItem(item);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(item item) {
        items.add(item);
        for (item list : items) {
            Log.d(TAG, "add된 아이템 : " + list.getUserImageUrl());
        }
    }
    public item getItem(int position) { return items.get(position);}
    public ArrayList<item> getItem() { return items;}

    public class ViewHolderFriendList extends RecyclerView.ViewHolder {
        Long userNo;
        TextView userName;
        ImageView profileImage;
        ImageButton menuBtn;

        PopupMenu popup;
        OnMenuItemClickListener listener;

        public ViewHolderFriendList(View itemView, final RecyclerAdapter.OnItemClickEventListener itemClickListener, final OnMenuItemClickEventListener popUpMenuClickEventListener) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name_text);
            profileImage = itemView.findViewById(R.id.user_image);
            menuBtn = itemView.findViewById(R.id.menu_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(view, position);
                    }
                }
            });

            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.show();//Popup Menu 보이기
                }
            });

            //Popup Menu의 MenuItem을 클릭하는 것을 감지하는 listener 객체 생성
            //import android.widget.PopupMenu.OnMenuItemClickListener 가 되어있어야 합니다.
            //OnMenuItemClickListener 클래스는 다른 패키지에도 많기 때문에 PopupMenu에 반응하는 패키지를 임포트하셔야 합니다.
            listener = new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        switch (item.getItemId()) {//눌러진 MenuItem의 Item Id를 얻어와 식별
                            case R.id.unfriend:
                                popUpMenuClickEventListener.onMenuItemClick(position);
                                break;
                        }
                    }
                    return false;
                }
            };
        }

        public void setItem(item item) {
            userNo = item.getUserNo();
            userName.setText(item.getUserName());
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
            //PopupMenu객체 생성.
            //생성자함수의 첫번재 파라미터 : Context
            //생성자함수의 두번째 파라미터 : Popup Menu를 붙일 anchor 뷰
            popup = new PopupMenu(context, menuBtn);//v는 클릭된 뷰를 의미

            //Popup Menu에 들어갈 MenuItem 추가.
            //이전 포스트의 컨텍스트 메뉴(Context menu)처럼 xml 메뉴 리소스 사용
            //첫번재 파라미터 : res폴더>>menu폴더>>mainmenu.xml파일 리소스
            //두번재 파라미터 : Menu 객체->Popup Menu 객체로 부터 Menu 객체 얻어오기
            menuInflater.inflate(R.menu.user_edit_menu, popup.getMenu());

            //Popup Menu의 MenuItem을 클릭하는 것을 감지하는 listener 설정
            popup.setOnMenuItemClickListener(listener);
        }
    }

    public class ViewHolderFriendText extends RecyclerView.ViewHolder {
        TextView numberOfFriend;

        public ViewHolderFriendText(View itemView) {
            super(itemView);

            numberOfFriend = itemView.findViewById(R.id.numberOfFriend_text);
        }

        public void setItem(item item) {
            numberOfFriend.setText(item.getNumberOfFriend() + "명");
        }
    }

    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(RecyclerAdapter.OnItemClickEventListener listener) {
        mItemClickListener = listener;
    }

    public  interface OnMenuItemClickEventListener {
        void onMenuItemClick(int position);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickEventListener listener) {
        mMenuItemClickListener = listener;
    }

}

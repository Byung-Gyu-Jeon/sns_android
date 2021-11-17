package com.example.sns.main.ui.feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.FeedimagelistDATA;

import java.util.List;



public class ViewPageAdapter extends PagerAdapter {
    Context context;
    item item;

    private static final String TAG4 = "FeedFragment4";

    public ViewPageAdapter(Context context, item item) {
        this.context = context;
        this.item = item;
    }

    //기존 multiImageContents에서 item.imageContents.size()로 변경함
    @Override
    public int getCount() {
        return item.imageContents.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.img_contents_view, container, false);
        ImageView imageView = view.findViewById(R.id.img_contents_view);

        List<FeedimagelistDATA> data = item.getImageContents();


        Log.d(TAG4,"data의 크기"+data.size());

        Glide.with(context).load("http://192.168.0.2:8080/sns/download2?fileName="+data.get(position).getImagename()).into(imageView);

        //imageView.setImageResource(item.getMultiImageContents()[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}

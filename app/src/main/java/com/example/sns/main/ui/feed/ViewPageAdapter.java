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

import com.example.sns.R;

public class ViewPageAdapter extends PagerAdapter {
    Context context;
    item item;

    public ViewPageAdapter(Context context, item item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.getMultiImageContents().length;
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
        imageView.setImageResource(item.getMultiImageContents()[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}

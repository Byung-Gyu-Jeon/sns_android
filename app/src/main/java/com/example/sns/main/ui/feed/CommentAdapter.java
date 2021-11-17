package com.example.sns.main.ui.feed;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sns.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    ArrayList<Data2> datadata = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ItemViewHolder holder, int position) {

        holder.Bind(datadata.get(position));
    }

    @Override
    public int getItemCount() {
        return datadata.size();
    }
    void addItem(Data2 data){
        datadata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private TextView textView2;
        private TextView textView3;
        private Button btn;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            textView3 = itemView.findViewById(R.id.textview3);
            btn = itemView.findViewById(R.id.button);

        }
        void Bind(Data2 data){
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
            imageView.setImageResource(data.getResId());
            textView.setText(data.getUsername());
            textView2.setText(data.getInside());
        }

    }
}

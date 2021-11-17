package com.example.sns.main.ui.feed;

import com.google.gson.annotations.SerializedName;

//게시글 추가화면에서 피드 텍스트
public class MyFeedaddDATA {

    @SerializedName("feedtext")
    private String feedtext;

    public String getFeedtext() {
        return feedtext;
    }

    public void setFeedtext(String feedtext) {
        this.feedtext = feedtext;
    }
}

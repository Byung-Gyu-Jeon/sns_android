package com.example.sns.main.ui.Myprofile;

import com.google.gson.annotations.SerializedName;
//게시물 추가메뉴에서 추가한 피드들의 번호와 사진이름들을 저장하기 위한 데이터 클래스
public class FeedimagelistDATA {

    @SerializedName("feedno")
    private int feedno;

    @SerializedName("imagename")
    private String imagename;

    @SerializedName("username")
    private String username;

    @SerializedName("feedcontent")
    private String feedcontent;

    @SerializedName("myimagename")
    private String myimagename;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFeedcontent() {
        return feedcontent;
    }

    public void setFeedcontent(String feedcontent) {
        this.feedcontent = feedcontent;
    }

    public String getMyimagename() {
        return myimagename;
    }

    public void setMyimagename(String myimagename) {
        this.myimagename = myimagename;
    }

    public int getFeedno() {
        return feedno;
    }

    public void setFeedno(int feedno) {
        this.feedno = feedno;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}

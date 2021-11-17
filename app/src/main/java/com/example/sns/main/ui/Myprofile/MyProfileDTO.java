package com.example.sns.main.ui.Myprofile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyProfileDTO {


    @SerializedName("username")
    private String username;

    @SerializedName("userIntroduce")
    private String userIntroduce;

    @SerializedName("ptoname")
    private String ptoname;

    @SerializedName("list")
    public List<FeedimagelistDATA> list;






    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserIntroduce(String userIntroduce) {
        this.userIntroduce = userIntroduce;
    }

    public void setPtoname(String ptoname) {this.ptoname = ptoname;}

    public String getUsername() {
        return username;
    }

    public String getUserIntroduce() {
        return userIntroduce;
    }

    public String getPtoname() { return ptoname; }

    @Override
    public String toString() {
        return "유저이름:"+username+"유저소개:"+userIntroduce;
    }


}

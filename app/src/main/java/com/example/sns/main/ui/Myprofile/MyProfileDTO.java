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

    /*
	친구 관계 판별하기위한 코드
	4900 : 친구가 아님
	4800 : 친구 요청을 받음
	4700 : 내가 친구요청을 함
	4600 : 친구관계
	*/
    @SerializedName("code")
    private int code;

    @SerializedName("numberOfFriend")
    private int numberOfFriend;






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

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public int getNumberOfFriend() { return numberOfFriend; }

    public void setNumberOfFriend(int numberOfFriend) { this.numberOfFriend = numberOfFriend; }

    @Override
    public String toString() {
        return "유저이름:"+username+"유저소개:"+userIntroduce;
    }


}

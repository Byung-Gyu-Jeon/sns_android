package com.example.sns.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

public class User {
    @SerializedName("tokenDTO")
    private TokenDTO tokenDTO;

    @SerializedName("userNo")
    private Long userNo;

    @SerializedName("userRole")
    private List<String> userRole;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userId")
    private String userId;

    @SerializedName("userPassword")
    private String userPassword;

    @SerializedName("userImageUrl")
    private String userImageUrl;

    @SerializedName("userIntroduction")
    private String userIntroduction;

    @SerializedName("userSignupType")
    private byte userSignupType;

    public TokenDTO getTokenDTO() {
        return tokenDTO;
    }

    public void setTokenDTO(TokenDTO tokenDTO) {
        this.tokenDTO = tokenDTO;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public List<String> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<String> userRole) {
        this.userRole = userRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public byte getUserSignupType() {
        return userSignupType;
    }

    public void setUserSignupType(byte userSignupType) {
        this.userSignupType = userSignupType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", userIntroduction='" + userIntroduction + '\'' +
                ", userSignupType=" + userSignupType +
                '}';
    }
}

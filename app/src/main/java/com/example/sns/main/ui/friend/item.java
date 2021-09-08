package com.example.sns.main.ui.friend;

public class item {
    private Long userNo;
    private String userName;
    private String userImageUrl;
    private String isFriend;
    private int profileImage;
    private int acceptButton;
    private int deleteButton;
    int viewType;

    public item(int viewType) {
        this.viewType = viewType;
    }

//    public item(String userName, int profileImage, int viewType) {
//        this.userName = userName;
//        this.profileImage = profileImage;
//        this.viewType = viewType;
//    }

//    public item(String userName, String userImageUrl, int viewType) {
//        this.userName = userName;
//        this.userImageUrl = userImageUrl;
//        this.viewType = viewType;
//    }

//    public item(String userName, int profileImage, int acceptButton, int deleteButton, int viewType) {
//        this.userName = userName;
//        this.profileImage = profileImage;
//        this.acceptButton = acceptButton;
//        this.deleteButton = deleteButton;
//        this.viewType = viewType;
//    }


    public item(Long userNo, String userName, String userImageUrl, int viewType) {
        this.userNo = userNo;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.viewType = viewType;
    }

    public item(String userName, String userImageUrl, int acceptButton, int deleteButton, int viewType) {
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.acceptButton = acceptButton;
        this.deleteButton = deleteButton;
        this.viewType = viewType;
    }

    public item(Long userNo, String userName, String userImageUrl, String isFriend, int viewType) {
        this.userNo = userNo;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.isFriend = isFriend;
        this.viewType = viewType;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public int getAcceptButton() {
        return acceptButton;
    }

    public void setAcceptButton(int acceptButton) {
        this.acceptButton = acceptButton;
    }

    public int getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(int deleteButton) {
        this.deleteButton = deleteButton;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

package com.example.sns.main.ui.chat;

public class SearchListItem {
    private Long userNo;
    private String userName;
    private String userImageUrl;
    private String isFriend;
    int viewType;

    public SearchListItem(int viewType) {
        this.viewType = viewType;
    }

    public SearchListItem(Long userNo, String userName, String userImageUrl, String isFriend, int viewType) {
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

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

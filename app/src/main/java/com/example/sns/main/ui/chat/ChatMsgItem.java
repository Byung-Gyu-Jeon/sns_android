package com.example.sns.main.ui.chat;

public class ChatMsgItem {
    private String roomNo;
    private Long userNo;
    private String userName;
    private String userImageUrl;
    private String message;
    private String time;
    int viewType;

    public ChatMsgItem(String time, int viewType) {
        this.time = time;
        this.viewType = viewType;
    }

    public ChatMsgItem(String roomNo, Long userNo, String userName, String userImageUrl, String message, String time, int viewType) {
        this.roomNo = roomNo;
        this.userNo = userNo;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.message = message;
        this.time = time;
        this.viewType = viewType;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

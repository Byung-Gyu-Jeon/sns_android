package com.example.sns.main.ui.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatRoomItem implements Serializable {
    private String roomNo;
    private String roomName;
    private List<Long> userNo;
    private List<String> userImageUrl;
//    private List<String> userName;
    int participantNumbers;
    private String receivedMsg;
    private String receivedMsgTime;
    private int receivedMsgNumbers;
    int viewType;

    public ChatRoomItem() {
    }

    public ChatRoomItem(String roomNo, String roomName, List<Long> userNo, List<String> userImageUrl, int participantNumbers, String receivedMsg, String receivedMsgTime, int receivedMsgNumbers, int viewType) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.userNo = userNo;
        this.userImageUrl = userImageUrl;
        this.participantNumbers = participantNumbers;
        this.receivedMsg = receivedMsg;
        this.receivedMsgTime = receivedMsgTime;
        this.receivedMsgNumbers = receivedMsgNumbers;
        this.viewType = viewType;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomName() { return roomName; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public List<Long> getUserNo() {
        return userNo;
    }

    public void setUserNo(List<Long> userNo) {
        this.userNo = userNo;
    }

    public List<String> getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(List<String> userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

//    public List<String> getUserName() {
//        return userName;
//    }
//
//    public void setUserName(List<String> userName) {
//        this.userName = userName;
//    }

    public int getParticipantNumbers() {
        return participantNumbers;
    }

    public void setParticipantNumbers(int participantNumbers) {
        this.participantNumbers = participantNumbers;
    }

    public String getReceivedMsg() {
        return receivedMsg;
    }

    public void setReceivedMsg(String receivedMsg) {
        this.receivedMsg = receivedMsg;
    }

    public String getReceivedMsgTime() {
        return receivedMsgTime;
    }

    public void setReceivedMsgTime(String receivedMsgTime) {
        this.receivedMsgTime = receivedMsgTime;
    }

    public int getReceivedMsgNumbers() {
        return receivedMsgNumbers;
    }

    public void setReceivedMsgNumbers(int receivedMsgNumbers) {
        this.receivedMsgNumbers = receivedMsgNumbers;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

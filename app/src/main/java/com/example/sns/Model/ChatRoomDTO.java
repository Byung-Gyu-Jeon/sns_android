package com.example.sns.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ChatRoomDTO extends RealmObject {
    @Required
    private String roomNo;
    private String roomName;

    private Long userNo;
    private String myImageUrl;
    private String userName;

    private RealmList<Long> userNumbers;
    private RealmList<String> userImageUrl;

    private int participantNumbers;
    private String receivedMsg;
    private String receivedMsgTime;
    private int receivedMsgNumbers;
//    private RealmList<ChatRoomDTO> chatRoomDTORealmList;

    public ChatRoomDTO() { }

    public ChatRoomDTO(String roomNo, String myImageUrl, String userName, Long userNo, String roomName, int participantNumbers) {
        this.roomNo = roomNo;
        this.myImageUrl = myImageUrl;
        this.userName = userName;
        this.userNo = userNo;
        this.roomName = roomName;
        this.participantNumbers = participantNumbers;
    }

    public ChatRoomDTO(String roomNo, String myImageUrl, String userName, Long userNo) {
        this.roomNo = roomNo;
        this.myImageUrl = myImageUrl;
        this.userName = userName;
        this.userNo = userNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RealmList<String> getUserImageUrl() { return userImageUrl; }

    public void setUserImageUrl(RealmList<String> userImageUrl) { this.userImageUrl = userImageUrl; }

    public String getMyImageUrl() { return myImageUrl; }

    public RealmList<Long> getUserNumbers() { return userNumbers; }

    public void setUserNumbers(RealmList<Long> userNumbers) { this.userNumbers = userNumbers; }

    public void setMyImageUrl(String myImageUrl) { this.myImageUrl = myImageUrl; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

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

//    public RealmList<ChatRoomDTO> getChatRoomDTORealmList() {
//        return chatRoomDTORealmList;
//    }
//
//    public void setChatRoomDTORealmList(RealmList<ChatRoomDTO> chatRoomDTORealmList) {
//        this.chatRoomDTORealmList = chatRoomDTORealmList;
//    }
}

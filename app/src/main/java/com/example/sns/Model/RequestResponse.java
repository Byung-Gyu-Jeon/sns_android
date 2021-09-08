package com.example.sns.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RequestResponse {
    @SerializedName("tokenDTO")
    private TokenDTO tokenDTO;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private int code;

    @SerializedName("friendRequestListEntity")
    private List<FriendRequestListEntity> friendRequestList = new ArrayList<>();
    @SerializedName("searchListEntity")
    private List<SearchListEntity> searchList = new ArrayList<>();

//    FriendRequestListEntity friendRequestListEntity;

    public static class FriendRequestListEntity {
        @SerializedName("userNo")
        private Long userNo;
        @SerializedName("userName")
        private String userName;
        @SerializedName("userImageUrl")
        private String userImageUrl;

//        public FriendRequestListEntity() {
//            super();
//        }
//
        public FriendRequestListEntity(Long userNo, String userName, String userImageUrl) {
            this.userNo = userNo;
            this.userName = userName;
            this.userImageUrl = userImageUrl;
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

    }

    public static class SearchListEntity {
        @SerializedName("userNo")
        Long userNo;
        @SerializedName("userName")
        String userName;
        @SerializedName("isFriend")
        String isFriend;
        @SerializedName("userImageUrl")
        String userImageUrl;

        public SearchListEntity() {
            super();
        }

        public SearchListEntity(String userName, String userImageUrl) {
            super();
            this.userName = userName;
            this.userImageUrl = userImageUrl;
        }

        public SearchListEntity(String userName, String isFriend, String userImageUrl) {
            super();
            this.userName = userName;
            this.isFriend = isFriend;
            this.userImageUrl = userImageUrl;
        }

        public SearchListEntity(Long userNo, String userName, String userImageUrl) {
            super();
            this.userNo = userNo;
            this.userName = userName;
            this.userImageUrl = userImageUrl;
        }

        public SearchListEntity(Long userNo, String userName, String isFriend, String userImageUrl) {
            super();
            this.userNo = userNo;
            this.userName = userName;
            this.isFriend = isFriend;
            this.userImageUrl = userImageUrl;
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

        public String getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(String isFriend) {
            this.isFriend = isFriend;
        }

        public String getUserImageUrl() {
            return userImageUrl;
        }

        public void setUserImageUrl(String userImageUrl) {
            this.userImageUrl = userImageUrl;
        }
    }

//	public FriendRequestListEntity instance = new RequestResponse.FriendRequestListEntity();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<FriendRequestListEntity> getFriendRequestListEntity() {
        return friendRequestList;
    }

    public void setFriendRequestListEntity(List<FriendRequestListEntity> friendRequestList) {
        this.friendRequestList = friendRequestList;
    }

    public TokenDTO getTokenDTO() {
        return tokenDTO;
    }

    public void setTokenDTO(TokenDTO tokenDTO) {
        this.tokenDTO = tokenDTO;
    }

    public List<SearchListEntity> getSearchListEntity() {
        return searchList;
    }

    public void setSearchListEntity(List<SearchListEntity> searList) {
        this.searchList = searList;
    }

}


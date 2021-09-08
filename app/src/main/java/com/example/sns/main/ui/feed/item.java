package com.example.sns.main.ui.feed;

public class item {
    private String userName;
    private String textContents;
    private int profileImage;
    private int imageContents;
    private int[] multiImageContents;
    private int expandedMenuButton;
    private int likeButton;
    private int commentsButton;
    private int shareButton;
    int viewType;

    public item(String userName, String textContents, int profileImage, int imageContents, int expandedMenuButton, int likeButton, int commentsButton, int shareButton, int viewType) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.imageContents = imageContents;
        this.expandedMenuButton = expandedMenuButton;
        this.likeButton = likeButton;
        this.commentsButton = commentsButton;
        this.shareButton = shareButton;
        this.viewType = viewType;
    }

    public item(String userName, String textContents, int profileImage, int[] multiImageContents, int expandedMenuButton, int likeButton, int commentsButton, int shareButton, int viewType) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.multiImageContents = multiImageContents;
        this.expandedMenuButton = expandedMenuButton;
        this.likeButton = likeButton;
        this.commentsButton = commentsButton;
        this.shareButton = shareButton;
        this.viewType = viewType;
    }

    public item(String userName, String textContents, int profileImage, int imageContents, int viewType) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.imageContents = imageContents;
        this.viewType = viewType;
    }

    public item(String userName, String textContents, int profileImage, int[] multiImageContents, int viewType) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.multiImageContents = multiImageContents;
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextContents() {
        return textContents;
    }

    public void setTextContents(String textContents) {
        this.textContents = textContents;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public int getImageContents() {
        return imageContents;
    }

    public void setImageContents(int imageContents) {
        this.imageContents = imageContents;
    }

    public int[] getMultiImageContents() { return multiImageContents; }

    public void setMultiImageContents(int[] imageContents) { this.multiImageContents = multiImageContents; }

    public int getExpandedMenuButton() {
        return expandedMenuButton;
    }

    public void setExpandedMenuButton(int expandedMenuButton) {
        this.expandedMenuButton = expandedMenuButton;
    }

    public int getLikeButton() {
        return likeButton;
    }

    public void setLikeButton(int likeButton) {
        this.likeButton = likeButton;
    }

    public int getCommentsButton() {
        return commentsButton;
    }

    public void setCommentsButton(int commentsButton) {
        this.commentsButton = commentsButton;
    }

    public int getShareButton() {
        return shareButton;
    }

    public void setShareButton(int shareButton) {
        this.shareButton = shareButton;
    }
}

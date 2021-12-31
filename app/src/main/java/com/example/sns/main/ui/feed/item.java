package com.example.sns.main.ui.feed;

import com.example.sns.main.ui.Myprofile.FeedimagelistDATA;

import java.util.List;

public class item {
    private String userName;
    private String textContents;
    //profileImage int -> string 변경
    private String profileImage;
    //imageContents -> List<imageContents>로 변경
    List<FeedimagelistDATA> imageContents=null;
    private int[] multiImageContents;
    private int expandedMenuButton;
    private int likeButton;
    private int commentsButton;
    private int shareButton;
    //현재 피드 번호를 넘겨주기 위한 코드 (comments 테이블의 feedno 컬럼에 데이터를 넣어주기 위해서)
    private int feedno;
    int viewType;

    private String profilename;
    private String text;
    private int userno;
    //댓글 작성한 시간
    String commenttime;

    //답글화면에서 필요한 feedno
    String feednoo;

    //댓글 고유번호
    String commentsno;

    //답글 내용
    String recommenttext;

    //답글 작성시간
    String recommenttime;

    //댓글 화면에서 필요한 답글 갯수
    int recomment_count;

    //좋아요 갯수
    int feedlikecount;

    //스프링에서 뽑은 내 고유 userno
    int spring_my_userno;

    public int getSpring_my_userno() {
        return spring_my_userno;
    }

    public void setSpring_my_userno(int spring_my_userno) {
        this.spring_my_userno = spring_my_userno;
    }

    public int getFeedlikecount() {
        return feedlikecount;
    }

    public void setFeedlikecount(int feedlikecount) {
        this.feedlikecount = feedlikecount;
    }

    public int getRecomment_count() {
        return recomment_count;
    }

    public void setRecomment_count(int recomment_count) {
        this.recomment_count = recomment_count;
    }

    public String getRecommenttext() {
        return recommenttext;
    }

    public void setRecommenttext(String recommenttext) {
        this.recommenttext = recommenttext;
    }

    public String getRecommenttime() {
        return recommenttime;
    }

    public void setRecommenttime(String recommenttime) {
        this.recommenttime = recommenttime;
    }

    public String getCommentsno() {
        return commentsno;
    }

    public void setCommentsno(String commentsno) {
        this.commentsno = commentsno;
    }

    public String getFeednoo() {
        return feednoo;
    }

    public void setFeednoo(String feednoo) {
        this.feednoo = feednoo;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserno() {
        return userno;
    }

    public void setUserno(int userno) {
        this.userno = userno;
    }

    public int getFeedno() {
        return feedno;
    }

    public void setFeedno(int feedno) {
        this.feedno = feedno;
    }

    int additemsize;

   /* public item(String userName, String textContents, String profileImage, int imageContents, int expandedMenuButton, int likeButton, int commentsButton, int shareButton, int viewType) {
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

    public item(String userName, String textContents, String profileImage, int[] multiImageContents, int expandedMenuButton, int likeButton, int commentsButton, int shareButton, int viewType) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.multiImageContents = multiImageContents;
        this.expandedMenuButton = expandedMenuButton;
        this.likeButton = likeButton;
        this.commentsButton = commentsButton;
        this.shareButton = shareButton;
        this.viewType = viewType;
    }*/

    //메인 화면에서 쓰는거
    public item(String userName, String textContents, String profileImage, List<FeedimagelistDATA> imageContents, int viewType,int feedno,int userno,int feedlikecount,int spring_my_userno) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.imageContents = imageContents;
        this.viewType = viewType;
        this.feedno = feedno;
        this.userno = userno;
        this.feedlikecount = feedlikecount;
        this.spring_my_userno = spring_my_userno;
    }
    //댓글 화면에서 쓰는거
    public item(String userName,String profilename,String text,int userno,String commenttime,String feednoo,String commentsno,int recomment_count){
        this.userName = userName;
        this.profilename = profilename;
        this.text = text;
        this.userno = userno;
        this.commenttime = commenttime;
        this.feednoo = feednoo;
        this.commentsno = commentsno;
        this.recomment_count =recomment_count;
    }

    //답글 화면에서 쓰는거
    public item(String userName,String profilename,String recommenttext,String recommenttime){
        this.userName = userName;
        this.profilename = profilename;
        this.recommenttext = recommenttext;
        this.recommenttime = recommenttime;
    }

    //대댓글 화면에서 쓰는거 - 미완성
   /* public item(String userName,String profilename,String text,int userno,String commenttime,String feednoo,String s){

    }*/


    /*public item(String userName, String textContents, String profileImage, int[] multiImageContents, int viewType) {
        this.userName = userName;
        this.textContents = textContents;
        this.profileImage = profileImage;
        this.multiImageContents = multiImageContents;
        this.viewType = viewType;
    }*/

    public int getAdditemsize() {
        return additemsize;
    }

    public void setAdditemsize(int additemsize) {
        this.additemsize = additemsize;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<FeedimagelistDATA> getImageContents() {
        return imageContents;
    }

    public void setImageContents(List<FeedimagelistDATA> imageContents) {
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

package com.example.sns.main.ui.feed;

import com.google.gson.annotations.SerializedName;

public class CommenttextDATA {

    @SerializedName("commentText")
    String commentText;

    @SerializedName("picname")
    String picname;

    @SerializedName("username")
    String username;

    @SerializedName("text")
    String text;

    @SerializedName("userno")
    int userno;

    //댓글 작성시간
    @SerializedName("commenttime")
    String commenttime;

    //댓글 고유번호
    @SerializedName("commentsno")
    String commentsno;

    //답글 내용
    @SerializedName("recommenttext")
    String recommenttext;

    //답글 작성시간
    @SerializedName("recommenttime")
    String recommenttime;

    //답글화면서 필요한 comments_no
    @SerializedName("comments_no")
    int comments_no;

    //댓글화면에서 필요한 답글 갯수
    @SerializedName("recomment_count")
    int recomment_count;

    public int getRecomment_count() {
        return recomment_count;
    }

    public void setRecomment_count(int recomment_count) {
        this.recomment_count = recomment_count;
    }

    public int getComments_no() {
        return comments_no;
    }

    public void setComments_no(int comments_no) {
        this.comments_no = comments_no;
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

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public int getUserno() {
        return userno;
    }

    public void setUserno(int userno) {
        this.userno = userno;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}

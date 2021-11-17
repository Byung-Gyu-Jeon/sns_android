package com.example.sns.main.ui.chat;

import java.io.Serializable;

public class CheckBoxData implements Serializable {
    private Long userNo;
    private String userName;
    private String imageUrl;
    private Boolean checked;

    public CheckBoxData() {
    }

    public CheckBoxData(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public CheckBoxData(Long userNo, Boolean checked) {
        this.userNo = userNo;
        this.checked = checked;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

package com.example.sns.main.ui.feed;

public class Data2 {
    private int resId;
    private String username;
    private String inside;

    public Data2(int resId, String username, String inside) {
        this.resId = resId;
        this.username = username;
        this.inside = inside;
    }

    public int getResId() {
        return resId;
    }

    public String getUsername() {
        return username;
    }

    public String getInside() {
        return inside;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInside(String inside) {
        this.inside = inside;
    }
}
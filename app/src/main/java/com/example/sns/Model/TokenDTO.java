package com.example.sns.Model;

import com.google.gson.annotations.SerializedName;

public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpireIn;
    private int code;

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public Long getAccessTokenExpiresIn() {
        return accessTokenExpiresIn;
    }
    public void setAccessTokenExpiresIn(Long accessTokenExpiresIn) {
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }
    public Long getRefreshTokenExpireIn() {
        return refreshTokenExpireIn;
    }
    public void setRefreshTokenExpireIn(Long refreshTokenExpireIn) {
        this.refreshTokenExpireIn = refreshTokenExpireIn;
    }
    public int getCode() { return code; }
}

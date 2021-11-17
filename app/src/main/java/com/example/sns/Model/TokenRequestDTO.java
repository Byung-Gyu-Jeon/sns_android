package com.example.sns.Model;

import com.google.gson.annotations.SerializedName;

public class TokenRequestDTO {
    @SerializedName("tokenDTO")
    private TokenDTO tokenDTO;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("userNo")
    private Long userNo;
    @SerializedName("type")
    private int type; // 0-> acessToken만 갱신, 1 -> refreshToken도 갱신.. 자동로그인시 3 -> 재로그인 필요

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public int getType() {
        return type;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TokenDTO getTokenDTO() {
        return tokenDTO;
    }

    public void setTokenDTO(TokenDTO tokenDTO) {
        this.tokenDTO = tokenDTO;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }
}

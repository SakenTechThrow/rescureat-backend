package com.rescureat.dto;

import com.rescureat.model.User;

public class AuthResponse {

    private String token;
    private UserResponse user;

    public static AuthResponse of(String token, User user) {
        AuthResponse r = new AuthResponse();
        r.token = token;
        r.user = UserResponse.from(user);
        return r;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}

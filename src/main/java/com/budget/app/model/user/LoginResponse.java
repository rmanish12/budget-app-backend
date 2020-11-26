package com.budget.app.model.user;

public class LoginResponse {

    private UserInfoOnLogin user;
    private String authToken;

    public UserInfoOnLogin getUser() {
        return user;
    }

    public void setUser(UserInfoOnLogin user) {
        this.user = user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

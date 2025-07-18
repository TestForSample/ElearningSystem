package com.example.ElearningSystem.model;

public class AuthResponse {


    private String authToken;
public AuthResponse(String authToken){
    this.authToken=authToken;
}
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package com.pulsetap.dto;

import com.pulsetap.model.User;

public class AuthResponse {
    
    private User user;
    private String token;
    private String message;
    
    // Construtores
    public AuthResponse() {}
    
    public AuthResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
    
    public AuthResponse(User user, String token, String message) {
        this.user = user;
        this.token = token;
        this.message = message;
    }
    
    // Getters e Setters
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "user=" + user +
                ", token='[PROTECTED]'" +
                ", message='" + message + '\'' +
                '}';
    }
}

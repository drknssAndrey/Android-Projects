package com.example.ambientcontrollproject.model;

public class Users {

    private String username;
    private String password;
    private String picture;

    public Users(){

    }

    public Users(String username, String password, String picture) {
        this.username = username;
        this.password = password;
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

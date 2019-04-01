package com.example.cs160_sp18.prog3;

public class Userinfo {
    private String name;
    private String email;
    private String phone_num;

    private String comment;
    private String landmark;
    public Userinfo(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return phone_num;
    }

    public void setPassword(String password) {
        this.phone_num = password;
    }
}

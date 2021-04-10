package com.ir.smartcity.user;

public class User {

    private String name;
    private String username;
    private String password;
    private String data;
    private String uid;
    private String phoneNumber;

    public User() {
    }

    public User(String name, String username, String data, String phoneNo) {
        this.name = name;
        this.username = username;
        //this.password = password;
        this.data = data;
        //this.uid = uid;
        phoneNumber = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

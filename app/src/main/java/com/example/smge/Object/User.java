package com.example.smge.Object;

public class User {
    private String name;
    private String dateOfBirth;
    private String mobileNo;
    private String email;

    public User(String name, String dateOfBirth, String mobileNo, String email) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.mobileNo = mobileNo;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

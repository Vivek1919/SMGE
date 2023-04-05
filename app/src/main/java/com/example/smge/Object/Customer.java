package com.example.smge.Object;

public class Customer {
    private String name;
    private String emailNo;
    private String mobileNo;
    private String dateOfBirth;

    public Customer(String name, String emailNo, String mobileNo, String dateOfBirth) {
        this.name = name;
        this.emailNo = emailNo;
        this.mobileNo = mobileNo;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getEmailNo() {
        return emailNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}


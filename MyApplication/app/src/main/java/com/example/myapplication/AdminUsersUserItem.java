package com.example.myapplication;

public class AdminUsersUserItem {

    private String ref,FName,LName,gender,phone,address,birthDate,email,property;

    public AdminUsersUserItem(String ref, String FName, String LName, String gender, String phone, String address, String birthDate, String email, String property) {
        this.ref = ref;
        this.FName = FName;
        this.LName = LName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.email = email;
        this.property = property;
    }

    public String getRef() {
        return ref;
    }

    public String getFName() {
        return FName;
    }

    public String getLName() {
        return LName;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getProperty() {
        return property;
    }
}

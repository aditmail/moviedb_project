package com.example.movieproject.models.register_form;

import android.graphics.Bitmap;

public class RegisterForm {
    private String idUser = "";
    private String phoneNumber = "";
    private String fullName = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";

    public RegisterForm(String idUser, String phoneNumber, String fullName, String email, String password, String confirmPassword) {
        this.idUser = idUser;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}

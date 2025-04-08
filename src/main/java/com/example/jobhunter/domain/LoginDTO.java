package com.example.jobhunter.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank(message = "user name can't be empty")
    private String userName;
    @NotBlank(message = "password cann't be blank")
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

}

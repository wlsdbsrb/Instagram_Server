package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String UserName;
    private String ID;
    private String password;
    private String email;
    private String phone;
    private String nickname;
}

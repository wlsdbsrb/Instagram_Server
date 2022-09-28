package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetUserProfileRes {
    private int userIdx;
    private String userName;
    private String profileImage;
    private String nickname;
    private String introduce;
    private String email;
    private int followReceiver;
    private int followSender;
    private int post;
}

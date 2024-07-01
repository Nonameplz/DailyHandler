package com.nonameplz.dailyhandler.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class user {
    private String userUUID;
    private String userName;
    private String password;
    private String userAvatar;
    private String userNickName;
    private String email;
    private String phoneNumber;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}

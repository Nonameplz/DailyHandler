package com.nonameplz.dailyhandler.service;

import com.nonameplz.dailyhandler.pojo.user;

public interface userService {
    //根据用户名获取用户UUID
    String findUserByUserName(String username);

    //根据用户UUID获取用户
    user getUser(String userUUID);

    //根据用户名密码获取用户
    user getUser(String userName,String password);

    //更新用户信息
    void updateUserInfo(user user);

    void updateUserPassword(String userUUID, String password);
}

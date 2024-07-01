package com.nonameplz.dailyhandler.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import com.nonameplz.dailyhandler.pojo.Result;
import com.nonameplz.dailyhandler.pojo.user;
import com.nonameplz.dailyhandler.service.userService;
import com.nonameplz.dailyhandler.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private userService userservice;

    @GetMapping("/getUserInfo")
    public Result<JSONObject> getUser() {
        Map<String, Object> userBaseInfo = ThreadLocalUtil.get();

        user user = userservice.getUser(userBaseInfo.get("userUUID").toString());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userUUID", user.getUserUUID());
        userInfo.put("userName", user.getUserName());
        userInfo.put("userAvatar", user.getUserAvatar());
        userInfo.put("userNickName", user.getUserNickName());
        userInfo.put("email", user.getEmail());
        userInfo.put("phoneNumber", user.getPhoneNumber());
        userInfo.put("createTime", user.getCreateTime());

        JSONObject jsonObject = new JSONObject(userInfo);

        return Result.success(jsonObject);
    }

}

package com.nonameplz.dailyhandler.controller;

import com.alibaba.fastjson2.JSONObject;
import com.nonameplz.dailyhandler.utils.AliOSSUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.nonameplz.dailyhandler.pojo.Result;
import com.nonameplz.dailyhandler.pojo.user;
import com.nonameplz.dailyhandler.service.userService;
import com.nonameplz.dailyhandler.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private userService userservice;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AliOSSUtils ossUtils;


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

    @PutMapping("/updateUserInfo")
    public Result<Object> updateUserInfo(
            @RequestParam Object userAvatar,
            @RequestParam String userNickName,
            @RequestParam String email,
            @RequestParam String phoneNumber
    ) throws IOException {

        Map<String, Object> userInfo = ThreadLocalUtil.get();
        user user = userservice.getUser(userInfo.get("userUUID").toString());

        try {
            MultipartFile userAvatarFile = (MultipartFile) userAvatar;
            String dir = "userAvatar/";
            String url = ossUtils.upload(userAvatarFile, dir, user.getUserAvatar());

            user.setUserAvatar(url);
        } finally {
            user.setUserNickName(userNickName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
        }

        userservice.updateUserInfo(user);

        return Result.success();
    }

    @PutMapping("/rePassword")
    public Result<Object> rePassword(
            HttpServletRequest request,
            @RequestParam String password,
            @RequestParam String rePassword
    ) {

        if (!password.equals(rePassword)) {
            return Result.error("两次密码输入不同!");
        }

        Map<String, Object> userInfo = ThreadLocalUtil.get();
        String userUUID = userInfo.get("userUUID").toString();
        String authorization = request.getHeader("authorization");

        userservice.updateUserPassword(userUUID, password);

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(authorization);

        return Result.success();
    }

}

package com.nonameplz.dailyhandler.controller;


import com.nonameplz.dailyhandler.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.nonameplz.dailyhandler.pojo.Result;
import com.nonameplz.dailyhandler.pojo.reCAPTCHAResponse;
import com.nonameplz.dailyhandler.pojo.user;
import com.nonameplz.dailyhandler.service.authenticationService;

import jakarta.validation.constraints.Pattern;
import com.nonameplz.dailyhandler.service.userService;
import com.nonameplz.dailyhandler.state.statusInfo;
import com.nonameplz.dailyhandler.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/authentication")
public class authenticationController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private authenticationService authService;

    @Autowired
    private userService userservice;

    @PostMapping("/reCAPTCHACheck")
    public Result<Object> verify(HttpServletRequest request, @RequestParam("recaptchaToken") String token) {

        reCAPTCHAResponse res = authService.reCAPTCHAVerify(request, token);

        if (!res.isSuccess()) {
            return Result.error(statusInfo.reCAPTCHA_Verify_Failed);
        }

        if (res.getScore() < 0.5) {
            return Result.error(statusInfo.reCAPTCHA_Verify_NOT_Human);
        }

        return Result.success(statusInfo.reCAPTCHA_Verify_Success);
    }

    @PostMapping("/register")
    public Result<Object> register(@Pattern(regexp = "^\\S{4,20}$") String userName,
                                   @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z0-9])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{}|\\\\:;\"'<>,.?/~]{6,20}$") String password,
                                   @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") String email) {
        if (userservice.findUserByUserName(userName) != null) {
            return Result.error(statusInfo.userName_In_Use);
        }

        authService.register(userName, password, email);

        return Result.success();
    }

    @PostMapping("/login")
    public Result<Object> login(@Pattern(regexp = "^\\S{4,20}$") String userName,
                                @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z0-9])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{}|\\\\:;\"'<>,.?/~]{6,20}$") String password,
                                @RequestParam boolean rememberMe
    ) {
//        log.info("登录用户: {},密码: {}",userName,password);
        user user = userservice.getUser(userName, password);

        if (user == null) {
            return Result.error(statusInfo.user_Login_Failed);
        }

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String token = ThreadLocalUtil.get();
        if (token != null && ops.get(token) != null){
//            log.info(ops.get(token));
            ops.getOperations().delete(token);
        }

        Map<String, Object> jwtToken = new HashMap<>();
        jwtToken.put("userUUID", user.getUserUUID());
        jwtToken.put("userName", user.getUserName());

        String jwt = JwtUtil.genToken(jwtToken);

        ops.set(jwt, jwt, rememberMe ? 30 : 1, TimeUnit.DAYS);

        return Result.success(jwt);
    }
}

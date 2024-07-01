package com.nonameplz.dailyhandler.service.impl;

import com.nonameplz.dailyhandler.pojo.reCAPTCHAResponse;
import com.nonameplz.dailyhandler.pojo.user;
import com.nonameplz.dailyhandler.service.authenticationService;
import jakarta.servlet.http.HttpServletRequest;
import com.nonameplz.dailyhandler.mapper.userMapper;
import com.nonameplz.dailyhandler.utils.Md5Util;
import com.nonameplz.dailyhandler.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;



@Service
public class authenticationServiceImpl implements authenticationService {

    // 从配置文件中读取到后端key
    @Value("${recaptcha.server-key}")
    private String serverKey;

    // 请求地址
    @Value("${recaptcha.siteVerify}")
    private String siteVerify;

    @Autowired
    private userMapper userMapper;

    @Override
    public reCAPTCHAResponse reCAPTCHAVerify(HttpServletRequest request, String reCAPTCHAToken) {

        reCAPTCHAResponse RECAPTCHAResponse = null;

        //创建一个webClient实例
        WebClient webClient = WebClient.builder()
                .baseUrl(siteVerify)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //封装要发送给reCAPTCHA验证的数据
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", this.serverKey);            // 服务端key
        requestBody.add("response", reCAPTCHAToken);          // 客户端提交的token
        requestBody.add("remoteip", request.getRemoteAddr()); // 客户的ip地址，不是必须的参数。

        //以post请求发送给reCAPTCHA进行验证
        CompletableFuture<reCAPTCHAResponse> res = webClient.post()
                .uri("")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(reCAPTCHAResponse.class)
                .toFuture();

        try {
            RECAPTCHAResponse = res.get(); // 这里会阻塞直到获取到响应数据
        } catch (Exception e) {
            System.err.println("Error getting response: " + e.getMessage());
        }
        return RECAPTCHAResponse;
    }

    @Override
    public void register(String userName, String password, String email) {
        user User = new user();
        User.setUserUUID(UUIDGenerator.generateUUID());
        User.setUserName(userName);
        User.setPassword(Md5Util.getMD5String(password));
        User.setEmail(email);
        User.setCreateTime(LocalDateTime.now());
        User.setModifyTime(LocalDateTime.now());
        userMapper.register(User);
    }
}

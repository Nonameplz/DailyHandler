package com.nonameplz.dailyhandler.service;

import com.nonameplz.dailyhandler.pojo.reCAPTCHAResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface authenticationService {
    reCAPTCHAResponse reCAPTCHAVerify(HttpServletRequest request, String reCAPTCHAToken);

    void register(String userName, String password, String email);
}

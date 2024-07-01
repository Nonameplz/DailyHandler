package com.nonameplz.dailyhandler.interceptors;

import com.alibaba.fastjson2.JSONObject;
import com.nonameplz.dailyhandler.pojo.reCAPTCHAResponse;
import com.nonameplz.dailyhandler.state.statusInfo;
import com.nonameplz.dailyhandler.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.nonameplz.dailyhandler.pojo.Result;
import com.nonameplz.dailyhandler.service.authenticationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class authenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private authenticationService authService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws IOException {
        String token = request.getHeader("Authorization");
        String reCAPTCHAToken = request.getHeader("Reauthorization");

        if (token != null){
            ThreadLocalUtil.set(token);
        }

        log.info("reCAPTCHAToken:{}", reCAPTCHAToken);

        reCAPTCHAResponse res = authService.reCAPTCHAVerify(request, reCAPTCHAToken);

        log.info(res.toString());

        if (!res.isSuccess()) {

            Result<Object> error = Result.error(statusInfo.reCAPTCHA_Verify_Failed);

            String VERIFY_ERROR = JSONObject.toJSONString(error);

            log.error(VERIFY_ERROR);

            response.getWriter().write(VERIFY_ERROR);
            return false;
        }

        if (res.getScore() < 0.5) {
            Result<Object> error = Result.error(statusInfo.reCAPTCHA_Verify_NOT_Human);

            String NOT_HUMAN = JSONObject.toJSONString(error);

            log.error(NOT_HUMAN);

            response.getWriter().write(NOT_HUMAN);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据，防止内存泄漏
        ThreadLocalUtil.remove();
    }
}
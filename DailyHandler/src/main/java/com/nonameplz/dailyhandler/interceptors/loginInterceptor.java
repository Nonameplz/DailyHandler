package com.nonameplz.dailyhandler.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.nonameplz.dailyhandler.utils.JwtUtil;
import com.nonameplz.dailyhandler.utils.ThreadLocalUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;


@Slf4j
@Component
public class loginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws IOException {
        String token = request.getHeader("authorization");

        try {
            String redisToken = stringRedisTemplate.opsForValue().get(token);

            if (redisToken == null) {
                throw new RuntimeException();
            }

            Map<String, Object> userBaseInfo = JwtUtil.parseToken(redisToken);

            ThreadLocalUtil.set(userBaseInfo);

            return true;

        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

    }


    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据，防止内存泄漏
        ThreadLocalUtil.remove();
    }
}

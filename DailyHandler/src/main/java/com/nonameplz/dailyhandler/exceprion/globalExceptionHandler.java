package com.nonameplz.dailyhandler.exceprion;

import com.nonameplz.dailyhandler.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception ex) {
        ex.printStackTrace();
        return Result.error("出现异常情况!");
    }


}

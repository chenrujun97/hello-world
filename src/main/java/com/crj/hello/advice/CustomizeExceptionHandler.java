package com.crj.hello.advice;

import com.crj.hello.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/***
 * 处理服务器异常
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    String handle(Throwable ex,
                  Model model){
        if (ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        } else {
            model.addAttribute("message", "服务冒烟了, 要不你稍后再试试！！！");
        }
        return "error";
    }
}

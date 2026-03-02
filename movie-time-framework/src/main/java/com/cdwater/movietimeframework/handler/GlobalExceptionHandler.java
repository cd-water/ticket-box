package com.cdwater.movietimeframework.handler;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.fail();
    }

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return Result.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        log.error("请求参数错误", e);
        return Result.fail(RetEnum.BAD_REQUEST.getCode(), RetEnum.BAD_REQUEST.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("请求参数错误", e);
        return Result.fail(RetEnum.BAD_REQUEST.getCode(), RetEnum.BAD_REQUEST.getMsg());
    }
}

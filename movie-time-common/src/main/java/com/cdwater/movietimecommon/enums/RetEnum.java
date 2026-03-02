package com.cdwater.movietimecommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回信息枚举
 */
@AllArgsConstructor
@Getter
public enum RetEnum {

    OK(200, "请求成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "凭证无效或已过期"),
    FORBIDDEN(403, "无访问权限"),
    NOT_FOUND(404, "资源未找到"),
    ERROR(500, "系统异常"),

    /**
     * 认证相关
     */
    CODE_ERROR(666, "验证码错误"),
    CODE_FREQUENCY(666, "验证码请求频繁"),
    LOGIN_FREQUENT(666, "登录请求频繁"),
    LOGIN_FAIL(666, "账号或密码错误"),
    OLD_PASSWORD_ERROR(666, "原密码错误"),
    ACCOUNT_EXIST(666, "账号已存在"),

    UPLOAD_FAIL(666, "文件上传失败"),
    SYSTEM_BUSY(666, "系统繁忙，请稍后再试"),
    SEAT_LOCKED(666, "座位已锁定"),
    PAY_FAIL(666, "支付失败");

    private final Integer code;
    private final String msg;
}
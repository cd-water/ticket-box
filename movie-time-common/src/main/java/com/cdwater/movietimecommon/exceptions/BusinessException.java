package com.cdwater.movietimecommon.exceptions;

import com.cdwater.movietimecommon.enums.RetEnum;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String msg;

    public BusinessException(RetEnum retEnum) {
        this.code = retEnum.getCode();
        this.msg = retEnum.getMsg();
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

package com.cdwater.movietimecommon;

import com.cdwater.movietimecommon.enums.RetEnum;
import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(RetEnum.OK.getCode());
        result.setMsg(RetEnum.OK.getMsg());
        return result;
    }

    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode(RetEnum.ERROR.getCode());
        result.setMsg(RetEnum.ERROR.getMsg());
        return result;
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}

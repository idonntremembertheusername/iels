package com.iels.framework.model.response;

import lombok.ToString;

/**
 * @Description: 通用返回结果代码
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
@ToString
public enum CommonCode implements ResultCode {
    SUCCESS(true, 10000, "操作成功！"),
    UNAUTHENTICATED(false, 10001, "此操作需要登陆系统！"),
    INVALID_PARAM(false, 10002, "非法参数！"),
    FAIL(false, 11111, "操作失败！"),
    AUTHENTICATION_NOT_LOGGED_IN(false, 10003, "未登录认证"),
    PERMISSION_DENIED(false, 10004, "权限不足!"),
    SERVER_ERROR(false, 99999, "抱歉，系统繁忙，请稍后重试！"),
    UNAUTHORISE(false, 10005, "权限不足，无权操作！！"),

    ;
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private CommonCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

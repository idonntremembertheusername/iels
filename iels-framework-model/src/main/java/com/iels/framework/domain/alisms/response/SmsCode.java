package com.iels.framework.domain.alisms.response;

import com.iels.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/19 23
 * @Other:
 **/
@ToString
public enum SmsCode implements ResultCode {
    SMS_SEND_FAIL(false,25001,"发送短信失败!"),
    SMS_SAVE_CODE_FAIL(false,25001,"存储短信验证码失败!"),
    ;

    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    SmsCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String message() {
        return null;
    }
}

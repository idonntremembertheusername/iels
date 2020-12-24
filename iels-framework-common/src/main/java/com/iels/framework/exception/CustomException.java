package com.iels.framework.exception;

import com.iels.framework.model.response.ResultCode;

/**
 * @Description: 自定义异常类型
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
public class CustomException extends RuntimeException {

    //异常的结果代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}

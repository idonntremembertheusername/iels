package com.iels.framework.exception;

import com.iels.framework.model.response.ResultCode;

/**
 * @Description: 真正执行捕获[抛出]异常的类
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
public class ExceptionCast {

    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}

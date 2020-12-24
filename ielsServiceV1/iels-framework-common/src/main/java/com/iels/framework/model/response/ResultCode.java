package com.iels.framework.model.response;

/**
 * @Description: 返回结果代码
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
public interface ResultCode {
    //操作是否成功: true为成功,false操作失败
    boolean success();

    //操作代码
    int code();

    //提示信息
    String message();
}

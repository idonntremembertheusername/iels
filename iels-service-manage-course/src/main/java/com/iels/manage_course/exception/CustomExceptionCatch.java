package com.iels.manage_course.exception;


import com.iels.framework.exception.ExceptionCatch;
import com.iels.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @Description: 课程管理自定义的异常类
 * @Author: snypxk
 * @Date: 2019/12/17 14
 * @Other:
 **/
@ControllerAdvice //控制器增强
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}

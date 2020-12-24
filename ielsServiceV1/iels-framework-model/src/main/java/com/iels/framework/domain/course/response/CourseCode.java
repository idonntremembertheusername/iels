package com.iels.framework.domain.course.response;

import com.google.common.collect.ImmutableMap;
import com.iels.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@ToString
public enum CourseCode implements ResultCode {

    COURSE_DENIED_DELETE(false, 31001, "删除课程失败，只允许删除本机构的课程！"),
    COURSE_GET_NOT_EXISTS(false, 31002, "获取的课程不存在！"),
    COURSE_MEDIA_TEACHPLAN_ISNULL(false, 31003, "课程对应的媒资为空！"),
    COURSE_MEDIA_TEACHPLAN_GRADEERROR(false, 31004, "课程计划等级错误[应为三级课程计划]！"),
    ;

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;

    private CourseCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private static final ImmutableMap<Integer, CourseCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, CourseCode> builder = ImmutableMap.builder();
        for (CourseCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
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

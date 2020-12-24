package com.iels.framework.domain.learning.response;

import com.google.common.collect.ImmutableMap;
import com.iels.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 02
 * @Other:
 **/
public enum LearningCode implements ResultCode {

    LEARNING_GETMEDIA_ERROR(false,40601,"获取视频播放地址出错"),
    CHOOSE_COURSE_TASK_ISNULL(false,40602,"选择的课程任务为空!"),
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

    private LearningCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private static final ImmutableMap<Integer, LearningCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, LearningCode> builder = ImmutableMap.builder();
        for (LearningCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
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

package com.iels.framework.domain.filesystem.response;

import com.google.common.collect.ImmutableMap;
import com.iels.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * @Description: 文件系统返回结果代码
 * @Author: snypxk
 * @Date: 2019/12/13 10
 * @Other:
 **/
@ToString
public enum FileSystemCode implements ResultCode {
    FS_UPLOAD_FILE_ISNULL(false,25001,"上传文件为空！"),
    FS_UPLOAD_FILE_SERVER_FAIL(false,25002,"上传文件服务器出错"),
    INIT_FDFS_ERROR(false,25003,"初始化FDFS出错"),
    ;
    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private FileSystemCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private static final ImmutableMap<Integer, FileSystemCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, FileSystemCode> builder = ImmutableMap.builder();
        for (FileSystemCode commonCode : values()) {
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

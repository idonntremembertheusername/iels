package com.iels.framework.domain.filesystem.response;

import com.iels.framework.domain.filesystem.FileSystem;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 上传文件的返回结果类
 * @Author: snypxk
 * @Date: 2019/12/13 10
 * @Other:
 **/
@Data
public class UploadFileResult extends ResponseResult {

    //@ApiModelProperty(value = "文件信息", example = "true", required = true)
    FileSystem fileSystem;

    public UploadFileResult(ResultCode resultCode, FileSystem fileSystem) {
        super(resultCode);
        this.fileSystem = fileSystem;
    }
}

package com.iels.framework.domain.media.response;

import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description: 检查视频分块上传的结果返回类
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
public class CheckChunkResult extends ResponseResult {

    @ApiModelProperty(value = "文件分块存在标记", example = "true", required = true)
    boolean fileExist;

    public CheckChunkResult(ResultCode resultCode, boolean fileExist) {
        super(resultCode);
        this.fileExist = fileExist;
    }
}

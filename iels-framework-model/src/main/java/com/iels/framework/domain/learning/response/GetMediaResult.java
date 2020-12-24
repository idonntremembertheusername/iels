package com.iels.framework.domain.learning.response;

import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 02
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {

    //媒资文件播放地址
    private String fileUrl;

    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}

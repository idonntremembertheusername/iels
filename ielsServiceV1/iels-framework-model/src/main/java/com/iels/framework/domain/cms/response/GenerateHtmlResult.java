package com.iels.framework.domain.cms.response;

import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;

@Data
public class GenerateHtmlResult extends ResponseResult {
    String html;

    public GenerateHtmlResult(ResultCode resultCode, String html) {
        super(resultCode);
        this.html = html;
    }
}

package com.iels.framework.domain.order.response;

import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayQrcodeResult extends ResponseResult {

    public PayQrcodeResult(ResultCode resultCode) {
        super(resultCode);
    }

    private String codeUrl;
    private Float money;
    private String orderNumber;
}

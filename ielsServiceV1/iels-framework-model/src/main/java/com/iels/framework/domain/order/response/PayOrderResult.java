package com.iels.framework.domain.order.response;

import com.iels.framework.domain.order.IelsOrdersPay;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayOrderResult extends ResponseResult {
    public PayOrderResult(ResultCode resultCode) {
        super(resultCode);
    }

    public PayOrderResult(ResultCode resultCode, IelsOrdersPay xcOrdersPay) {
        super(resultCode);
        this.xcOrdersPay = xcOrdersPay;
    }

    private IelsOrdersPay xcOrdersPay;
    private String orderNumber;

    //当tradeState为NOTPAY（未支付）时显示支付二维码
    private String codeUrl;
    private Float money;
}

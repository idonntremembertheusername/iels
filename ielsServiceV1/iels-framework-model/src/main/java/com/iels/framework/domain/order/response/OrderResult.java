package com.iels.framework.domain.order.response;

import com.iels.framework.domain.order.IelsOrders;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderResult extends ResponseResult {

    private IelsOrders xcOrders;

    public OrderResult(ResultCode resultCode, IelsOrders xcOrders) {
        super(resultCode);
        this.xcOrders = xcOrders;
    }
}

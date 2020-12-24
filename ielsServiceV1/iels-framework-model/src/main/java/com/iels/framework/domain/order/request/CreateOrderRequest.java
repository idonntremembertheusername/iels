package com.iels.framework.domain.order.request;

import com.iels.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateOrderRequest extends RequestData {
    String courseId;
}

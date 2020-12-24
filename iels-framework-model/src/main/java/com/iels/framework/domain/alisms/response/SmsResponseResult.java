package com.iels.framework.domain.alisms.response;

import com.iels.framework.domain.alisms.ext.SmsResponse;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description: 短信服务请求返回结果类
 * @Author: snypxk
 * @Date: 2019/12/18 21
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
public class SmsResponseResult extends ResponseResult {

    private SmsResponse smsResponse;

    public SmsResponseResult(SmsResponse smsResponse, ResultCode resultCode) {
        super(resultCode);
        this.smsResponse = smsResponse;
    }
}

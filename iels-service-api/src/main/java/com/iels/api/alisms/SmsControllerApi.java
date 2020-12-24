package com.iels.api.alisms;

import com.iels.framework.domain.alisms.response.SmsResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/18 15
 * @Other:
 **/
@Api(value = "发送短信验证", tags = {"发送短信验证"})
public interface SmsControllerApi {

    @ApiOperation("发送短信")
    public SmsResponseResult sendSms(String phone);
}

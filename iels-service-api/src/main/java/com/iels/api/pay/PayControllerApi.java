package com.iels.api.pay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/19 01
 * @Other:
 **/
@Api(value = "支付接口", tags = "支付宝支付")
public interface PayControllerApi {

    @ApiOperation("去到支付页面")
    public String gotopay(String orderId) throws Exception;

    @ApiOperation("支付成功后回调的返回URL")
    public String returnUrl(HttpServletRequest request) throws Exception;

    @ApiOperation("进行支付")
    public String payByAlipay(Model model) throws Exception;
}

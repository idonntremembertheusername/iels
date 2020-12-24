package com.iels.api.auth;

import com.iels.framework.domain.ucenter.request.LoginRequest;
import com.iels.framework.domain.ucenter.response.JwtResult;
import com.iels.framework.domain.ucenter.response.LoginResult;
import com.iels.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/16 21
 * @Other:
 **/
@Api(value = "用户认证", tags = {"用户认证接口"})
public interface AuthControllerApi {

    @ApiOperation("登录")
    public LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    public ResponseResult logout();

    @ApiOperation("查询用户的jwt令牌")
    public JwtResult userjwt();
}

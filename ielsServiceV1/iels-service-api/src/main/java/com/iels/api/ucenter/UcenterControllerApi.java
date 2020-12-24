package com.iels.api.ucenter;

import com.iels.framework.domain.ucenter.IelsTeacher;
import com.iels.framework.domain.ucenter.IelsUser;
import com.iels.framework.domain.ucenter.IelsUserInterest;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
import com.iels.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 01
 * @Other:
 **/
@Api(value = "用户中心", tags = {"用户中心管理"})
public interface UcenterControllerApi {

    @ApiOperation("根据用户账号查询用户信息")
    public IelsUserExt getUserext(String username);

    @ApiOperation("根据用户账号查询用户信息")
    public IelsTeacher getTeacherInfo();

    @ApiOperation("根据用户id查询用户信息")
    public IelsUser getUserInfo();

    @ApiOperation("更新用户信息")
    public IelsUser updateUserInfo(String id,IelsUser user);

    @ApiOperation("保存用户兴趣")
    public ResponseResult saveUserInterest(IelsUserInterest ielsUserInterest);

    @ApiOperation("用户登录")
    public IelsUser userLogin(String username,String password);

    @ApiOperation("查询用户个人信息")
    public IelsUser findUserById(String id);

    @ApiOperation("更新用户信息")
    public ResponseResult updateUser(IelsUser user);


}

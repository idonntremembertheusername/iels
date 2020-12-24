package com.iels.ucenter.controller;

import com.iels.api.ucenter.UcenterControllerApi;
import com.iels.framework.domain.ucenter.IelsTeacher;
import com.iels.framework.domain.ucenter.IelsUser;
import com.iels.framework.domain.ucenter.IelsUserInterest;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.utils.IelsOauth2Util;
import com.iels.framework.web.BaseController;
import com.iels.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 01
 * @Other:
 **/
@RestController
@RequestMapping("/ucenter")
public class UcenterController extends BaseController implements UcenterControllerApi {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping("/getuserext")
    public IelsUserExt getUserext(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }

    /*
     * @description: 根据userid查找教师信息
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.ucenter.IelsTeacher
     **/
    @Override
    @GetMapping("/teacher")
    public IelsTeacher getTeacherInfo() {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String userId = userJwt.getId();
        return userService.getTeacherInfo(userId);
    }
    /*
     * @description: 根据id查找用户信息
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    @Override
    @GetMapping("/userInfo")
    public IelsUser getUserInfo() {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String id = userJwt.getId();
        return userService.getUserInfo(id);

    }

    /*
     * @description: 更新用户信息
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    @Override
    @PostMapping("/updateUserInfo/{id}")
    public IelsUser updateUserInfo(@PathVariable("id") String id, @RequestBody IelsUser user) {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
//        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
//        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
//        if (userJwt == null) {
//            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
//        }
//        String id = userJwt.getId();
//        user.setId(id);

        IelsUser newUser=user;
        newUser.setId(id);

        return null;
    }


    /*
     * @description: 保存用户兴趣
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    @Override
    @PostMapping("/saveUserInterest")
    public ResponseResult saveUserInterest(IelsUserInterest ielsUserInterest){
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String id = userJwt.getId();
        ielsUserInterest.setUserid(id);
        return userService.saveUserInterest(ielsUserInterest);
    }

    /*
     * @description: 普通登录
     * @author: thesky
     * @param
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    @Override
    @PostMapping("/userlogin")
    public IelsUser userLogin(@RequestParam("username")String username, @RequestParam("password")String password) {
        return userService.userLogin(username,password);
    }

    @Override
    @PostMapping("/findUserInfo")
    public IelsUser findUserById(@RequestParam("id")String id) {
        return userService.findUserById(id);
    }

    @Override
    @PostMapping("/updateUser")
    public ResponseResult updateUser(IelsUser user) {

        return userService.updateUserInfo(user);
    }


}





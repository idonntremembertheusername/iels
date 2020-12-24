package com.iels.ucenter.service;

import com.github.pagehelper.util.StringUtil;
import com.iels.framework.domain.ucenter.*;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.ResponseResult;
import com.iels.ucenter.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 01
 * @Other:
 **/
@Service
public class UserService {

    @Autowired
    private IelsUserRepository ielsUserRepository;

    @Autowired
    private IelsCompanyUserRepository IelsCompanyUserRepository;

    @Autowired
    private IelsMenuMapper IelsMenuMapper;

    @Autowired
    private IelsTeacherRepository ielsTeacherRepository;

    @Autowired
    private IelsUserInterestRepository ielsUserInterestRepository;

    //根据用户账号查询用户信息
    public IelsUser findIelsUserByUsername(String username) {
        return ielsUserRepository.findByUsername(username);
    }

    //根据账号查询用户的信息，返回用户扩展信息
    public IelsUserExt getUserExt(String username) {
        //根据用户账号查询用户信息
        IelsUser IelsUser = this.findIelsUserByUsername(username);
        if (IelsUser == null) {
            return null;
        }
        IelsUserExt IelsUserExt = new IelsUserExt();
        BeanUtils.copyProperties(IelsUser, IelsUserExt);
        //用户id
        String userId = IelsUserExt.getId();
        //根据用户id查询其权限列表 -2019/12/17
        List<IelsMenu> IelsMenus = IelsMenuMapper.selectPermissionByUserId(userId);
        //设置权限
        IelsUserExt.setPermissions(IelsMenus);
        //根据用户id查询用户所属公司的id
        IelsCompanyUser IelsCompanyUser = IelsCompanyUserRepository.findByUserId(userId);
        if (IelsCompanyUser != null) {
            String companyId = IelsCompanyUser.getCompanyId();
            IelsUserExt.setCompanyId(companyId);
        }
        return IelsUserExt;
    }

    /*
     * @description: 根据userid查找教师信息
     * @author: snypxk
     * @param userid
     * @return: com.iels.framework.domain.ucenter.IelsTeacher
     **/
    public IelsTeacher getTeacherInfo(String userid) {
        return ielsTeacherRepository.findByUserId(userid);
    }

    /*
     * @description: 根据id查询用户信息
     * @author: snypxk
     * @param userid
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    public IelsUser getUserInfo(String id) {
        Optional<IelsUser> optional = ielsUserRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /*
     * @description: 更新用户信息
     * @author: snypxk
     * @param userid
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/

    public ResponseResult updateUserInfo(IelsUser user) {
        if (user!=null){
            Optional<IelsUser> optional = ielsUserRepository.findById(user.getId());
            if(optional.isPresent()){
                IelsUser ielsUser1 = optional.get();
                if(!user.getName().isEmpty()){
                    ielsUser1.setName(user.getName());
                }
                if(!user.getSex().isEmpty()){
                    ielsUser1.setSex(user.getSex());
                }
                if(!user.getPhone().isEmpty()){
                    ielsUser1.setPhone(user.getPhone());
                }
                if(!user.getEmail().isEmpty()){
                    ielsUser1.setEmail(user.getEmail());
                }
                if(!user.getUtype().isEmpty()){
                    ielsUser1.setUtype(user.getUtype());
                }

                ielsUserRepository.save(ielsUser1);
                return new ResponseResult(CommonCode.SUCCESS);
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 保存用户兴趣
     * @author: snypxk
     * @param userid
     * @return: com.iels.framework.domain.ucenter.IelsUserInterest
     **/

    @Transactional
    public ResponseResult saveUserInterest(IelsUserInterest ielsUserInterest) {
        IelsUserInterest byUserid = ielsUserInterestRepository.findByUserid(ielsUserInterest.getUserid());
        if (byUserid == null) {
            if (StringUtil.isNotEmpty(ielsUserInterest.getCategoryid())) {
                ielsUserInterestRepository.save(ielsUserInterest);
                return new ResponseResult(CommonCode.SUCCESS);
            }
        } else {
            if (StringUtil.isNotEmpty(ielsUserInterest.getCategoryid())) {
                byUserid.setCategoryid(ielsUserInterest.getCategoryid());
                ielsUserInterestRepository.save(byUserid);
                return new ResponseResult(CommonCode.SUCCESS);
            }
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /*
     * @description: 普通登录
     * @author: thesky
     * @param username,password
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    public IelsUser userLogin(String usename, String password) {
        return ielsUserRepository.findByUsernameEqualsAndPasswordEquals(usename, password);

    }

    /*
     * @description: 根据id查询用户信息
     * @author: thesky
     * @param id
     * @return: com.iels.framework.domain.ucenter.IelsUser
     **/
    public IelsUser findUserById(String id){
        return ielsUserRepository.findIelsUserById(id);
    }




}

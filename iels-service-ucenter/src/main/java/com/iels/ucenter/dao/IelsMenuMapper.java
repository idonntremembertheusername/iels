package com.iels.ucenter.dao;

import com.iels.framework.domain.ucenter.IelsMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 14
 * @Other:
 **/
@Mapper
public interface IelsMenuMapper {

    /*
     * @description: 根据用户Id获取其权限列表
     * @author: snypxk
     * @param userid
     * @return: java.util.List<com.iels.framework.domain.ucenter.IelsMenu>
     **/
    public List<IelsMenu> selectPermissionByUserId(String userid);
}

package com.iels.ucenter.dao;

import com.iels.framework.domain.ucenter.IelsCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 01
 * @Other:
 **/
public interface IelsCompanyUserRepository extends JpaRepository<IelsCompanyUser, String> {
    //根据userId查询用户的所在组织[公司]的Id
    IelsCompanyUser findByUserId(String userId);
}

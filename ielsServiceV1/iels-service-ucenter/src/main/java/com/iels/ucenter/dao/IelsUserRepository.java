package com.iels.ucenter.dao;

import com.iels.framework.domain.ucenter.IelsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 01
 * @Other:
 **/

public interface IelsUserRepository extends JpaRepository<IelsUser, String> {
    //根据账号username查询用户信息
    IelsUser findByUsername(String username);

    IelsUser findByUsernameEqualsAndPasswordEquals(String username,String password);

    IelsUser findIelsUserById(String id);




}

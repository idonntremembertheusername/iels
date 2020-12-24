package com.iels.ucenter.dao;

import com.iels.framework.domain.ucenter.IelsUserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: 学生用户感兴趣的一级课程分类
 * @Author: snypxk
 * @Date: 2020/1/5 18
 * @Other:
 **/
public interface IelsUserInterestRepository extends JpaRepository<IelsUserInterest, String> {
    IelsUserInterest findByUserid(String userid);
}

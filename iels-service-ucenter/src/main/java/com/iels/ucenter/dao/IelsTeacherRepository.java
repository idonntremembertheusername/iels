package com.iels.ucenter.dao;

import com.iels.framework.domain.ucenter.IelsTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/30 00
 * @Other:
 **/
public interface IelsTeacherRepository extends JpaRepository<IelsTeacher,String> {

    //根据userid查找教师信息
    IelsTeacher findByUserId(String userid);
}

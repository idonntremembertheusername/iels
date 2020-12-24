package com.iels.ucenter.test;

import com.iels.framework.domain.ucenter.IelsUser;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
import com.iels.framework.utils.BCryptUtil;
import com.iels.ucenter.dao.IelsUserRepository;
import com.iels.ucenter.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 15
 * @Other:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    IelsUserRepository ielsUserRepository;

    @Test
    public void testGetUserExt() {
        IelsUserExt itcast = userService.getUserExt("admin");
        System.out.println(itcast.getPassword());
        System.out.println(BCryptUtil.matches("admin", itcast.getPassword()));
    }

    @Test
    public void testUserInfo(){
        IelsUser userInfo = userService.getUserInfo("53");
        System.out.println(userInfo);
    }

    @Test
    public void testUpateUserInfo(){
        IelsUser user=new IelsUser();
        user.setId("55");
        user.setEmail("987664321@163.com");
        user.setUtype("hhhhhh");
        user.setName("lyyywu");
        user.setPassword("1242534657");
        user.setUsername("hahhhhiahi");

        userService.updateUserInfo(user);
        System.out.println(user);

    }

    @Test
    public void testLogin(){
        String usename="zhangsan";
        String password="123";
        IelsUser ielsUser = ielsUserRepository.findByUsernameEqualsAndPasswordEquals(usename, password);
        System.out.println(ielsUser);
    }

    @Test
    public void testupdate(){
        String usename="zhangsan";
        String password="123";
        IelsUser ielsUser = ielsUserRepository.findByUsernameEqualsAndPasswordEquals(usename, password);
        System.out.println(ielsUser);
    }
}

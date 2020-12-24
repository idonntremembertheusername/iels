package com.iels.auth;

import com.iels.auth.client.UserClient;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
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
public class TestUserClient {

    @Autowired
    UserClient userClient;

    @Test
    public void testGetUserExt() {
        IelsUserExt itcast = userClient.getUserext("itcast");
        System.out.println(itcast);
    }
}

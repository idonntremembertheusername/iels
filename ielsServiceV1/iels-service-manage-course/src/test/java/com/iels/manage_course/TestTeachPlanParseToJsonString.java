package com.iels.manage_course;

import com.alibaba.fastjson.JSON;
import com.iels.framework.domain.course.ext.TeachplanNode;
import com.iels.framework.utils.UUIDUtil;
import com.iels.manage_course.dao.TeachplanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestTeachPlanParseToJsonString {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Test
    public void testFeign() {
        String id = "4028e581617f945f01617f9dabc40000";
        //根据id查询teachplan
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        String jsonString = JSON.toJSONString(teachplanNode);
        System.out.println(jsonString);
    }


    @Test
    public void testDateParse() {
        String dateStr = "2011-12-30T07:59:39.000+0000";
        if (dateStr.contains("+0000")) {
            System.out.println("111111111");
            String z = dateStr.replace("+0000", "Z");
            System.out.println(z);
        }
        System.out.println(dateStr);
        System.out.println(10000);
    }

    @Test
    public void testGetUUid() {
        for (int i = 0; i < 5 ; i++) {
            System.out.println(UUIDUtil.get32UUID());
        }
    }


}

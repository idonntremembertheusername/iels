package com.iels.manage_course;

import com.iels.manage_course.client.CmsPageClient;
import com.iels.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {
    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void testFeign() {
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5a754adf6abb500ad05688d9");
        System.out.println(cmsPage);
    }
}

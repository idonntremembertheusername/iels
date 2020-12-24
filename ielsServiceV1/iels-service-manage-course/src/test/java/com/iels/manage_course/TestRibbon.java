package com.iels.manage_course;

import com.iels.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    @LoadBalanced
    public void testRibbon() {
        //服务id
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        for (int i = 0; i < 10; i++) {
            //通过服务id调用
            // 在[Ctrl+Alt+Shift+N]类: RibbonLoadBalancerClient
            // 的public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException
            // if (server == null) {}这里打一个断点测试
            ResponseEntity<CmsPage> forEntity =
                    restTemplate.getForEntity("http://" + serviceId + "/cms/page/get/5a754adf6abb500ad05688d9", CmsPage.class);
            CmsPage cmsPage = forEntity.getBody();
            System.out.println(cmsPage);
        }
    }
}

package com.iels.auth.client;

import com.iels.framework.client.IelsServiceList;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/17 01
 * @Other:
 **/
@FeignClient(value = IelsServiceList.IELS_SERVICE_UCENTER)
public interface UserClient {

    @GetMapping("/ucenter/getuserext")
    public IelsUserExt getUserext(@RequestParam("username") String username);
}

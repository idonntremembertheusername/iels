package com.iels.manage_course.client;

import com.iels.framework.client.IelsServiceList;
import com.iels.framework.domain.cms.CmsPage;
import com.iels.framework.domain.cms.response.CmsPageResult;
import com.iels.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/4 20
 * @Other:
 **/
@FeignClient(IelsServiceList.IELS_SERVICE_MANAGE_CMS) //指定远程调用的服务名称
public interface CmsPageClient {
    //根据页面ID查询页面信息,远程调用cms请求数据
    @GetMapping("/cms/page/get/{id}")
    public CmsPage findCmsPageById(@PathVariable("id") String id);

    //添加页面,用于课程预览
    @PostMapping("/cms/page/save")
    public CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);

    //一键发布页面
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}

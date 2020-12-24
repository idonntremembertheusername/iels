package com.iels.manage_media.service;

import com.iels.framework.domain.media.request.QueryMediaFileRequest;
import com.iels.framework.model.response.QueryResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author: snypxk
 * @Date: 2019/12/11 19
 * @Other:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMediaFileService {

    @Autowired
    MediaFileService mediaFileService;

    @Test
    public void testMergeFile() {
        QueryMediaFileRequest queryMediaFileRequest = new QueryMediaFileRequest();
        queryMediaFileRequest.setTag("");
        queryMediaFileRequest.setProcessStatus("303002");
        queryMediaFileRequest.setFileOriginalName("语言发展");
        QueryResponseResult result = mediaFileService
                .findListByCompanyIdOrByUserId(1, 10, queryMediaFileRequest, "48", "1");
        System.out.println(result.getQueryResult().getList());
    }

}

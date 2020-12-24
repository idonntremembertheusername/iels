package com.iels.manage_media.controller;

import com.iels.api.media.MediaFileControllerApi;
import com.iels.framework.domain.media.request.QueryMediaFileRequest;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.utils.IelsOauth2Util;
import com.iels.framework.web.BaseController;
import com.iels.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/12 15
 * @Other:
 **/
@RestController
@RequestMapping("/media/file")
public class MediaFileController extends BaseController implements MediaFileControllerApi {

    @Autowired
    private MediaFileService mediaFileService;

    /*
     * @description: 媒资文件列表查询-查询所有媒资
     * @author: snypxk
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return: com.xuecheng.framework.model.response.QueryResponseResult
     **/
    @Override
    @GetMapping("/list/all/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,
                                        @PathVariable("size") int size,
                                        QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileService.findList(page, size, queryMediaFileRequest);
    }

    /*
     * @description: 根据教师用户ID查询媒资文件列表
     * @author: snypxk
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return: com.iels.framework.model.response.QueryResponseResult
     **/
    @Override
    @GetMapping("/list/userId/{page}/{size}")
    public QueryResponseResult findListByUserId(@PathVariable("page") int page,
                                                @PathVariable("size") int size,
                                                QueryMediaFileRequest queryMediaFileRequest) {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String userId = userJwt.getId();
        return mediaFileService.findListByUserId(page, size, queryMediaFileRequest, userId);
    }

    /*
     * @description: 根据教师用户所在的公司的id查询媒资文件列表,如果教师用户没有对应的公司ID,则根据教师用户ID查询
     * @author: snypxk
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return: com.iels.framework.model.response.QueryResponseResult
     **/
    @Override
    @GetMapping("/list/companyid/{page}/{size}")
    public QueryResponseResult findListByCompanyIdOrByUserId(@PathVariable("page") int page,
                                                             @PathVariable("size") int size,
                                                             QueryMediaFileRequest queryMediaFileRequest) {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String userId = userJwt.getId();
        String companyId = userJwt.getCompanyId();
        return mediaFileService.findListByCompanyIdOrByUserId(page, size, queryMediaFileRequest, userId, companyId);
    }
}

package com.iels.manage_media.service;

import com.iels.framework.domain.media.MediaFile;
import com.iels.framework.domain.media.request.QueryMediaFileRequest;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.QueryResult;
import com.iels.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/12 15
 * @Other:
 **/
@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    /*
     * @description: 媒资文件列表查询
     * @author: snypxk
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return: com.xuecheng.framework.model.response.QueryResponseResult
     **/
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        MediaFile mediaFile = new MediaFile();
        //定义条件匹配器对象: tag 和 文件原始名称 进行模糊匹配, 处理状态 进行精确匹配
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact());
        //查询条件对象
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        //定义Example对象
        Example<MediaFile> example = Example.of(mediaFile, exampleMatcher);
        //定义Pageable对象
        Pageable pageable = PageRequest.of(page, size);
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        QueryResult<MediaFile> queryResult = new QueryResult<MediaFile>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /*
     * @description: 根据教师用户ID查询媒资文件列表
     * @author: snypxk
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return: com.iels.framework.model.response.QueryResponseResult
     **/
    public QueryResponseResult findListByUserId(int page, int size, QueryMediaFileRequest queryMediaFileRequest, String userId) {
        MediaFile mediaFile = new MediaFile();
        //定义条件匹配器对象: tag 和 文件原始名称 进行模糊匹配, 处理状态 进行精确匹配
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact());
        //查询条件对象
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        if (StringUtils.isNotEmpty(userId)) {
            mediaFile.setUserId(userId);
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        //定义Example对象
        Example<MediaFile> example = Example.of(mediaFile, exampleMatcher);
        //定义Pageable对象
        Pageable pageable = PageRequest.of(page, size);
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        QueryResult<MediaFile> queryResult = new QueryResult<MediaFile>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /*
     * @description: 根据教师用户所在的公司的id查询媒资文件列表,如果教师用户没有对应的公司ID,则根据教师用户ID查询
     * @author: snypxk
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return: com.iels.framework.model.response.QueryResponseResult
     **/
    public QueryResponseResult findListByCompanyIdOrByUserId(int page, int size, QueryMediaFileRequest queryMediaFileRequest,
                                                             String userId, String companyId) {
        MediaFile mediaFile = new MediaFile();
        ExampleMatcher exampleMatcher = null;

        //查询条件对象
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        if (StringUtils.isNotEmpty(userId)) {
            mediaFile.setUserId(userId);
        }
        if (StringUtils.isNotEmpty(companyId)) {
            mediaFile.setCompanyId(companyId);
            //定义条件匹配器对象: tag 和 文件原始名称 进行模糊匹配, 处理状态 进行精确匹配
            exampleMatcher = ExampleMatcher.matching()
                    .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("companyId", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            //定义条件匹配器对象: tag 和 文件原始名称 进行模糊匹配, 处理状态 进行精确匹配
            exampleMatcher = ExampleMatcher.matching()
                    .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        //定义Example对象
        Example<MediaFile> example = Example.of(mediaFile, exampleMatcher);
        //定义Pageable对象
        Pageable pageable = PageRequest.of(page, size);
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        QueryResult<MediaFile> queryResult = new QueryResult<MediaFile>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}

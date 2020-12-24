package com.iels.framework.domain.media.request;

import com.iels.framework.model.request.RequestData;
import lombok.Data;

/**
 * @Description: 媒资条件查询对象
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@Data
public class QueryMediaFileRequest extends RequestData {
    //原始名称
    private String fileOriginalName;
    //处理状态
    private String processStatus;
    //标签
    private String tag;
}

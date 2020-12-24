package com.iels.framework.domain.course.response;

import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {

    //预览url
    String previewUrl;

    public CoursePublishResult(ResultCode resultCode, String previewUrl) {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}

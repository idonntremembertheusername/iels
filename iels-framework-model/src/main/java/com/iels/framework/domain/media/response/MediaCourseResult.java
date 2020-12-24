package com.iels.framework.domain.media.response;

import com.iels.framework.domain.media.MediaFile;
import com.iels.framework.domain.media.MediaVideoCourse;
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
public class MediaCourseResult extends ResponseResult {

    //媒资视频信息对象
    MediaFile mediaVideo;
    //课程媒资视频信息对象
    MediaVideoCourse mediaVideoCourse;

    public MediaCourseResult(ResultCode resultCode, MediaVideoCourse mediaVideoCourse) {
        super(resultCode);
        this.mediaVideoCourse = mediaVideoCourse;
    }
}

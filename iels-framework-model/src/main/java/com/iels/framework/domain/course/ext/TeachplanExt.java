package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

/**
 * @Description: 课程计划扩展类
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
public class TeachplanExt extends Teachplan {
    //媒资文件id
    private String mediaId;

    //媒资文件原始名称
    private String mediaFileOriginalName;

    //媒资文件访问地址
    private String mediaUrl;
}

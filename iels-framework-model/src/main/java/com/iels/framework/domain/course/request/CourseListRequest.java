package com.iels.framework.domain.course.request;

import com.iels.framework.model.request.RequestData;
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
public class CourseListRequest extends RequestData {
    //公司Id
    private String companyId;
    //课程名称
    private String name;
    //课程创建者ID
    private String userId;
}

package com.iels.framework.domain.course.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 课程费用等信息 - 扩展类 [把日期转成字符串来接收前台发过来的数据]
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
public class CourseMarketEx {

    private String id;
    private String charge;
    private String valid;
    private String qq;
    private Float price;
    private Float price_old;
    private String startTime;
    private String endTime;
}

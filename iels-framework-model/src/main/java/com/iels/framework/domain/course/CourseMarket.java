package com.iels.framework.domain.course;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 课程费用等信息
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "course_market")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class CourseMarket {
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(length = 32)
    private String id;

    private String charge;
    private String valid;
    private String qq;
    private Float price;
    private Float price_old;

    @Column(name = "start_time")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Column(name = "end_time")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}

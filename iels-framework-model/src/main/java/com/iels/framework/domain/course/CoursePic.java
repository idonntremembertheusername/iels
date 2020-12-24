package com.iels.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description: 课程图片
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name = "course_pic")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class CoursePic implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;

    @Id
    @GeneratedValue(generator = "jpa-assigned")
    private String courseid;    //课程id

    private String pic;         //图片id
}

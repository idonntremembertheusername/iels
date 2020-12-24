package com.iels.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description: 课程教学计划 - 相当于课程目录
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name = "teachplan")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Teachplan implements Serializable {
    private static final long serialVersionUID = -916357110051689485L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    private String pname;       //层级，分为1、2、3级
    private String parentid;    //父级课程计划ID
    private String grade;       //层级，分为1、2、3级
    private String ptype;       //课程类型: [1]视频 [2]文档
    private String description; //章节及课程时介绍
    private String courseid;    //课程id
    private String status;      //状态：未发布、已发布
    private Integer orderby;    //排序字段
    private Double timelength;  //时长, 单位:分钟
    private String trylearn;    //是否试学
}

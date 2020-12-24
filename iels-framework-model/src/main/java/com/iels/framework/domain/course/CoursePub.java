package com.iels.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 课程发布 - 把四张表的数据集合到一起
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name = "course_pub")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class CoursePub implements Serializable {
    private static final long serialVersionUID = -916357110051689487L;

    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(length = 32)
    private String id;

    // ** 对应表格: course_base
    private String name;        //课程名称
    private String users;       //适用人群
    private String mt;          //大分类
    private String st;          //小分类
    private String grade;       //课程等级
    private String studymodel;  //学习模式
    private String teachmode;   //教育模式
    private String description; //课程介绍

    // ** 对应表格: course_pic
    private String pic;         //课程图片

    // ** 对应表格: course_market
    private Date timestamp;     //时间戳logstash使用
    private String charge;      //收费规则，对应数据字典
    private String valid;       //有效性，对应数据字典
    private String qq;          //咨询qq
    private Double price;       //价格
    private Double price_old;   //原价格
    private String expires;     //过期时间

    // ** 对应表格: teachplan
    private String teachplan;   //课程计划: 根据课程ID在表teachplan中查询得到后转成JSON字符串再存入course_pub
    @Column(name = "pub_time")
    private String pubTime;     //课程发布时间

}

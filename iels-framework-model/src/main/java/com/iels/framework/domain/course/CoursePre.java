package com.iels.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 课程预览
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name="course_pre")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class CoursePre implements Serializable {
    private static final long serialVersionUID = -916357110051689488L;

    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(length = 32)
    private String id;

    private String name;        //课程名称
    private String users;       //适用人群
    private String mt;          //大分类
    private String st;          //小分类
    private String grade;       //课程等级
    private String studymodel;  //学习模式
    private String description; //课程介绍
    private String pic;         //图片
    private Date timestamp;     //时间戳
    private String charge;      //收费规则，对应数据字典
    private String valid;       //有效性，对应数据字典
    private String qq;          //咨询qq
    private Float price;        //价格
    private Float price_old;    //原价格
    private Date expires;       //过期时间
    private String teachplan;   //课程计划
}

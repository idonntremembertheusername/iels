package com.iels.framework.domain.course;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description: 课程基础信息
 * @Author: snypxk
 * @Date: 2019/12/4 07
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "course_base")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseBase implements Serializable {
    private static final long serialVersionUID = -916357110051689486L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    private String name;        //课程名称
    private String users;       //适用人群
    private String mt;          //大分类
    private String st;          //小分类
    private String grade;       //课程等级
    private String studymodel;  //学习模式
    private String teachmode;   //教学模式
    private String description; //课程介绍
    private String status;      //课程状态

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "user_id")
    private String userId;

    @Transient
    private String mtName;

    @Transient
    private String stName;

}

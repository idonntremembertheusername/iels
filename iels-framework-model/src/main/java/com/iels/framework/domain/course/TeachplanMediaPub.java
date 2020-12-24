package com.iels.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 当课程发布后,课程的信息基本全部确定,把课程的媒资信息集成到此类,供Logstash索引到ES中
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name = "teachplan_media_pub")
@GenericGenerator(name = "jpa‐assigned", strategy = "assigned")
public class TeachplanMediaPub implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa‐assigned")
    @Column(name = "teachplan_id")
    private String teachplanId;             //课程计划id

    @Column(name = "media_id")
    private String mediaId;                 //媒资文件id

    @Column(name = "media_fileoriginalname")
    private String mediaFileOriginalName;   //媒资文件的原始名称

    @Column(name = "media_url")
    private String mediaUrl;                //媒资文件访问地址

    @Column(name = "courseid")
    private String courseId;                //课程Id

    @Column(name = "timestamp")
    private Date timestamp;                 //时间戳
}

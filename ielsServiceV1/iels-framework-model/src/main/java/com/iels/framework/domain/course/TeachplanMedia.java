package com.iels.framework.domain.course;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description: 课程计划[章节]对应的视频媒资资源信息
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name = "teachplan_media")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class TeachplanMedia implements Serializable {
    private static final long serialVersionUID = -916357110051689485L;

    @Id
    @GeneratedValue(generator = "jpa-assigned")
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
}

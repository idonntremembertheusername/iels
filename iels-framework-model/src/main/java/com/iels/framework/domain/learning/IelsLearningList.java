package com.iels.framework.domain.learning;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: TODO
 * @author: snypxk
 * @return:
 **/
@Data
@ToString
@Entity
@Table(name = "iels_learning_list")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class IelsLearningList implements Serializable {
    private static final long serialVersionUID = -916357210051689799L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "course_id")
    private String courseId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    private String status;

}

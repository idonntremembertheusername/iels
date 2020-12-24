package com.iels.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2020/1/4 13
 * @Other:
 **/
@Data
@ToString
@Entity
@Table(name = "iels_user_interest")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class IelsUserInterest {

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String userid;

    private String categoryid;
}
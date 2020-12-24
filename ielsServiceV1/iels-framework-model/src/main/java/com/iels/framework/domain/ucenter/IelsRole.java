package com.iels.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "iels_role")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class IelsRole {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "role_name")
    private String roleName;
    @Column(name = "roleCode")
    private String role_code;
    private String description;
    private String status;
    @Column(name = "createTime")
    private Date create_time;
    @Column(name = "update_time")
    private Date updateTime;
}
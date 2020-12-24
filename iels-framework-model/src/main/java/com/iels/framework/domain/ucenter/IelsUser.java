package com.iels.framework.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "iels_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class IelsUser {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    String id;

    String username;
    String password;
    String salt;
    String name;
    String utype;
    String birthday;
    String userpic;
    String sex;
    String email;
    String phone;
    String status;
    @Column(name = "create_time")
    Date createTime;
    @Column(name = "update_time")
    Date updateTime;


}

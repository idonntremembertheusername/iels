package com.iels.framework.domain.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/*
 * @description: 该实体类是用户支付成功后对应的数据实体类
 * @author: snypxk
 **/
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "iels_orders_pay")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class IelsOrdersPay implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "order_number")
    private String orderNumber;     //订单号

    @Column(name = "pay_number")    //支付系统订单号
    private String payNumber;

    private String status;          //交易状态
}

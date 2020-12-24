package com.iels.framework.domain.alisms.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/18 21
 * @Other:
 **/
@Data
@ToString
@NoArgsConstructor
public class SmsResponse {
    //手机号
    String phone;
    //验证码
    String code;
}

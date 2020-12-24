package com.iels.framework.domain.ucenter.response;

import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class LoginResult extends ResponseResult {

    public LoginResult(ResultCode resultCode, String token) {
        super(resultCode);
        this.token = token;
    }

    private String token;
}

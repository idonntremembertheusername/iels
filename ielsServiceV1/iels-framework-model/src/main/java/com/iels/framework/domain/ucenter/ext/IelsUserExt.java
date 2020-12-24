package com.iels.framework.domain.ucenter.ext;

import com.iels.framework.domain.ucenter.IelsMenu;
import com.iels.framework.domain.ucenter.IelsUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class IelsUserExt extends IelsUser {

    //权限信息
    private List<IelsMenu> permissions;

    //企业信息
    private String companyId;
}

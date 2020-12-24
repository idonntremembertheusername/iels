package com.iels.framework.domain.ucenter.ext;

import com.iels.framework.domain.course.ext.CategoryNode;
import com.iels.framework.domain.ucenter.IelsMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class IelsMenuExt extends IelsMenu {

    List<CategoryNode> children;
}

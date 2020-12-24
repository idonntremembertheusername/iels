package com.iels.framework.domain.cms.ext;

import com.iels.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

}

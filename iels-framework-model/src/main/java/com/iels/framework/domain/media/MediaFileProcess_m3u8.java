package com.iels.framework.domain.media;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@Data
@ToString
public class MediaFileProcess_m3u8 extends MediaFileProcess {
    //ts列表
    private List<String> tslist;
}

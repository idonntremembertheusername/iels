package com.iels.framework.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 操作数据库表的查询结果
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
@Data
@ToString
public class QueryResult<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private long total;
}
package com.iels.framework.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: 通用数据库查询结果返回类
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
        this.queryResult = queryResult;
    }
}

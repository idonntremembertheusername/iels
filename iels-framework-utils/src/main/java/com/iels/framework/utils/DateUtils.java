package com.iels.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/27 15
 * @Other:
 **/
public class DateUtils {
    /*
     * @description: 将 字符串日期 [2019-06-03T16:00:00.000Z] 格式 转换为 Date [2019-06-03 16:00:00] 格式
     * @author: snypxk
     * @param oldDateStr
     * @return: java.util.Date
     **/
    public static Date parse(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        Date date = null;
        Date date1 = null;
        try {
            if(dateStr.contains("+0000")){
                dateStr = dateStr.replace("+0000","Z");
            }
            dateStr = dateStr.replace("Z", " UTC");  //是空格+UTC
            //System.out.println(dateStr);
            //System.out.println(10000);
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            date1 = df1.parse(dateStr);
            SimpleDateFormat df2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date = df2.parse(date1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

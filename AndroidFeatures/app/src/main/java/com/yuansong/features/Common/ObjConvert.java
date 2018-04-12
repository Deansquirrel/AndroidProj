package com.yuansong.features.Common;

/**
 * Created by yuansong on 2018/2/28.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ObjConvert {

    /**
     * 返回日期时间字符串
     * @param date
     * @return
     */
    public static String getDateStr(java.util.Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDateStr(java.util.Date date,String format) {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 根据字符串返回日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static java.util.Date getDateFromStr(String date,String format) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    /**
     * 转换 java.sql.Date 至 java.util.Date
     * @param date
     * @return
     */
    public static java.util.Date convertSqlDateToUtilDate(java.sql.Date date){
        return new java.util.Date(date.getTime());
    }

    /**
     * 转换 java.util.Date 至 java.sql.Date
     * @param date
     * @return
     */
    public static java.sql.Date convertUtilDateToSqlDate(java.util.Date date){
        return new java.sql.Date(date.getTime());
    }
}

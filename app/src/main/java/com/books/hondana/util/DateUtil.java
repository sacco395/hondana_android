package com.books.hondana.util;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/09/06.
 */
public class DateUtil {

    public static boolean isOneYearAfter(String dateString ) {
        boolean status=false;

        // 現在日時の取得
        Date now = new Date();
        Calendar cNow = Calendar.getInstance();
        cNow.setTime(now);
        int nowYear = cNow.get(Calendar.YEAR);
        int nowMonth = cNow.get(Calendar.MONTH);

        try {
            dateString.trim();       // スペースを取り除く
            DateFormat df = new SimpleDateFormat("yyyy年mm月");
            Date targetDate = df.parse(dateString);
            Calendar cTarget = Calendar.getInstance();
            cTarget.setTime(targetDate);
            int targetYear = cTarget.get(Calendar.YEAR);
            int targetMonth = cTarget.get(Calendar.MONTH);

            if ( targetYear < (nowYear-1) ){
                status = true; // 1年以上前
            } else if(targetYear == (nowYear-1)){
                if ( targetMonth <= nowMonth ){
                    status = true;
                } else {
                    status = false;
                }
            } else {
                status = false;
            }

        } catch(ParseException e){
            e.printStackTrace();
            status = false; // Parse失敗
        }
        return status;
    }
}
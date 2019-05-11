/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author hp
 */
public class DateTimeConvertor {
    public static String timeStampToDate(Timestamp timestamp){
        Date time=new Date(timestamp.getTime());
        String pattern = "dd-MM-yy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(time);
        return date;
    }
    public static int diffInDays(Timestamp timestamp){
        return (int)(System.currentTimeMillis()-timestamp.getTime())/(1000*60*60*24);
    }
}

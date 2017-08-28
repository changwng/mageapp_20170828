package com.example.foo.mageapp.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by foo on 8/27/17.
 */

public class Test {

    public String getUTC(Date date) {
        /*Calendar cal = Calendar.getInstance();
        TimeZone tzone = TimeZone.getTimeZone("GMT");
        cal.setTimeZone(tzone);
        Date date = cal.getTime();
        return date.toString();*/

        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmt = sdf.format(date);
        return gmt;
    }
}

package by.pavka.memento.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CalendarConverter {
    private static DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

    private CalendarConverter() {
    }
    public static Calendar intoCalendar(String date) {
        Calendar cal = Calendar.getInstance();
        String[] dates = date.split("/");
        int month = Integer.parseInt(dates[0]);
        int year = Integer.parseInt(dates[1]);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    public static String showDate(Calendar end) {
        if (end == null) {
            return "";
        }
        return df.format(end.getTime());
    }

    public static String showDate(Date date) {
        if (date == null) {
            return "";
        }
        return df.format(date);
    }

    public static Calendar fromString(String sDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = df.parse(sDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static String showTime(int hour, int minute) {
        String h = hour < 10? "0" + hour : "" + hour;
        String m = minute < 10? "0" + minute : "" + minute;
        return h + ":" + m;
    }

    public static int showProgress(Calendar start, Calendar end) {
        Calendar now = Calendar.getInstance();
        int progress = (int)(100 * (now.getTimeInMillis() - start.getTimeInMillis()) / (end.getTimeInMillis() - start.getTimeInMillis()));

        return progress;
    }

    public static int showProgress (Calendar start, Calendar end, int initial) {
        Calendar now = Calendar.getInstance();
        int progress = initial + (int)((100 - initial) * (now.getTimeInMillis() - start.getTimeInMillis()) / (end.getTimeInMillis() - start.getTimeInMillis()));
        return progress;
    }
}

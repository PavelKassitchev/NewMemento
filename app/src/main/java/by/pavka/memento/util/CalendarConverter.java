package by.pavka.memento.util;

import java.text.DateFormat;
import java.util.Calendar;

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
        return df.format(end.getTime());
    }

    public static String showTime(int hour, int minute) {
        String h = hour < 10? "0" + hour : "" + hour;
        String m = minute < 10? "0" + minute : "" + minute;
        return h + ":" + m;
    }
}

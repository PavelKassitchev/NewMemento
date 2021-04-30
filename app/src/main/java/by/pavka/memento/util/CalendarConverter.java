package by.pavka.memento.util;

import java.util.Calendar;

public class CalendarConverter {

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
}

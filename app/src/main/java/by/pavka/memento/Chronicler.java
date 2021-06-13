package by.pavka.memento;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Chronicler {
    private Map<Calendar, Double> chronicle;
    private Calendar latestDate;

    public Chronicler() {
        chronicle = new HashMap<Calendar, Double>();
    }

    public boolean addRecord(Calendar calendar, double weight) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        chronicle.put(calendar, weight);
        if (latestDate == null || latestDate.before(calendar) || latestDate.equals(calendar)) {
            latestDate = calendar;
            return true;
        }
        return false;
    }


}

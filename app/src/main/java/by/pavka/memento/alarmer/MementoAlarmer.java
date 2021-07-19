package by.pavka.memento.alarmer;

import java.util.Calendar;

public class MementoAlarmer {
    private Calendar calendar;

    public MementoAlarmer() {
        calendar = Calendar.getInstance();
    }

    public long tillNextAlarm(boolean[] week, int hour, int minute, boolean exact) {
        int day = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 6 : calendar.get(Calendar.DAY_OF_WEEK) - 2;
        Calendar next = Calendar.getInstance();
        next.set(Calendar.HOUR_OF_DAY, hour);
        next.set(Calendar.MINUTE, minute);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);
        long diff = next.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if (!exact && diff < 300000) {
            diff = -1;
        }
        if (week[day] && diff >= 0) {
            return diff / 1000;
        } else {
            return getDayGap(day, week) * 3600 * 24 + diff / 1000;
        }
    }

    private int getDayGap(int day, boolean[] week) {
        int gap = 0;
        for (int i = day + 1; i < week.length; i++) {
            gap++;
            if (week[i]) {
                return gap;
            }
        }
        for (int i = 0; i < day + 1; i++) {
            gap++;
            if (week[i]) {
                return gap;
            }
        }
        return -1;
    }

    public long tillEnd(Calendar end) {
        if (end != null) {
            return ((end.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000);
        } else {
            return -1;
        }
    }
}

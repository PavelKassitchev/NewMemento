package by.pavka.memento.user;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import by.pavka.memento.util.CalendarConverter;

public class Chronicler {
    private TreeMap<String, Double> chronicle;
    private Calendar latestDate;

    public Chronicler() {
        chronicle = new TreeMap<>(new ChronicleComaparator());
    }

    public boolean addRecord(Calendar calendar, double weight) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        String date = CalendarConverter.showDate(calendar);
        chronicle.put(date, weight);
        if (latestDate == null || latestDate.before(calendar) || latestDate.equals(calendar)) {
            latestDate = calendar;
            return true;
        }
        return false;
    }

    public double removeRecord(Calendar calendar) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        if (chronicle.size() > 1) {
            chronicle.remove(CalendarConverter.showDate(calendar));
            latestDate = findLastDate();
        }
        if (latestDate != null) {
            return chronicle.get(CalendarConverter.showDate(latestDate));
        } else {
            return 0.0;
        }
    }

    public double findLastWeight() {
        if (latestDate == null) {
            return 0;
        }
        return chronicle.get(CalendarConverter.showDate(latestDate));
    }

    private Calendar findLastDate() {
        List<String> dates = new ArrayList<>(chronicle.keySet());
        Collections.sort(dates, new ChronicleComaparator());
        String last = dates.get(dates.size() - 1);
        return CalendarConverter.fromString(last);
    }

    public DataPoint[] getDataSeries() {
        DataPoint[] dataSeries = new DataPoint[chronicle.size()];
        TreeMap<String, Double> copy = new TreeMap<>(chronicle);

        for (int i = 0; i < dataSeries.length; i++) {
            Map.Entry<String, Double> entry = copy.pollFirstEntry();
            Date date = CalendarConverter.fromString(entry.getKey()).getTime();
            double weight = entry.getValue();
            dataSeries[i] = new DataPoint(date, weight);
        }
        Arrays.sort(dataSeries, (o1, o2) -> {
            double d1 = o1.getX();
            double d2 = o2.getX();
            return (int)(d1 - d2);
        });
        return dataSeries;
    }


    public int getTimeIndex(Calendar calendar) {
        TreeMap<String, Double> copy = new TreeMap<>(chronicle);
        int index = 0;
        while (!copy.isEmpty() && CalendarConverter.fromString(copy.pollFirstEntry().getKey()).before(calendar)) {
            index++;
        }
        return index;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<String, Double> entry : chronicle.entrySet()) {
            result += " " + entry.getKey() + " hash: " + entry.getKey().hashCode() + ": " + entry.getValue();
        }
        return result;
    }

    private static class ChronicleComaparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (CalendarConverter.fromString(o1).getTimeInMillis() > CalendarConverter.fromString(o2).getTimeInMillis()) {
                return 1;
            } else if (CalendarConverter.fromString(o1).getTimeInMillis() == CalendarConverter.fromString(o2).getTimeInMillis()) {
                return 0;
            }
            return -1;
        }
    }
}

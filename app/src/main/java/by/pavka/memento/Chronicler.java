package by.pavka.memento;

import android.text.format.DateUtils;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import by.pavka.memento.util.CalendarConverter;

public class Chronicler {
    private TreeMap<String, Double> chronicle;
    private Calendar latestDate;

    public Chronicler() {
        chronicle = new TreeMap<String, Double>(new ChronicleComaparator());
    }

    public boolean addRecord(Calendar calendar, double weight) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        String date = CalendarConverter.showDate(calendar);
        Log.d("CHRON", "Add Record: " + date + ": " + weight);
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
            Log.d("CHRON", "SUCCESSFUL REMOVE: "
                    + toString(calendar) + " contains: "
                    + CalendarConverter.showDate(calendar) + " hash: " + CalendarConverter.showDate(calendar).hashCode()
                    + " " + chronicle.containsKey(CalendarConverter.showDate(calendar)));
            chronicle.remove(CalendarConverter.showDate(calendar));
            latestDate = findLastDate();
        }
        Log.d("CHRON", "REMOVE: " + CalendarConverter.showDate(calendar) + " FROM " + toString());
        if (latestDate != null) {
            return chronicle.get(CalendarConverter.showDate(latestDate));
        } else {
            return 0.0;
        }
    }

    public Calendar getLatestDate() {
        return latestDate;
    }

    public double findLastWeight() {
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
            Log.d("Date", "Date = " + date);
            double weight = entry.getValue();
            dataSeries[i] = new DataPoint(date, weight);
        }
        Arrays.sort(dataSeries, new Comparator<DataPoint>() {

            @Override
            public int compare(DataPoint o1, DataPoint o2) {
                double d1 = o1.getX();
                double d2 = o2.getX();
                return (int)(d1 - d2);
            }
        });
        return dataSeries;
    }


    public int getTimeIndex(Calendar calendar) {
        TreeMap<String, Double> copy = new TreeMap<>(chronicle);
        int index = 0;
        while (!copy.isEmpty() && CalendarConverter.fromString(copy.pollFirstEntry().getKey()).before(calendar)) {
            index++;
        }
        Log.d("POLL", "index = " + index);
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

    public String toString(Calendar calendar) {
        String result = "";
        for (Map.Entry<String, Double> entry : chronicle.entrySet()) {
            result += " " + entry.getKey() + " hash: " + entry.getKey().hashCode() + ": " + entry.getValue() + " is Equal? "
                    + entry.getKey().equals(CalendarConverter.showDate(calendar)) + " Same hash? "
                    + (entry.getKey().hashCode() == CalendarConverter.showDate(calendar).hashCode());
        }
        return result;
    }

    private class ChronicleComaparator implements Comparator<String> {

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

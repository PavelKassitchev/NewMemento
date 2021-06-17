package by.pavka.memento;

import com.jjoe64.graphview.series.DataPoint;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Chronicler {
    private Map<Calendar, Double> chronicle;
    private Calendar latestDate;

    public Chronicler() {
        chronicle = new TreeMap<Calendar, Double>(new Comparator<Calendar>() {
            @Override
            public int compare(Calendar o1, Calendar o2) {
                if (o1.getTimeInMillis() > o2.getTimeInMillis()) {
                    return 1;
                }
                return -1;
            }
        });
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

    public DataPoint[] getDataSeries() {
        DataPoint[] dataSeries = new DataPoint[chronicle.size()];
        TreeMap<Calendar, Double> copy = new TreeMap<>(chronicle);
        for (int i = 0; i < dataSeries.length; i++) {
            Map.Entry<Calendar, Double> entry = copy.pollFirstEntry();
            Date date = entry.getKey().getTime();
            double weight = entry.getValue();
            dataSeries[i] = new DataPoint(date, weight);
        }
        return dataSeries;
    }


    public int getTimeIndex(Calendar calendar) {
        TreeMap<Calendar, Double> copy = new TreeMap<>(chronicle);
        int index = 0;
        while (copy.pollFirstEntry().getKey().before(calendar)) {
            index++;
        }
        return index;
    }


}

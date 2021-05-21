package by.pavka.memento.habit;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.user.User;

public class ActivizationViewModel extends AndroidViewModel {

    private MementoApplication app;
    private Habit habit;
    //private LocalDate end;
    private Calendar end;
    private boolean[] week;
    //private LocalTime time;
    private int hour;
    private int minute;
    private boolean clearance;
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

    public ActivizationViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication) application;
    }

    public boolean isClearance() {
        return clearance;
    }

    public void setClearance(boolean clearance) {
        this.clearance = clearance;
    }

    public Habit getHabit() {
        return habit;
    }

    public void clearHabit() {
        habit = null;
        end = null;
        week = null;
        //time = null;
        hour = 0;
        minute = 0;
        clearance = false;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        HabitProgress progress = habits.get(habit);
        //end = progress.getEndDate() == null ? LocalDate.now().plusDays(MementoApplication.DAYS_FOR_HABIT) : progress.getEndDate();
        if (progress.getEndDate() != null) {
            end = progress.getEndDate();
            week = progress.getWeek();
            hour = progress.getHour();
            minute = progress.getMinute();
        } else {
            week = new boolean[]{false, false, false, false, false, false, false};
            Calendar last = Calendar.getInstance();
            hour = last.get(Calendar.HOUR_OF_DAY);
            minute = last.get(Calendar.MINUTE);
            last.add(Calendar.DATE, MementoApplication.DAYS_FOR_HABIT);
            end = last;
        }
    }

    public void resetProgress(boolean cleared) {
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        if (cleared) {
            habits.put(habit, new HabitProgress(HabitStatus.ENABLED));
        } else {
            end.set(Calendar.HOUR_OF_DAY, hour);
            end.set(Calendar.MINUTE, minute);
            habits.put(habit, new HabitProgress(HabitStatus.ACTIVE, Calendar.getInstance(), end, week, hour, minute));
            user.setHabitCustomized(true);
            app.customizeHabits(true);
        }
        clearHabit();
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public Calendar getEnd() {
        return end;
    }

    public boolean[] getWeek() {
        return week;
    }

    public void setWeek(boolean[] week) {
        this.week = week;
    }

    public boolean getDay(int i) {
        return week[i];
    }

    public void setDay(int i, boolean isChecked) {
        week[i] = isChecked;
    }

//    public LocalTime getTime() {
//        return time;
//    }
//
//    public void setTime(LocalTime time) {
//        this.time = time;
//    }


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}

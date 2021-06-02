package by.pavka.memento.habit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

import by.pavka.memento.MementoApplication;

public class HabitProgress {
    private HabitStatus habitStatus;
    private Calendar startDate;
    private Calendar endDate;
    private boolean[] week;
    //    private LocalTime time;
    private int hour;
    private int minute;

    public HabitProgress(HabitStatus habitStatus) {
        week = new boolean[7];
        this.habitStatus = habitStatus;
    }

    public HabitProgress(HabitStatus habitStatus, Calendar startDate, Calendar endDate, boolean[] week, int hour, int minute) {
        this.habitStatus = habitStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.week = week;
        this.hour = hour;
        this.minute = minute;
    }

    //    public HabitProgress(HabitStatus habitStatus, LocalDate startDate) {
//        this.habitStatus = habitStatus;
//        this.startDate = startDate;
//    }

    public HabitProgress() {
        habitStatus = HabitStatus.ENABLED;
    }

//    public HabitProgress(HabitStatus habitStatus, LocalDate startDate, LocalDate endDate) {
//        this.habitStatus = habitStatus;
//        this.startDate = startDate;
//        this.endDate = endDate;
//    }
//
//    public HabitProgress(HabitStatus habitStatus, LocalDate startDate, LocalDate endDate, boolean[] week) {
//        this.habitStatus = habitStatus;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.week = week;
//    }

//    public LocalTime getTime() {
//        return time;
//    }
//
//    public void setTime(LocalTime time) {
//        this.time = time;
//    }

//    public HabitProgress(HabitStatus habitStatus, LocalDate startDate, LocalDate endDate, boolean[] week, LocalTime time) {
//        this.habitStatus = habitStatus;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.week = week;
//        this.time = time;
//    }

    public HabitStatus getHabitStatus() {
        return habitStatus;
    }

    public void setHabitStatus(HabitStatus habitStatus) {
        this.habitStatus = habitStatus;

        week = new boolean[7];
        startDate = null;
        endDate = null;
        hour = 0;
        minute = 0;

    }

//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }

    public boolean[] getWeek() {
        return week;
    }

    public void setWeek(boolean[] week) {
        this.week = week;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

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

package by.pavka.memento.habit;

import java.time.LocalDate;
import java.time.LocalTime;

import by.pavka.memento.MementoApplication;

public class HabitProgress {
    private HabitStatus habitStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean[] week;
    private LocalTime time;

    public HabitProgress(HabitStatus habitStatus) {
        this.habitStatus = habitStatus;
    }

    public HabitProgress(HabitStatus habitStatus, LocalDate startDate) {
        this.habitStatus = habitStatus;
        this.startDate = startDate;
    }

    public HabitProgress() {
        habitStatus = HabitStatus.ENABLED;
    }

    public HabitProgress(HabitStatus habitStatus, LocalDate startDate, LocalDate endDate) {
        this.habitStatus = habitStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public HabitProgress(HabitStatus habitStatus, LocalDate startDate, LocalDate endDate, boolean[] week) {
        this.habitStatus = habitStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.week = week;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public HabitProgress(HabitStatus habitStatus, LocalDate startDate, LocalDate endDate, boolean[] week, LocalTime time) {
        this.habitStatus = habitStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.week = week;
        this.time = time;
    }

    public HabitStatus getHabitStatus() {
        return habitStatus;
    }

    public void setHabitStatus(HabitStatus habitStatus) {
        this.habitStatus = habitStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean[] getWeek() {
        return week;
    }

    public void setWeek(boolean[] week) {
        this.week = week;
    }
}

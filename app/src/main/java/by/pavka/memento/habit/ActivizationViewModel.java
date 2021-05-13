package by.pavka.memento.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.time.LocalDate;
import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.user.User;

public class ActivizationViewModel extends AndroidViewModel {
    public static final boolean[] CHECKED_WEEK = {true, true, true, true, true, true, true, true};
    public static final boolean[] UNCHECKED_WEEK = {false, false, false, false, false, false, false, false};

    private MementoApplication app;
    private Habit habit;
    private LocalDate end;
    private boolean[] week;
    private boolean clearance;

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
        clearance = false;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        HabitProgress progress = habits.get(habit);
        end = progress.getEndDate() == null ? LocalDate.now().plusDays(MementoApplication.DAYS_FOR_HABIT) : progress.getEndDate();
        week = progress.getWeek() == null? UNCHECKED_WEEK : progress.getWeek();
    }

    public void resetProgress(boolean cleared) {
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        if (cleared) {
            habits.put(habit, new HabitProgress(HabitStatus.ENABLED));
            clearance = false;
        } else {
            habits.put(habit, new HabitProgress(HabitStatus.ACTIVE, LocalDate.now(), end, week));
            user.setHabitCustomized(true);
            app.customizeHabits(true);
        }
        clearHabit();
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getEnd() {
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
}

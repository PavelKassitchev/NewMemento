package by.pavka.memento.track;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.habit.Habit;
import by.pavka.memento.habit.HabitProgress;
import by.pavka.memento.habit.HabitStatus;
import by.pavka.memento.user.User;

public class ActivizationViewModel extends AndroidViewModel {

    private MementoApplication app;
    private Habit habit;
    private Calendar end;
    private boolean[] week;
    private int hour;
    private int minute;
    private String name;
    private String description;
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
        hour = 0;
        minute = 0;
        clearance = false;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
        description = habit.getDescription();
        name = habit.getName();
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        HabitProgress progress = habits.get(habit);
        if (progress.getEndDate() != null) {
            end = progress.getEndDate();
            week = progress.getWeek();
            hour = progress.getHour();
            minute = progress.getMinute();
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app);
            int period = Integer.parseInt(preferences.getString("period", "35"));
            week = new boolean[]{false, false, false, false, false, false, false};
            Calendar last = Calendar.getInstance();
            hour = last.get(Calendar.HOUR_OF_DAY);
            minute = last.get(Calendar.MINUTE);
            last.add(Calendar.DATE, period);
            end = last;
        }
    }

    public boolean validateSchedule() {
        Calendar now = Calendar.getInstance();
        Calendar finish = new GregorianCalendar(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH), hour, minute);
        return now.before(finish);
    }

    public void resetProgress(boolean cleared) {
        app.cancelWork(habit.getName() + habit.getId(), habit.getName(), habit.getId());
        habit.updateName(cleared);
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        if (cleared) {
            if (habit.getQuestion() < 0) {
                habits.remove(habit);
            }
            habits.put(habit, new HabitProgress(HabitStatus.ENABLED));
        } else {
            end.set(Calendar.HOUR_OF_DAY, hour);
            end.set(Calendar.MINUTE, minute);
            end.set(Calendar.SECOND, 0);
            end.set(Calendar.MILLISECOND, 0);
            if (habit.getQuestion() < 0) {
                habits.remove(habit);
            }
            habits.put(habit, new HabitProgress(HabitStatus.ACTIVE, Calendar.getInstance(), end, week, hour, minute));
            user.setHabitCustomized(true);
            app.customizeHabits(true);
        }
        clearHabit();
    }

    public boolean isHabitChangeable() {
        return habit.getQuestion() < 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        habit.setDescription(description);
    }

    public String getName() {
        return name;
    }
}

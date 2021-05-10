package by.pavka.memento.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.time.LocalDate;
import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.user.User;

public class ActivizationViewModel extends AndroidViewModel {
    private MementoApplication app;
    private Habit habit;
    private LocalDate end;

    public ActivizationViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication) application;
    }

    public Habit getHabit() {
        return habit;
    }

    public void clearHabit() {
        habit = null;
        end = null;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        HabitProgress progress = habits.get(habit);
        end = progress.getEndDate() == null ? LocalDate.now().plusDays(MementoApplication.DAYS_FOR_HABIT) : progress.getEndDate();
    }

    public void resetProgress() {
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        habits.put(habit, new HabitProgress(HabitStatus.ACTIVE, LocalDate.now(), end));
        user.setHabitCustomized(true);
        app.customizeHabits(true);
        habit = null;
        end = null;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getEnd() {
        return end;
    }
}

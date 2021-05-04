package by.pavka.memento.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Map;

import by.pavka.memento.MementoApplication;

public class ActivizationViewModel extends AndroidViewModel {
    private MementoApplication app;
    private Habit habit;

    public ActivizationViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication)application;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void resetProgress() {
        Map<Habit, HabitProgress> habits = app.getUser().getTracker().getHabits();
        habits.put(habit, new HabitProgress(HabitStatus.ACTIVE));
        habit = null;
    }
}

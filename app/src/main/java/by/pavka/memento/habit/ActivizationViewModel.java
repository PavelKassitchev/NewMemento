package by.pavka.memento.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.user.User;

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
        User user = app.getUser();
        Map<Habit, HabitProgress> habits = user.getTracker().getHabits();
        habits.put(habit, new HabitProgress(HabitStatus.ACTIVE));
        user.setHabitCustomized(true);
        app.customizeHabits(true);
        habit = null;
    }
}

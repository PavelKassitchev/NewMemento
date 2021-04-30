package by.pavka.memento.habit;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.user.User;

public class HabitTracker {
    private MementoApplication app;
    private Map<Habit, HabitProgress> habits;

    public HabitTracker() {
    }

    public HabitTracker(MementoApplication app) {
        this.app = app;
        habits = new HashMap<>();
        init();
    }

    private void init() {
        String[] habs = app.getResources().getStringArray(R.array.habits);
        int[] questions = app.getResources().getIntArray(R.array.link);
        int[] influence = app.getResources().getIntArray(R.array.influence);
        int length = habs.length;
        Habit[] habies = new Habit[length];
        User user = app.getUser();
        int[] answers = user.getAnswers();
        for (int i = 0; i < length; i++) {
            habies[i] = new Habit(habs[i], 0, questions[i], influence[i]);
        }
    }

    public Context getApp() {
        return app;
    }

    public void setApp(MementoApplication app) {
        this.app = app;
    }

    public Map<Habit, HabitProgress> getHabits() {
        return habits;
    }

    public void setHabits(Map<Habit, HabitProgress> habits) {
        this.habits = habits;
    }
}

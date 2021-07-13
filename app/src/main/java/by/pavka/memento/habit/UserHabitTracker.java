package by.pavka.memento.habit;

import android.content.res.TypedArray;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;

public class UserHabitTracker {
    private transient MementoApplication app;
    private Map<Habit, HabitProgress> habits;
    private int obtainedCustomizedHabits;

    public UserHabitTracker(MementoApplication app) {
        this.app = app;
        habits = new HashMap<>();
        init();
    }

    public UserHabitTracker() {
        habits = new HashMap<>();
    }

    private void init() {
        String[] habs = app.getResources().getStringArray(R.array.habits);
        int[] questions = app.getResources().getIntArray(R.array.link);
        int[] influence = app.getResources().getIntArray(R.array.influence);
        TypedArray imgs = app.getResources().obtainTypedArray(R.array.pics);
        int length = habs.length;
        Habit[] habies = new Habit[length];
        for (int i = 0; i < length; i++) {
            int pic = imgs.getResourceId(i, 0);
            habies[i] = new Habit(habs[i], pic, questions[i], influence[i]);
            habies[i].setId(i);
            habits.put(habies[i], new HabitProgress());
        }
        imgs.recycle();
    }

    public int getObtainedCustomizedHabits() {
        return obtainedCustomizedHabits;
    }

    public int increment(Habit habit) {
        getHabitProgress(habit.getId()).setHabitStatus(HabitStatus.DISABLED);
        return ++obtainedCustomizedHabits;
    }

    public void updateWithAnswers(int[] answers, boolean cleanUser) {
        for (Map.Entry<Habit, HabitProgress> entry : habits.entrySet()) {
            Habit habit = entry.getKey();
            int question = habit.getQuestion();
            int better = habit.getBetter();
            if (question > -1) {
                int answer = answers[question];
                HabitProgress progress = entry.getValue();
                if (!cleanUser) {
                    if (better * answer >= 0) {
                        Log.d("STATUS", " HABIT: " + habit.getName() + " STATUS: " + progress.getHabitStatus());
                        if (progress.getHabitStatus() == HabitStatus.DISABLED) {
                            progress.setHabitStatus(HabitStatus.ENABLED);
                        }
                    } else {
                        progress.setHabitStatus(HabitStatus.DISABLED);
                        Log.d("STATUS", "Habit = " + habit + " status DISABLED");
                        app.cancelWork(habit.getName(), habit.getName() + habit.getId(), habit.getId());
                    }
                } else {
                    if (progress.getHabitStatus() == HabitStatus.DISABLED) {
                        progress.setHabitStatus(HabitStatus.ENABLED);
                    }
                }
            }
        }
    }

    public Habit getHabit(int id) {
        for (Map.Entry<Habit, HabitProgress> entry : habits.entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getKey();
            }
        }
        return null;
    }

    public HabitProgress getHabitProgress(int id) {
        for (Map.Entry<Habit, HabitProgress> entry : habits.entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void clearHabitProgress(int id) {
        Habit habit = getHabit(id);
        habits.put(habit, new HabitProgress(HabitStatus.ENABLED));
    }

    public Map<Habit, HabitProgress> getHabits() {
        return habits;
    }

    public MementoApplication getApp() {
        return app;
    }

    public void setApp(MementoApplication app) {
        this.app = app;
    }

    public void setHabits(Map<Habit, HabitProgress> habits) {
        this.habits = habits;
    }
}

package by.pavka.memento.user;

import java.util.Calendar;
import java.util.Map;

import by.pavka.memento.habit.Habit;
import by.pavka.memento.habit.HabitProgress;
import by.pavka.memento.habit.HabitStatus;
import by.pavka.memento.habit.UserHabitTracker;

public class User {
    private String name;
    private int gender;
    private Calendar birthDate;
    private int weight;
    private int height;

    private int[] answers;
    private UserHabitTracker tracker;
    private boolean habitCustomized;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public int[] getAnswers() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }


    public UserHabitTracker getTracker() {
        return tracker;
    }

    public void setTracker(UserHabitTracker tracker) {
        this.tracker = tracker;
    }

    public boolean isHabitCustomized() {
        return habitCustomized;
    }

    public void setHabitCustomized(boolean habitCustomized) {
        this.habitCustomized = habitCustomized;
    }

    public void updateTracker(boolean cleanUser) {
        tracker.updateWithAnswers(answers, cleanUser);
        setHabitCustomized(true);
    }

    public boolean allHabitsClear() {
        for (HabitProgress progress : tracker.getHabits().values()) {
            if (progress.getHabitStatus() != HabitStatus.ENABLED) {
                return false;
            }
        }
        return true;
    }

    public void cleanQuestionnaire() {
        setName("");
        setHeight(0);
        setWeight(0);
        setGender(0);
        setBirthDate(null);
        setAnswers(new int[answers.length]);
        updateTracker(true);
    }
}

package by.pavka.memento.user;

import java.util.Calendar;

import by.pavka.memento.habit.HabitProgress;
import by.pavka.memento.habit.HabitStatus;

public class User {
    private String name;
    private int gender;
    private Calendar birthDate;
    private double weight;
    private double height;
    private int[] answers;
    private UserHabitTracker tracker;
    private boolean habitCustomized;
    private Chronicler chronicler;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
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

    public Chronicler getChronicler() {
        return chronicler;
    }

    public void setChronicler(Chronicler chronicler) {
        this.chronicler = chronicler;
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

package by.pavka.memento;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import by.pavka.memento.calculator.Questionnaire;
import by.pavka.memento.calculator.impl.QuestionnaireImpl;
import by.pavka.memento.habit.HabitTracker;
import by.pavka.memento.user.User;
import by.pavka.memento.util.CalendarConverter;

public class MementoApplication extends Application {
    public static final String APP_PREF = "MementoPref";
    public static final String NAME = "name";
    public static final String DATE = "birthDate";
    public static final String GENDER = "gender";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String INDEX = "index";
    public static final String HABITS_CUSTOMIZED = "customized";
    public static final String TRACKER = "tracker";

    private Questionnaire questionnaire;
    private User user;
    private HabitTracker habitTracker;

    public Questionnaire getQuestionnaire() {
        if (questionnaire == null) {
            questionnaire = new QuestionnaireImpl(this);
        }
        questionnaire.setCursor(0);
        return questionnaire;
    }

    public User getUser() {
        if (user == null) {
            user = createUser();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User createUser() {
        User user = new User();
        SharedPreferences preferences = getSharedPreferences(APP_PREF, MODE_PRIVATE);
        String dateOfBirth = preferences.getString(DATE, null);
        String name = "";
        int length = getQuestionnaire().getLength();
        int[] answers = new int[length];
        if (dateOfBirth != null) {
            name = preferences.getString(NAME, name);
            int gender = preferences.getInt(GENDER, 0);
            user.setGender(gender);
            int weight = preferences.getInt(WEIGHT, 0);
            user.setWeight(weight);
            int height = preferences.getInt(HEIGHT, 0);
            user.setHeight(height);
            Calendar birthDate = CalendarConverter.intoCalendar(dateOfBirth);
            user.setBirthDate(birthDate);
            for (int i = 0; i < length; i++) {
                answers[i] = preferences.getInt(INDEX + i, 0);
            }
        }
        user.setName(name);
        user.setAnswers(answers);
        return user;
    }

    public void savePersonData(String name, String dateOfBirth, int gender) {
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREF, MODE_PRIVATE).edit();
        editor.putString(NAME, name);
        editor.putString(DATE, dateOfBirth);
        editor.putInt(GENDER, gender);
        editor.apply();
    }

    public void saveBMIData(int weight, int height) {
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREF, MODE_PRIVATE).edit();
        editor.putInt(WEIGHT, weight);
        editor.putInt(HEIGHT, height);
        editor.apply();
    }

    public void saveAnswers(int[] answers) {
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREF, MODE_PRIVATE).edit();
        for (int i = 0; i < answers.length; i++) {
            editor.putInt(INDEX + i, answers[i]);
        }
        editor.apply();
    }

    public void clear() {
        SharedPreferences preferences = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public HabitTracker getHabitTracker() {
        if (habitTracker == null) {
            SharedPreferences preferences = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE);
            boolean customized = preferences.getBoolean(HABITS_CUSTOMIZED, false);
            if (customized) {
               habitTracker = loadTracker();
            } else {
                habitTracker = new HabitTracker(this);
            }
        }
        return habitTracker;
    }

    private HabitTracker loadTracker() {
        SharedPreferences preferences = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE);
        String tracker = preferences.getString(TRACKER, null);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(tracker, HabitTracker.class);
    }

    private void saveTracker(HabitTracker tracker) {
        SharedPreferences.Editor editor = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE).edit();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String sTracker = gson.toJson(tracker);
        editor.putString(TRACKER, sTracker);
        editor.apply();
    }

}

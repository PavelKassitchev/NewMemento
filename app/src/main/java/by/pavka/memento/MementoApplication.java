package by.pavka.memento;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;

import androidx.core.app.NotificationManagerCompat;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import by.pavka.memento.alarmer.MementoAlarmer;
import by.pavka.memento.calculator.Questionnaire;
import by.pavka.memento.calculator.impl.QuestionnaireImpl;
import by.pavka.memento.habit.Habit;
import by.pavka.memento.habit.HabitProgress;
import by.pavka.memento.habit.HabitStatus;
import by.pavka.memento.user.UserHabitTracker;
import by.pavka.memento.notification.CountDownWorker;
import by.pavka.memento.notification.MementoWorker;
import by.pavka.memento.user.Chronicler;
import by.pavka.memento.user.User;
import by.pavka.memento.util.CalendarConverter;

public class MementoApplication extends MultiDexApplication {
    public static final String MEMENTO_CHANNEL_ID = "Memento Channel";
    public static final String APP_PREF = "MementoPref";
    public static final String NAME = "name";
    public static final String DATE = "birthDate";
    public static final String GENDER = "gender";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String INDEX = "index";
    public static final String HABITS_CUSTOMIZED = "customized";
    public static final String TRACKER = "tracker";
    public static final String HABIT = "habit";
    private static final String CHRONICLER = "chronicler";

    private Questionnaire questionnaire;
    private User user;
    private NotificationChannel channel;
    private String success;
    private static String newHabit;
    private static String habitInProgress;


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        success = getString(R.string.success);
        habitInProgress = getString(R.string.progress);
        newHabit = getString(R.string.habit_new);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
    }

    public static String getNewHabit() {
        return newHabit;
    }

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
        User newUser = new User();
        SharedPreferences preferences = getSharedPreferences(APP_PREF, MODE_PRIVATE);
        boolean habitsCustomized = preferences.getBoolean(HABITS_CUSTOMIZED, false);
        UserHabitTracker tracker;
        if (habitsCustomized) {
            newUser.setHabitCustomized(true);
            tracker = loadTracker();
            tracker.setApp(this);
        } else {
            tracker = new UserHabitTracker(this);
        }
        Chronicler chronicler = loadChronicler();
        newUser.setChronicler(chronicler);
        String dateOfBirth = preferences.getString(DATE, null);
        String name = "";
        int length = getQuestionnaire().getLength();
        int[] answers = new int[length];
        if (dateOfBirth != null) {
            name = preferences.getString(NAME, name);
            int gender = preferences.getInt(GENDER, 0);
            newUser.setGender(gender);
            double weight = preferences.getFloat(WEIGHT, 0);
            newUser.setWeight(weight);
            double height = preferences.getFloat(HEIGHT, 0);
            newUser.setHeight(height);
            Calendar birthDate = CalendarConverter.intoCalendar(dateOfBirth);
            newUser.setBirthDate(birthDate);
            for (int i = 0; i < length; i++) {
                answers[i] = preferences.getInt(INDEX + i, 0);
            }
        }
        newUser.setName(name);
        newUser.setAnswers(answers);
        newUser.setTracker(tracker);
        return newUser;
    }

    public void savePersonData(String name, String dateOfBirth, int gender) {
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREF, MODE_PRIVATE).edit();
        editor.putString(NAME, name);
        editor.putString(DATE, dateOfBirth);
        editor.putInt(GENDER, gender);
        editor.putBoolean(HABITS_CUSTOMIZED, true);
        editor.apply();
    }

    public void customizeHabits(boolean customized) {
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREF, MODE_PRIVATE).edit();
        editor.putBoolean(HABITS_CUSTOMIZED, customized);
        editor.apply();
    }

    public void saveBMIData(double weight, double height) {
        SharedPreferences.Editor editor = getSharedPreferences(APP_PREF, MODE_PRIVATE).edit();
        editor.putFloat(WEIGHT, (float) weight);
        editor.putFloat(HEIGHT, (float) height);
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

    public void clearQuestionnaireData() {
        SharedPreferences.Editor editor = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE).edit();
        editor.remove(NAME).remove(GENDER).remove(DATE).remove(WEIGHT).remove(HEIGHT);
        editor.apply();
    }

    public void saveChronicler() {
        SharedPreferences.Editor editor = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE).edit();
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        Gson gson = builder.create();
        Chronicler chronicler = user.getChronicler();
        double weight = chronicler.findLastWeight();
        String sChronicler = gson.toJson(chronicler);
        editor.putString(CHRONICLER, sChronicler);
        editor.putFloat(WEIGHT, (float) weight);
        editor.apply();
    }

    public Chronicler loadChronicler() {
        SharedPreferences preferences = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE);
        String sChronicler = preferences.getString(CHRONICLER, null);
        if (sChronicler == null) {
            return new Chronicler();
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(sChronicler, Chronicler.class);
    }

    public void saveHabits() {
        SharedPreferences.Editor editor = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE).edit();
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        Gson gson = builder.create();
        UserHabitTracker habitTracker = user.getTracker();
        String sTracker = gson.toJson(habitTracker);
        editor.putString(TRACKER, sTracker);
        editor.apply();
    }

    private UserHabitTracker loadTracker() {
        SharedPreferences preferences = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE);
        String tracker = preferences.getString(TRACKER, null);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(tracker, UserHabitTracker.class);
    }

    public String getSuccess() {
        return success;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(MEMENTO_CHANNEL_ID, "Memento Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
        }
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
        notifyManager.createNotificationChannel(channel);
    }

    public void cancelWork(String tag) {
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.cancelAllWorkByTag(tag);
    }

    public void cancelWork(String tag1, String tag2, int id) {
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.cancelAllWorkByTag(tag1);
        workManager.cancelAllWorkByTag(tag2);
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(this);
        notifyManager.cancel(id);
    }

    public void launchNotification(int id, boolean resetting) {
        UserHabitTracker tracker = getUser().getTracker();
        Habit habit = tracker.getHabit(id);
        HabitProgress progress = tracker.getHabitProgress(id);
        boolean[] week = progress.getWeek();
        String habitName = habit.getName();
        int hour = progress.getHour();
        int minute = progress.getMinute();
        WorkManager workManager = WorkManager.getInstance(this);
        MementoAlarmer alarmer = new MementoAlarmer();
        long delay = alarmer.tillNextAlarm(week, hour, minute, resetting);

        if (progress.getHabitStatus() == HabitStatus.ACTIVE) {
            long end = alarmer.tillEnd(progress.getEndDate());
            String contentText;
            long tillNext;
            if (delay < 0 || end < delay) {
                contentText = success;
                tillNext = end;
            } else {
                contentText = habitInProgress;
                tillNext = delay;
            }
            Data data = new Data.Builder().putString(HABIT, habit.getName())
                    .putString("result", contentText).putInt("id", habit.getId()).build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MementoWorker.class)
                    .setInitialDelay(tillNext, TimeUnit.SECONDS).setInputData(data).addTag(habitName + id).build();
            workManager.enqueue(request);
        }
    }

    public void countDown(int id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int gap = Integer.parseInt(preferences.getString("delay", "180"));
        String habitName = getUser().getTracker().getHabit(id).getName();
        WorkManager workManager = WorkManager.getInstance(this);
        Data data = new Data.Builder().putInt("id", id).putString("name", habitName).build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CountDownWorker.class)
                .setInitialDelay(gap, TimeUnit.MINUTES).addTag(habitName).setInputData(data).build();
        workManager.enqueue(request);
    }

    public void failHabit(int id) {
        getUser().getTracker().getHabitProgress(id).setHabitStatus(HabitStatus.ENABLED);
        saveHabits();
    }
}

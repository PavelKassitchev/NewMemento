package by.pavka.memento;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.multidex.MultiDexApplication;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import by.pavka.memento.alarmer.MementoAlarmer;
import by.pavka.memento.calculator.Questionnaire;
import by.pavka.memento.calculator.impl.QuestionnaireImpl;
import by.pavka.memento.habit.Habit;
import by.pavka.memento.habit.HabitProgress;
import by.pavka.memento.habit.HabitStatus;
import by.pavka.memento.habit.UserHabitTracker;
import by.pavka.memento.notification.MementoWorker;
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
    public static final int DAYS_FOR_HABIT = 28;

    private Questionnaire questionnaire;
    private User user;
    private NotificationManager mNotifyManager;
    private NotificationChannel channel;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
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
        User user = new User();
        SharedPreferences preferences = getSharedPreferences(APP_PREF, MODE_PRIVATE);
        boolean habitsCustomized = preferences.getBoolean(HABITS_CUSTOMIZED, false);
        UserHabitTracker tracker;
        if (habitsCustomized) {
            tracker = loadTracker();
        } else {
            tracker = new UserHabitTracker(this);
            Log.d("MYSTERY", "CREATE USER NEW TRACKER");
        }
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
        user.setTracker(tracker);
        return user;
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

    public void clearQuestionnaireData() {
        SharedPreferences.Editor editor = getSharedPreferences(MementoApplication.APP_PREF, MODE_PRIVATE).edit();
        editor.remove(NAME).remove(GENDER).remove(DATE).remove(WEIGHT).remove(HEIGHT);
        editor.apply();
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
        UserHabitTracker habitTracker = gson.fromJson(tracker, UserHabitTracker.class);
        return habitTracker;
    }

//    public void controlHabit(Habit habit) {
//        HabitProgress progress = getUser().getTracker().getHabits().get(habit);
//        HabitStatus status = progress.getHabitStatus();
//        LocalTime time = progress.getTime();
//        LocalDate date = progress.getEndDate();
//        boolean[] week = progress.getWeek();
//        Data data = new Data.Builder().putString("habit", habit.getName())
//                .putInt("id", habit.getId())
//                .build();
//        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MementoWorker.class)
//                .setInitialDelay(10, TimeUnit.SECONDS)
//                .setInputData(data)
//                .build();
//        WorkManager.getInstance(this).enqueue(request);
//        //todo
//    }

    public void setNextNotification(int id, boolean init) {
        UserHabitTracker tracker = getUser().getTracker();
        Habit habit = tracker.getHabit(id);
        HabitProgress progress = tracker.getHabitProgress(id);
        WorkManager workManager = WorkManager.getInstance(this);
        if (init) {
            workManager.cancelAllWorkByTag(habit.getName());
        }
        if (progress.getHabitStatus() == HabitStatus.ACTIVE) {
            long interval = calculateDelay(progress);
            if (interval > 0) {
                Data data = new Data.Builder().putString("habit", habit.getName())
                        .putInt("id", habit.getId())
                        .build();
                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MementoWorker.class)
                        .setInitialDelay(interval, TimeUnit.SECONDS)
                        .setInputData(data)
                        .addTag(habit.getName())
                        .build();
                workManager.enqueue(request);
            }
        }
    }

    private long calculateDelay(HabitProgress progress) {
        long delay = 60;
//        java.time.LocalDate currentDate = LocalDate.now();
//        int dayOfWeek = currentDate.getDayOfWeek().getValue() - 1;
//        boolean[] week = progress.getWeek();
//        boolean today = week[dayOfWeek];
//        if (today && LocalTime.now().isBefore(progress.getTime())) {
//            delay = Duration.between(LocalTime.now(), progress.getTime()).getSeconds();
//        } else {
//            LocalDate nextDate = nextNotificationDay(week, dayOfWeek);
//            if (nextDate == null) {
//                return -1;
//            }
//            if (nextDate.isAfter(progress.getEndDate())) {
//                nextDate = progress.getEndDate();
//            }
//            LocalDateTime dateTime = LocalDateTime.of(nextDate, progress.getTime());
//            delay = Duration.between(LocalDateTime.now(), dateTime).getSeconds();
//        }
        return delay;
    }

    private LocalDate nextNotificationDay(boolean[] week, int day) {
//        LocalDate result = LocalDate.now();
//        for (int i = day + 1; i < week.length - 1; i++) {
//            result = result.plusDays(1);
//            if (week[i] = true) {
//                return result;
//            }
//        }
//        for (int i = 0; i <= day; i++) {
//            result = result.plusDays(1);
//            if (week[i] = true) {
//                return result;
//            }
//        }
        return null;
    }

    private int findNextDay(boolean[] week, int day) {
        for (int i = day + 1; i < week.length - 1; i++) {
            if (week[i] = true) {
                return i;
            }
        }
        for (int i = 0; i < day; i++) {
            if (week[i] = true) {
                return i;
            }
        }
        return -1;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(MEMENTO_CHANNEL_ID, "Memento Notification", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(getApplicationContext());
        notifyManager.createNotificationChannel(channel);
    }

    public void launchNotification(int id, boolean b) {
        UserHabitTracker tracker = getUser().getTracker();
        Habit habit = tracker.getHabit(id);
        HabitProgress progress = tracker.getHabitProgress(id);
        boolean[] week = progress.getWeek();
        Log.d("MYSTERY", "WEEK = " + week);
        int hour = progress.getHour();
        int minute = progress.getMinute();
        WorkManager workManager = WorkManager.getInstance(this);
        long delay = new MementoAlarmer().tillNextAlarm(week, hour, minute);
        long end = new MementoAlarmer().tillEnd(progress.getEndDate());
        Log.d("MYSTERY", "DELAY = " + delay + " ENDDELAY = " + end);
    }
}

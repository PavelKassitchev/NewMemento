package by.pavka.memento.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import by.pavka.memento.profile.MainActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.habit.Habit;
import by.pavka.memento.track.HabitActivity;

public class DoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Habit habit = (Habit) intent.getSerializableExtra(MementoApplication.HABIT);
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(context);
        notifyManager.cancel(habit.getId());
        String tag = habit.getName();
        String action = intent.getAction();
        if ("by.pavka.fail".equals(action)) {
            Intent failureIntent = new Intent(context, HabitActivity.class);
            failureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            failureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            failureIntent.putExtra(MementoApplication.HABIT, habit);
            context.startActivity(failureIntent);
        }
        if ("by.pavka.finish".equals(action)) {
            Intent finishIntent = new Intent(context, MainActivity.class);
            finishIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finishIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finishIntent.putExtra(MementoApplication.HABIT, habit);
            context.startActivity(finishIntent);
        }
        if ("by.pavka.expire".equals(action)) {
            tag += habit.getId();
            Intent failureIntent = new Intent(context, HabitActivity.class);
            failureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            failureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            failureIntent.putExtra(MementoApplication.HABIT, habit);
            failureIntent.putExtra("expire", true);
            context.startActivity(failureIntent);
            ((MementoApplication)(context.getApplicationContext())).failHabit(habit.getId());
        }
        ((MementoApplication) context.getApplicationContext()).cancelWork(tag);
    }
}
package by.pavka.memento.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import by.pavka.memento.MainActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.habit.Habit;
import by.pavka.memento.habit.HabitActivity;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

public class CountDownWorker extends Worker {
    public CountDownWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        MementoApplication context = (MementoApplication)getApplicationContext();
        int id = getInputData().getInt("id", -1);
        String habitName = getInputData().getString("name");
        context.cancelWork(habitName);

        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(context);
        notifyManager.cancel(id);

        Habit habit = context.getUser().getTracker().getHabit(id);
        Log.d("MYSTERY", "CountDownWorker habit = " + habit);
        Intent failed = new Intent(context, DoneReceiver.class);
        failed.setAction("by.pavka.expire");
        failed.putExtra("habit", habit);
        context.sendBroadcast(failed);

        Intent intent = new Intent(context, HabitActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,id, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MementoApplication.MEMENTO_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.thumb_down).setAutoCancel(true).setContentIntent(pendingIntent)
                .setContentTitle(habitName).setContentText(context.getResources().getString(R.string.fail))
                .setPriority(PRIORITY_HIGH);
        Notification notification = builder.build();
        notifyManager.notify(id, notification);
        return Result.success();
    }
}

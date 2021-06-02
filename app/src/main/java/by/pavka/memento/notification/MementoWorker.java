package by.pavka.memento.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.LocalDate;

import by.pavka.memento.MainActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.habit.ActivizationActivity;
import by.pavka.memento.habit.Habit;
import by.pavka.memento.habit.HabitActivity;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

public class MementoWorker extends Worker {
    public MementoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        MementoApplication context = (MementoApplication) getApplicationContext();
        String contentText = getInputData().getString("result");
        int id = getInputData().getInt("id", -1);
        Habit habit = context.getUser().getTracker().getHabit(id);
        String habitName = habit.getName();
        context.cancelWork(habitName + id);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, 0);

        Intent failed = new Intent(context, DoneReceiver.class);
        failed.setAction("by.pavka.fail");
        failed.putExtra("habit", habit);
        PendingIntent pendingFailed = PendingIntent.getBroadcast(context,id, failed, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MementoApplication.MEMENTO_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(habitName)
                .setContentText(contentText)
                .setLargeIcon(obtainIcon(id))
                .setPriority(PRIORITY_HIGH)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);
        if (!contentText.equals(context.getSuccess())) {
            Intent done = new Intent(context, DoneReceiver.class);
            done.setAction("by.pavka.done");
            done.putExtra("habit", habit);
            PendingIntent pendingDone = PendingIntent.getBroadcast(context, id, done, 0);
            builder.addAction(R.drawable.ic_done, context.getResources().getString(R.string.done), pendingDone);
            context.launchNotification(id, false);
            context.countDown(id);
        } else {
            Intent finish = new Intent(context, DoneReceiver.class);
            finish.setAction("by.pavka.finish");
            finish.putExtra("habit", habit);
            PendingIntent pendingFinish = PendingIntent.getBroadcast(context, id, finish, 0);
            builder.addAction(R.drawable.thumb_up, context.getResources().getString(R.string.success), pendingFinish);
        }
        builder.addAction(R.drawable.ic_fail, context.getResources().getString(R.string.fail), pendingFailed);
        Notification notification = builder.build();
        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(context);
        notifyManager.notify(id, notification);

        return Result.success();
    }

    private Bitmap obtainIcon(int id) {
        TypedArray imgs = getApplicationContext().getResources().obtainTypedArray(R.array.pics);
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getApplicationContext().getDrawable(imgs.getResourceId(id, 0));
        } else {
            drawable = getApplicationContext().getResources().getDrawable(imgs.getResourceId(id, 0));
        }
        Bitmap bm = Bitmap.createBitmap(124, 124, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bm;
    }
}

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

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

public class MementoWorker extends Worker {
    public MementoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
//        MementoApplication context = (MementoApplication) getApplicationContext();
//        String habitName = getInputData().getString("habit");
//        int id = getInputData().getInt("id", -1);
//        LocalDate endDate = context.getUser().getTracker().getHabitProgress(id).getEndDate();
//        String contentText = habitName;
//        if (LocalDate.now().isEqual(endDate)) {
//            //todo
//            contentText = "SUCCESS!";
//        }
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        Log.d("MYSTERY", "BITMAP: ");
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MementoApplication.MEMENTO_CHANNEL_ID);
//        builder.setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle(habitName)
//                .setContentText(contentText)
//                .setLargeIcon(obtainIcon(id))
//                .setPriority(PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .addAction(R.drawable.thumb_up, context.getResources().getString(R.string.done), null)
//                .addAction(R.drawable.thumb_down, context.getResources().getString(R.string.fail), null)
//                .setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        NotificationManagerCompat notifyManager = NotificationManagerCompat.from(context);
//        notifyManager.notify(id, notification);
//        //todo
//        if (!contentText.equals("SUCCESS!")) {
//            context.setNextNotification(id, false);
//        }
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

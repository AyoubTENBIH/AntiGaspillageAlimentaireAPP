package com.example.anti_gaspillagealimentaireapp.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import com.example.anti_gaspillagealimentaireapp.R;

public final class NotificationHelper {

    private static final String CHANNEL_ID = "saveat_reminders";
    private static final int NOTIFICATION_ID_REMINDER = 1001;

    private NotificationHelper() {}

    public static void ensureChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_reminders),
            NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Rappels de réservations paniers");
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
    }

    public static void showReservationReminder(Context context, int count) {
        ensureChannel(context);
        String title = context.getString(R.string.notification_reminder_title);
        String text = count <= 1
            ? context.getString(R.string.notification_reminder_one)
            : context.getString(R.string.notification_reminder_body, count);
        android.app.Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID_REMINDER, notification);
    }
}

package com.example.anti_gaspillagealimentaireapp.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.AppNotification;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

/**
 * Worker local (v1) : vérifie les réservations du jour pour le client connecté
 * et affiche une notification si besoin.
 */
public class ReservationReminderWorker extends Worker {

    public ReservationReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (!(getApplicationContext().getApplicationContext() instanceof SaveAtApplication)) {
            return Result.failure();
        }
        SaveAtApplication app = (SaveAtApplication) getApplicationContext().getApplicationContext();
        SessionManager session = new SessionManager(getApplicationContext());
        SessionManager.CurrentUser user = session.getCurrentUser();
        if (user == null) return Result.success();
        if (user.role != UserRole.CLIENT) return Result.success();

        java.util.List<com.example.anti_gaspillagealimentaireapp.data.local.entities.Reservation> todayReservations =
            app.getDatabase().reservationDao().getReservationsTodayByClient(user.id);
        if (!todayReservations.isEmpty()) {
            int count = todayReservations.size();
            NotificationHelper.showReservationReminder(getApplicationContext(), count);
            String title = getApplicationContext().getString(R.string.notification_reminder_title);
            String message = count <= 1
                ? getApplicationContext().getString(R.string.notification_reminder_one)
                : getApplicationContext().getString(R.string.notification_reminder_body, count);
            AppNotification notif = new AppNotification();
            notif.userId = user.id;
            notif.title = title;
            notif.message = message;
            app.getDatabase().appNotificationDao().insert(notif);
        }
        return Result.success();
    }
}

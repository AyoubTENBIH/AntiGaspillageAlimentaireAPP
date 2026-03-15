package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.AppNotification;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NotificationsViewModel(Application application) {
        super(application);
    }

    public LiveData<List<AppNotification>> getNotifications() {
        long uid = sessionManager.getCurrentUser() != null ? sessionManager.getCurrentUser().id : -1L;
        if (uid > 0) return app.getDatabase().appNotificationDao().getAllByUser(uid);
        return new MutableLiveData<>(Collections.<AppNotification>emptyList());
    }

    public LiveData<Integer> getUnreadCount() {
        long uid = sessionManager.getCurrentUser() != null ? sessionManager.getCurrentUser().id : -1L;
        if (uid > 0) return app.getDatabase().appNotificationDao().countUnreadByUser(uid);
        return new MutableLiveData<>(0);
    }

    public void markAsRead(final AppNotification notification) {
        if (notification.isRead) return;
        executor.execute(new Runnable() { @Override public void run() { app.getDatabase().appNotificationDao().markAsRead(notification.id); } });
    }
}

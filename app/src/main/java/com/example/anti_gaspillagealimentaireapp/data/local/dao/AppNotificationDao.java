package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.AppNotification;

import java.util.List;

@Dao
public interface AppNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AppNotification notification);

    @Query("SELECT * FROM app_notifications WHERE userId = :userId ORDER BY dateCreated DESC")
    LiveData<List<AppNotification>> getAllByUser(long userId);

    @Query("SELECT COUNT(*) FROM app_notifications WHERE userId = :userId AND isRead = 0")
    LiveData<Integer> countUnreadByUser(long userId);

    @Update
    void update(AppNotification notification);

    @Query("UPDATE app_notifications SET isRead = 1 WHERE id = :id")
    void markAsRead(long id);

    @Query("UPDATE app_notifications SET isRead = 1 WHERE userId = :userId")
    void markAllAsRead(long userId);
}

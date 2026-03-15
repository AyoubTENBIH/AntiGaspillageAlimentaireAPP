package com.example.anti_gaspillagealimentaireapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Notification in-app (v1 local) : rappels, infos, etc.
 */
@Entity(
    tableName = "app_notifications",
    indices = {@Index("userId"), @Index("dateCreated")}
)
public class AppNotification {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public long userId;
    public String title;
    public String message;
    public long dateCreated = System.currentTimeMillis();
    public boolean isRead = false;

    public AppNotification() {
    }

    public AppNotification(long id, long userId, String title, String message, long dateCreated, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.dateCreated = dateCreated;
        this.isRead = isRead;
    }
}

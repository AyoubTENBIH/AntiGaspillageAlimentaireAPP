package com.example.anti_gaspillagealimentaireapp.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.AppNotificationDao;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceDao;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.FavoriteDao;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.PanierDao;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.ReservationDao;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.UserDao;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.AppNotification;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Favorite;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Reservation;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.User;

/**
 * Base de données Room principale de SaveAt.
 */
@Database(
    entities = {User.class, Commerce.class, Panier.class, Reservation.class, Favorite.class, AppNotification.class},
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends androidx.room.RoomDatabase {

    public abstract UserDao userDao();
    public abstract CommerceDao commerceDao();
    public abstract PanierDao panierDao();
    public abstract ReservationDao reservationDao();
    public abstract FavoriteDao favoriteDao();
    public abstract AppNotificationDao appNotificationDao();

    private static final String DATABASE_NAME = "saveat_db";
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

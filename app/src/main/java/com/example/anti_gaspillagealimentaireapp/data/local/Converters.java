package com.example.anti_gaspillagealimentaireapp.data.local;

import androidx.room.TypeConverter;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;

/**
 * Convertisseurs Room pour les types personnalisés (enums).
 */
public class Converters {

    @TypeConverter
    public static String fromUserRole(UserRole value) {
        return value == null ? null : value.name();
    }

    @TypeConverter
    public static UserRole toUserRole(String value) {
        return value == null ? null : UserRole.valueOf(value);
    }

    @TypeConverter
    public static String fromReservationStatut(ReservationStatut value) {
        return value == null ? null : value.name();
    }

    @TypeConverter
    public static ReservationStatut toReservationStatut(String value) {
        return value == null ? null : ReservationStatut.valueOf(value);
    }
}

package com.example.anti_gaspillagealimentaireapp.data.local.entities;

/**
 * Nombre de réservations par jour (pour graphique marchand).
 */
public class ReservationCountByDay {
    public final String day;
    public final int count;

    public ReservationCountByDay(String day, int count) {
        this.day = day;
        this.count = count;
    }
}

package com.example.anti_gaspillagealimentaireapp.data.local.entities;

/**
 * Stats de réservations pour un panier (pour l'écran détail commerçant).
 */
public class ReservationStats {
    public final int total;
    public final int enAttente;

    public ReservationStats(int total, int enAttente) {
        this.total = total;
        this.enAttente = enAttente;
    }
}

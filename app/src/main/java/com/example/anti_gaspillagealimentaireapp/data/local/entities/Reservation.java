package com.example.anti_gaspillagealimentaireapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entité Room représentant une réservation de panier par un client.
 */
@Entity(
    tableName = "reservations",
    foreignKeys = {
        @ForeignKey(
            entity = Panier.class,
            parentColumns = "id",
            childColumns = "panierId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("clientId"), @Index("panierId")}
)
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public long clientId;
    public long panierId;
    public long dateReservation;
    public ReservationStatut statut;

    public Reservation() {
    }

    public Reservation(long id, long clientId, long panierId, long dateReservation, ReservationStatut statut) {
        this.id = id;
        this.clientId = clientId;
        this.panierId = panierId;
        this.dateReservation = dateReservation;
        this.statut = statut;
    }
}

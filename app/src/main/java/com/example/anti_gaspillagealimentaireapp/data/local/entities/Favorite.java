package com.example.anti_gaspillagealimentaireapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Panier favori d'un client.
 */
@Entity(
    tableName = "favorites",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "userId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Panier.class,
            parentColumns = "id",
            childColumns = "panierId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("userId"), @Index("panierId")}
)
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public long userId;
    public long panierId;
    public long createdAt;

    public Favorite() {
    }

    public Favorite(long id, long userId, long panierId, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.panierId = panierId;
        this.createdAt = createdAt;
    }
}

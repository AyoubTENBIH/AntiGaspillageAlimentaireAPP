package com.example.anti_gaspillagealimentaireapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entité Room représentant un commerce (boulangerie, restaurant, épicerie…).
 */
@Entity(
    tableName = "commerces",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "commercantId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("commercantId")}
)
public class Commerce {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public String nom;
    public String adresse;
    public String description;
    public String imageUrl;
    public long commercantId;
    public String categorie;
    public boolean isActive = true;
    public double latitude = 0.0;
    public double longitude = 0.0;

    public Commerce() {
    }

    public Commerce(long id, String nom, String adresse, String description, String imageUrl,
                   long commercantId, String categorie, boolean isActive, double latitude, double longitude) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.imageUrl = imageUrl;
        this.commercantId = commercantId;
        this.categorie = categorie;
        this.isActive = isActive;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

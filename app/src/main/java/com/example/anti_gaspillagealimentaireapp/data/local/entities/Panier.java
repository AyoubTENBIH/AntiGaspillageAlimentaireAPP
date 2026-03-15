package com.example.anti_gaspillagealimentaireapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entité Room représentant un panier alimentaire invendu proposé par un commerce.
 */
@Entity(
    tableName = "paniers",
    foreignKeys = {
        @ForeignKey(
            entity = Commerce.class,
            parentColumns = "id",
            childColumns = "commerceId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("commerceId")}
)
public class Panier {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public String titre;
    public String description;
    public double prix;
    public int quantiteDisponible;
    public long commerceId;
    public long dateExpiration;
    public String imageUrl;
    public boolean isAvailable = true;

    public Panier() {
    }

    public Panier(long id, String titre, String description, double prix, int quantiteDisponible,
                  long commerceId, long dateExpiration, String imageUrl, boolean isAvailable) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.quantiteDisponible = quantiteDisponible;
        this.commerceId = commerceId;
        this.dateExpiration = dateExpiration;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }
}

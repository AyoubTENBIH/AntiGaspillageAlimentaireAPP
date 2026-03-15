package com.example.anti_gaspillagealimentaireapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entité Room représentant un utilisateur (client ou commerçant).
 */
@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public String nom;
    public String email;
    public String motDePasse;
    public UserRole role;
    public long dateCreation;

    public User() {
    }

    public User(long id, String nom, String email, String motDePasse, UserRole role, long dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
    }

    public User(String nom, String email, String motDePasse, UserRole role, long dateCreation) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
    }
}

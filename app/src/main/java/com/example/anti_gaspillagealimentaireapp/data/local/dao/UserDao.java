package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.User;

import java.util.List;

/**
 * DAO Room pour l'entité User.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getById(long userId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND motDePasse = :passwordHash LIMIT 1")
    User getByEmailAndPassword(String email, String passwordHash);

    @Query("SELECT * FROM users WHERE role = 'COMMERCANT'")
    List<User> getAllCommercants();

    @Query("UPDATE users SET motDePasse = :newHash WHERE id = :userId")
    void updatePassword(long userId, String newHash);
}

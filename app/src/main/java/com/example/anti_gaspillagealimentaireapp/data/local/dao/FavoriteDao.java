package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Favorite;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Favorite favorite);

    @Query("DELETE FROM favorites WHERE userId = :userId AND panierId = :panierId")
    void delete(long userId, long panierId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND panierId = :panierId)")
    boolean isFavorite(long userId, long panierId);

    @Query("SELECT panierId FROM favorites WHERE userId = :userId")
    List<Long> getFavoritePanierIds(long userId);

    @Query("SELECT p.* FROM paniers p INNER JOIN favorites f ON f.panierId = p.id WHERE f.userId = :userId")
    List<Panier> getFavoritePaniers(long userId);
}

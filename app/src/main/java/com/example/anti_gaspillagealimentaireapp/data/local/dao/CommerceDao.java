package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce;

import java.util.List;

/**
 * DAO Room pour l'entité Commerce et relations Commerce + Paniers.
 */
@Dao
public interface CommerceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Commerce commerce);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Commerce> commerces);

    @Query("SELECT * FROM commerces WHERE id = :commerceId")
    Commerce getById(long commerceId);

    @Query("SELECT * FROM commerces WHERE isActive = 1 ORDER BY nom")
    List<Commerce> getAllActive();

    @Query("SELECT * FROM commerces WHERE commercantId = :commercantId")
    List<Commerce> getByCommercant(long commercantId);

    @Query("SELECT * FROM commerces WHERE categorie = :categorie AND isActive = 1")
    List<Commerce> getByCategorie(String categorie);

    @Transaction
    @Query("SELECT * FROM commerces WHERE id = :commerceId")
    CommerceWithPaniers getCommerceWithPaniers(long commerceId);

    @Transaction
    @Query("SELECT * FROM commerces WHERE isActive = 1 ORDER BY nom")
    List<CommerceWithPaniers> getAllCommercesWithPaniers();
}

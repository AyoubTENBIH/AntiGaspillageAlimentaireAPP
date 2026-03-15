package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.List;

/**
 * DAO Room pour l'entité Panier.
 */
@Dao
public interface PanierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Panier panier);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Panier> paniers);

    @Update
    void update(Panier panier);

    @Query("SELECT * FROM paniers WHERE id = :panierId")
    Panier getById(long panierId);

    @Query("SELECT * FROM paniers WHERE id = :panierId")
    LiveData<Panier> getByIdLiveData(long panierId);

    @Query("UPDATE paniers SET isAvailable = :isAvailable WHERE id = :panierId")
    void updateDisponibilite(long panierId, boolean isAvailable);

    @Query("SELECT * FROM paniers WHERE commerceId = :commerceId AND isAvailable = 1 AND quantiteDisponible > 0 ORDER BY dateExpiration")
    List<Panier> getPaniersDisponibles(long commerceId);

    @Query("SELECT * FROM paniers WHERE commerceId = :commerceId ORDER BY dateExpiration")
    List<Panier> getByCommerce(long commerceId);

    @Query("SELECT * FROM paniers WHERE commerceId = :commerceId ORDER BY dateExpiration")
    LiveData<List<Panier>> getByCommerceLiveData(long commerceId);

    @Query("SELECT * FROM paniers WHERE isAvailable = 1 AND quantiteDisponible > 0 ORDER BY dateExpiration")
    List<Panier> getAllPaniersDisponibles();

    @Query("UPDATE paniers SET quantiteDisponible = quantiteDisponible - 1 WHERE id = :panierId AND quantiteDisponible > 0")
    void decrementQuantite(long panierId);

    @Query("DELETE FROM paniers WHERE id = :panierId")
    void deleteById(long panierId);

    @Query("SELECT COUNT(*) FROM paniers p INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :commercantId AND p.isAvailable = 1 AND p.quantiteDisponible > 0")
    int countPaniersActifs(long commercantId);

    @Query("SELECT COUNT(*) FROM paniers p INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :commercantId AND p.isAvailable = 1 AND p.quantiteDisponible > 0")
    LiveData<Integer> countPaniersActifsLiveData(long commercantId);
}

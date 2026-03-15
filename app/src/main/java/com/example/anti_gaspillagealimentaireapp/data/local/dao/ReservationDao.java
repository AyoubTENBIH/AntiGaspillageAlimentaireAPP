package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Reservation;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationCountByDay;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationMerchantDetails;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStats;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationWithDetails;

import java.util.List;

/**
 * DAO Room pour l'entité Reservation.
 */
@Dao
public interface ReservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Reservation reservation);

    @Update
    void update(Reservation reservation);

    @Query("SELECT * FROM reservations WHERE id = :reservationId")
    Reservation getById(long reservationId);

    @Query("SELECT * FROM reservations WHERE clientId = :clientId ORDER BY dateReservation DESC")
    List<Reservation> getReservationsByClient(long clientId);

    @Query("SELECT r.id, r.clientId, r.panierId, r.dateReservation, r.statut, " +
            "p.titre AS panierTitre, c.nom AS commerceNom, p.imageUrl AS panierImageUrl " +
            "FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE r.clientId = :clientId ORDER BY r.dateReservation DESC")
    List<ReservationWithDetails> getReservationsByClientWithDetails(long clientId);

    @Query("SELECT * FROM reservations r INNER JOIN paniers p ON r.panierId = p.id WHERE p.commerceId = :commerceId ORDER BY r.dateReservation DESC")
    List<Reservation> getReservationsByCommerce(long commerceId);

    @Query("SELECT COUNT(*) FROM reservations r INNER JOIN paniers p ON r.panierId = p.id WHERE p.commerceId = :commerceId AND r.statut = 'EN_ATTENTE'")
    int countEnAttenteByCommerce(long commerceId);

    @Query("UPDATE reservations SET statut = :statut WHERE id = :reservationId")
    void updateStatut(long reservationId, ReservationStatut statut);

    @Query("SELECT COUNT(*) FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :uid AND date(r.dateReservation / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')")
    int countReservationsToday(long uid);

    @Query("SELECT COALESCE(SUM(p.prix), 0.0) FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :uid AND r.statut = 'CONFIRMEE' " +
            "AND r.dateReservation >= (strftime('%s', 'now', '-7 days') * 1000)")
    double sumRevenusThisWeek(long uid);

    @Query("SELECT COUNT(*) FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :uid AND date(r.dateReservation / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')")
    LiveData<Integer> countReservationsTodayLiveData(long uid);

    @Query("SELECT COALESCE(SUM(p.prix), 0.0) FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :uid AND r.statut = 'CONFIRMEE' " +
            "AND r.dateReservation >= (strftime('%s', 'now', '-7 days') * 1000)")
    LiveData<Double> sumRevenusThisWeekLiveData(long uid);

    @Query("SELECT r.id, r.clientId, r.panierId, r.dateReservation, r.statut, " +
            "p.titre AS panierTitre, p.prix AS panierPrix, p.imageUrl AS panierImageUrl, u.nom AS clientNom " +
            "FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id INNER JOIN users u ON r.clientId = u.id " +
            "WHERE c.commercantId = :commercantId AND r.statut = :statut ORDER BY r.dateReservation DESC")
    LiveData<List<ReservationMerchantDetails>> getReservationsByCommercantAndStatut(long commercantId, ReservationStatut statut);

    @Query("SELECT (SELECT COUNT(*) FROM reservations WHERE panierId = :panierId) as total, " +
            "(SELECT COUNT(*) FROM reservations WHERE panierId = :panierId AND statut = 'EN_ATTENTE') as enAttente")
    LiveData<ReservationStats> getStatsByPanier(long panierId);

    @Query("SELECT date(r.dateReservation / 1000, 'unixepoch', 'localtime') AS day, COUNT(*) AS count " +
            "FROM reservations r INNER JOIN paniers p ON r.panierId = p.id " +
            "INNER JOIN commerces c ON p.commerceId = c.id " +
            "WHERE c.commercantId = :commercantId AND r.dateReservation >= (strftime('%s', 'now', '-7 days') * 1000) " +
            "GROUP BY day ORDER BY day")
    List<ReservationCountByDay> getReservationsCountByDayLast7Days(long commercantId);

    @Query("SELECT COUNT(*) FROM reservations WHERE clientId = :clientId AND statut = 'CONFIRMEE'")
    int countReservationsConfirmeesByClient(long clientId);

    @Query("SELECT r.* FROM reservations r WHERE r.clientId = :clientId " +
            "AND date(r.dateReservation / 1000, 'unixepoch', 'localtime') = date('now', 'localtime') " +
            "AND r.statut != 'ANNULEE'")
    List<Reservation> getReservationsTodayByClient(long clientId);
}

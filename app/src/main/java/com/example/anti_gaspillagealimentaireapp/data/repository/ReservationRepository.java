package com.example.anti_gaspillagealimentaireapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;
import com.example.anti_gaspillagealimentaireapp.data.local.AppDatabase;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Reservation;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationCountByDay;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationMerchantDetails;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStats;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationWithDetails;

import java.util.List;

/**
 * Repository pour les réservations (création en transaction avec décrément stock).
 */
public class ReservationRepository {

    private final AppDatabase db;
    private final com.example.anti_gaspillagealimentaireapp.data.local.dao.ReservationDao reservationDao;
    private final com.example.anti_gaspillagealimentaireapp.data.local.dao.PanierDao panierDao;

    public ReservationRepository(AppDatabase db) {
        this.db = db;
        this.reservationDao = db.reservationDao();
        this.panierDao = db.panierDao();
    }

    public Reservation getById(long reservationId) {
        return reservationDao.getById(reservationId);
    }

    public List<Reservation> getReservationsByClient(long clientId) {
        return reservationDao.getReservationsByClient(clientId);
    }

    public List<ReservationWithDetails> getReservationsByClientWithDetails(long clientId) {
        return reservationDao.getReservationsByClientWithDetails(clientId);
    }

    public List<Reservation> getReservationsByCommerce(long commerceId) {
        return reservationDao.getReservationsByCommerce(commerceId);
    }

    public int countReservationsToday(long commercantId) {
        return reservationDao.countReservationsToday(commercantId);
    }

    public double sumRevenusThisWeek(long commercantId) {
        return reservationDao.sumRevenusThisWeek(commercantId);
    }

    public void updateStatut(long reservationId, ReservationStatut statut) {
        reservationDao.updateStatut(reservationId, statut);
    }

    public LiveData<Integer> countReservationsTodayLiveData(long commercantId) {
        return reservationDao.countReservationsTodayLiveData(commercantId);
    }

    public LiveData<Double> sumRevenusThisWeekLiveData(long commercantId) {
        return reservationDao.sumRevenusThisWeekLiveData(commercantId);
    }

    public LiveData<List<ReservationMerchantDetails>> getReservationsByCommercantAndStatut(long commercantId, ReservationStatut statut) {
        return reservationDao.getReservationsByCommercantAndStatut(commercantId, statut);
    }

    public LiveData<ReservationStats> getStatsByPanier(long panierId) {
        return reservationDao.getStatsByPanier(panierId);
    }

    public List<ReservationCountByDay> getReservationsCountByDayLast7Days(long commercantId) {
        return reservationDao.getReservationsCountByDayLast7Days(commercantId);
    }

    public int countReservationsConfirmeesByClient(long clientId) {
        return reservationDao.countReservationsConfirmeesByClient(clientId);
    }

    /**
     * Crée une réservation et décrémente la quantité du panier dans une transaction.
     * @return id de la réservation ou -1 si panier indisponible / quantité insuffisante
     */
    public long reserver(long clientId, long panierId) {
        final long[] result = new long[] { -1 };
        db.runInTransaction(new Runnable() {
            @Override
            public void run() {
                com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier panier = panierDao.getById(panierId);
                if (panier == null || !panier.isAvailable || panier.quantiteDisponible <= 0) return;
                panierDao.decrementQuantite(panierId);
                Reservation reservation = new Reservation(0, clientId, panierId, System.currentTimeMillis(), ReservationStatut.EN_ATTENTE);
                result[0] = reservationDao.insert(reservation);
            }
        });
        return result[0];
    }
}

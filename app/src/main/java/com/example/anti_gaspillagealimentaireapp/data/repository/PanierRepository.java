package com.example.anti_gaspillagealimentaireapp.data.repository;

import androidx.lifecycle.LiveData;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.PanierDao;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.List;

/**
 * Repository pour les paniers (CRUD, disponibilité, décrément).
 */
public class PanierRepository {

    private final PanierDao panierDao;

    public PanierRepository(PanierDao panierDao) {
        this.panierDao = panierDao;
    }

    public long insert(Panier panier) {
        return panierDao.insert(panier);
    }

    public void update(Panier panier) {
        panierDao.update(panier);
    }

    public Panier getById(long panierId) {
        return panierDao.getById(panierId);
    }

    public LiveData<Panier> getByIdLiveData(long panierId) {
        return panierDao.getByIdLiveData(panierId);
    }

    public void updateDisponibilite(long panierId, boolean isAvailable) {
        panierDao.updateDisponibilite(panierId, isAvailable);
    }

    public List<Panier> getPaniersDisponibles(long commerceId) {
        return panierDao.getPaniersDisponibles(commerceId);
    }

    public List<Panier> getByCommerce(long commerceId) {
        return panierDao.getByCommerce(commerceId);
    }

    public LiveData<List<Panier>> getPaniersByCommerceLiveData(long commerceId) {
        return panierDao.getByCommerceLiveData(commerceId);
    }

    public LiveData<Integer> countPaniersActifsLiveData(long commercantId) {
        return panierDao.countPaniersActifsLiveData(commercantId);
    }

    public int countPaniersActifs(long commercantId) {
        return panierDao.countPaniersActifs(commercantId);
    }

    public List<Panier> getAllPaniersDisponibles() {
        return panierDao.getAllPaniersDisponibles();
    }

    public void deleteById(long panierId) {
        panierDao.deleteById(panierId);
    }

    /** Décrémente la quantité disponible (à appeler dans une transaction avec la réservation). */
    public void decrementQuantite(long panierId) {
        panierDao.decrementQuantite(panierId);
    }
}

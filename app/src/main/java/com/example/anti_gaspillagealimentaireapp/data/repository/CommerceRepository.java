package com.example.anti_gaspillagealimentaireapp.data.repository;

import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceDao;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce;

import java.util.List;

/**
 * Repository pour les commerces et relations Commerce-Paniers.
 */
public class CommerceRepository {

    private final CommerceDao commerceDao;

    public CommerceRepository(CommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }

    public long insert(Commerce commerce) {
        return commerceDao.insert(commerce);
    }

    public Commerce getById(long commerceId) {
        return commerceDao.getById(commerceId);
    }

    public List<Commerce> getAllActive() {
        return commerceDao.getAllActive();
    }

    public List<Commerce> getByCommercant(long commercantId) {
        return commerceDao.getByCommercant(commercantId);
    }

    public List<Commerce> getByCategorie(String categorie) {
        return commerceDao.getByCategorie(categorie);
    }

    public CommerceWithPaniers getCommerceWithPaniers(long commerceId) {
        return commerceDao.getCommerceWithPaniers(commerceId);
    }

    public List<CommerceWithPaniers> getAllCommercesWithPaniers() {
        return commerceDao.getAllCommercesWithPaniers();
    }
}

package com.example.anti_gaspillagealimentaireapp.data.repository;

import com.example.anti_gaspillagealimentaireapp.data.local.dao.FavoriteDao;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Favorite;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.List;

public class FavoriteRepository {

    private final FavoriteDao favoriteDao;

    public FavoriteRepository(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public List<Long> getFavoritePanierIds(long userId) {
        return favoriteDao.getFavoritePanierIds(userId);
    }

    public List<Panier> getFavoritePaniers(long userId) {
        return favoriteDao.getFavoritePaniers(userId);
    }

    public void toggleFavorite(long userId, long panierId) {
        boolean alreadyFavorite = favoriteDao.isFavorite(userId, panierId);
        if (alreadyFavorite) {
            favoriteDao.delete(userId, panierId);
        } else {
            favoriteDao.insert(new Favorite(0, userId, panierId, System.currentTimeMillis()));
        }
    }
}

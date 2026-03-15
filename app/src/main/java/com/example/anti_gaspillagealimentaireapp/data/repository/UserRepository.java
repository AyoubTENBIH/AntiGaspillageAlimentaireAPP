package com.example.anti_gaspillagealimentaireapp.data.repository;

import com.example.anti_gaspillagealimentaireapp.data.local.dao.UserDao;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.User;

import java.util.List;

/**
 * Repository pour la gestion des utilisateurs (auth, CRUD).
 */
public class UserRepository {

    private final UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public long insert(User user) {
        return userDao.insert(user);
    }

    public User getById(long userId) {
        return userDao.getById(userId);
    }

    public User getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    /** Vérifie les identifiants et retourne l'utilisateur si OK. */
    public User login(String email, String passwordHash) {
        return userDao.getByEmailAndPassword(email, passwordHash);
    }

    public List<User> getAllCommercants() {
        return userDao.getAllCommercants();
    }

    public void updatePassword(long userId, String newPasswordHash) {
        userDao.updatePassword(userId, newPasswordHash);
    }
}

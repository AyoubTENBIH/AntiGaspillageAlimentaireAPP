package com.example.anti_gaspillagealimentaireapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitaires de hash pour les mots de passe (stockage local simple).
 */
public final class HashUtils {

    private HashUtils() {}

    /** Hash MD5 simple pour démo locale (ne pas utiliser en production réelle). */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

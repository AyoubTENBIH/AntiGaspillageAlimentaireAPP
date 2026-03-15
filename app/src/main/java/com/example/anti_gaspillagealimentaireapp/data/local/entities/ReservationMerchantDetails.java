package com.example.anti_gaspillagealimentaireapp.data.local.entities;

/**
 * Réservation avec détails panier + client (pour l'écran commerçant).
 */
public class ReservationMerchantDetails {
    public final long id;
    public final long clientId;
    public final long panierId;
    public final long dateReservation;
    public final ReservationStatut statut;
    public final String panierTitre;
    public final double panierPrix;
    public final String panierImageUrl;
    public final String clientNom;

    public ReservationMerchantDetails(long id, long clientId, long panierId, long dateReservation,
                                      ReservationStatut statut, String panierTitre, double panierPrix,
                                      String panierImageUrl, String clientNom) {
        this.id = id;
        this.clientId = clientId;
        this.panierId = panierId;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.panierTitre = panierTitre;
        this.panierPrix = panierPrix;
        this.panierImageUrl = panierImageUrl;
        this.clientNom = clientNom;
    }
}

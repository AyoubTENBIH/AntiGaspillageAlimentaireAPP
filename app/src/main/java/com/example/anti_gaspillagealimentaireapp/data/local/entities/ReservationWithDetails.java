package com.example.anti_gaspillagealimentaireapp.data.local.entities;

/**
 * Réservation avec titre du panier et nom du commerce (pour l'affichage Historique).
 */
public class ReservationWithDetails {
    public final long id;
    public final long clientId;
    public final long panierId;
    public final long dateReservation;
    public final ReservationStatut statut;
    public final String panierTitre;
    public final String commerceNom;
    public final String panierImageUrl;

    public ReservationWithDetails(long id, long clientId, long panierId, long dateReservation,
                                  ReservationStatut statut, String panierTitre, String commerceNom,
                                  String panierImageUrl) {
        this.id = id;
        this.clientId = clientId;
        this.panierId = panierId;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.panierTitre = panierTitre;
        this.commerceNom = commerceNom;
        this.panierImageUrl = panierImageUrl;
    }
}

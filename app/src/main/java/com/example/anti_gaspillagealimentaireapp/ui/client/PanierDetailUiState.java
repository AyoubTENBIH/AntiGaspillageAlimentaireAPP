package com.example.anti_gaspillagealimentaireapp.ui.client;

import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

public class PanierDetailUiState {
    public final Panier panier;
    public final String commerceName;
    public final Boolean reserveSuccess; // null = no result yet, true/false = result
    public final boolean reserveError;

    public PanierDetailUiState(Panier panier, String commerceName, Boolean reserveSuccess, boolean reserveError) {
        this.panier = panier;
        this.commerceName = commerceName != null ? commerceName : "";
        this.reserveSuccess = reserveSuccess;
        this.reserveError = reserveError;
    }

    public PanierDetailUiState() {
        this(null, null, null, false);
    }
}

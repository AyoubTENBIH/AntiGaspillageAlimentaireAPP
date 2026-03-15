package com.example.anti_gaspillagealimentaireapp.ui.client;

import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.ArrayList;
import java.util.List;

public class FavorisUiState {
    public final List<Panier> paniers;

    public FavorisUiState(List<Panier> paniers) {
        this.paniers = paniers != null ? paniers : new ArrayList<Panier>();
    }

    public FavorisUiState() {
        this(null);
    }
}

package com.example.anti_gaspillagealimentaireapp.ui.client;

import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;

public class CommerceDetailUiState {
    public final CommerceWithPaniers commerceWithPaniers;

    public CommerceDetailUiState(CommerceWithPaniers commerceWithPaniers) {
        this.commerceWithPaniers = commerceWithPaniers;
    }

    public CommerceDetailUiState() {
        this(null);
    }
}

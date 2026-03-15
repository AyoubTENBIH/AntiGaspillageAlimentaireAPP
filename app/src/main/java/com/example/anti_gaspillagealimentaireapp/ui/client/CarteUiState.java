package com.example.anti_gaspillagealimentaireapp.ui.client;

import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;

import java.util.ArrayList;
import java.util.List;

public class CarteUiState {
    public final List<CommerceWithPaniers> commerces;

    public CarteUiState(List<CommerceWithPaniers> commerces) {
        this.commerces = commerces != null ? commerces : new ArrayList<CommerceWithPaniers>();
    }

    public CarteUiState() {
        this(null);
    }
}

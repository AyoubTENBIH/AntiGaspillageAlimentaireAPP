package com.example.anti_gaspillagealimentaireapp.ui.client;

import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationWithDetails;

import java.util.ArrayList;
import java.util.List;

public class HistoryUiState {
    public final List<ReservationWithDetails> reservations;
    public final int selectedTab;

    public HistoryUiState(List<ReservationWithDetails> reservations, int selectedTab) {
        this.reservations = reservations != null ? reservations : new ArrayList<ReservationWithDetails>();
        this.selectedTab = selectedTab;
    }

    public HistoryUiState() {
        this(null, 0);
    }
}

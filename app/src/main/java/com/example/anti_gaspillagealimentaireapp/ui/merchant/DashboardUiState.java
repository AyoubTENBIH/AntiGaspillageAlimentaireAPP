package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationCountByDay;

import java.util.ArrayList;
import java.util.List;

public class DashboardUiState {
    public final String userName;
    public final int activePaniers;
    public final int reservationsToday;
    public final double revenueWeek;
    public final List<Panier> paniers;
    public final List<ReservationCountByDay> stats7Days;

    public DashboardUiState(String userName, int activePaniers, int reservationsToday, double revenueWeek,
                            List<Panier> paniers, List<ReservationCountByDay> stats7Days) {
        this.userName = userName != null ? userName : "";
        this.activePaniers = activePaniers;
        this.reservationsToday = reservationsToday;
        this.revenueWeek = revenueWeek;
        this.paniers = paniers != null ? paniers : new ArrayList<Panier>();
        this.stats7Days = stats7Days != null ? stats7Days : new ArrayList<ReservationCountByDay>();
    }

    public DashboardUiState() {
        this("", 0, 0, 0.0, null, null);
    }
}

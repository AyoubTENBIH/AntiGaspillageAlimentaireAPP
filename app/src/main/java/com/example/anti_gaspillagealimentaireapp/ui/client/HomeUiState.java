package com.example.anti_gaspillagealimentaireapp.ui.client;

import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeUiState {
    public final List<CommerceWithPaniers> commercesWithPaniers;
    public final List<Panier> paniersDisponibles;
    public final Set<Long> favoritePanierIds;
    public final String searchQuery;
    public final boolean isLoading;

    public HomeUiState(List<CommerceWithPaniers> commercesWithPaniers, List<Panier> paniersDisponibles,
                       Set<Long> favoritePanierIds, String searchQuery, boolean isLoading) {
        this.commercesWithPaniers = commercesWithPaniers != null ? commercesWithPaniers : new ArrayList<CommerceWithPaniers>();
        this.paniersDisponibles = paniersDisponibles != null ? paniersDisponibles : new ArrayList<Panier>();
        this.favoritePanierIds = favoritePanierIds != null ? favoritePanierIds : new HashSet<Long>();
        this.searchQuery = searchQuery != null ? searchQuery : "";
        this.isLoading = isLoading;
    }

    public HomeUiState() {
        this(null, null, null, null, false);
    }

    public HomeUiState copyWithSearch(String searchQuery) {
        return new HomeUiState(commercesWithPaniers, paniersDisponibles, favoritePanierIds, searchQuery, isLoading);
    }

    public HomeUiState copyWithFavorites(Set<Long> favoritePanierIds) {
        return new HomeUiState(commercesWithPaniers, paniersDisponibles, favoritePanierIds, searchQuery, isLoading);
    }

    public List<CommerceWithPaniers> filteredCommerces() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) return commercesWithPaniers;
        String q = searchQuery.toLowerCase();
        List<CommerceWithPaniers> out = new ArrayList<>();
        for (CommerceWithPaniers cwp : commercesWithPaniers) {
            if (cwp.commerce.nom.toLowerCase().contains(q) || cwp.commerce.categorie.toLowerCase().contains(q)) {
                out.add(cwp);
                continue;
            }
            for (Panier p : cwp.paniers) {
                if (p.titre.toLowerCase().contains(q)) { out.add(cwp); break; }
            }
        }
        return out;
    }

    public List<Panier> filteredPaniers(Map<Long, String> commerceNameById) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) return paniersDisponibles;
        String q = searchQuery.toLowerCase();
        List<Panier> out = new ArrayList<>();
        for (Panier p : paniersDisponibles) {
            if (p.titre.toLowerCase().contains(q)) { out.add(p); continue; }
            String name = commerceNameById != null ? commerceNameById.get(p.commerceId) : null;
            if (name != null && name.toLowerCase().contains(q)) out.add(p);
        }
        return out;
    }
}

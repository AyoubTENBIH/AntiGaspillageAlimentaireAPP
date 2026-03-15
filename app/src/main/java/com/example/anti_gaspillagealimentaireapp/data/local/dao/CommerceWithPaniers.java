package com.example.anti_gaspillagealimentaireapp.data.local.dao;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;

import java.util.List;

public class CommerceWithPaniers {
    @Embedded
    public Commerce commerce;
    @Relation(parentColumn = "id", entityColumn = "commerceId", entity = Panier.class)
    public List<Panier> paniers;
}

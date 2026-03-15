package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemPanierCardBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PanierCardAdapter extends RecyclerView.Adapter<PanierCardAdapter.VH> {

    private final List<Panier> items;
    private final Set<Long> favoritePanierIds;
    private final Map<Long, String> commerceNameById;
    private final OnPanierClickListener onPanierClick;
    private final OnFavoriteClickListener onFavoriteClick;

    public interface OnPanierClickListener { void onPanierClick(long panierId); }
    public interface OnFavoriteClickListener { void onFavoriteClick(long panierId); }

    public PanierCardAdapter(List<Panier> items, Set<Long> favoritePanierIds, Map<Long, String> commerceNameById, OnPanierClickListener onPanierClick, OnFavoriteClickListener onFavoriteClick) {
        this.items = items != null ? items : new java.util.ArrayList<>();
        this.favoritePanierIds = favoritePanierIds != null ? favoritePanierIds : new java.util.HashSet<>();
        this.commerceNameById = commerceNameById != null ? commerceNameById : new java.util.HashMap<>();
        this.onPanierClick = onPanierClick;
        this.onFavoriteClick = onFavoriteClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemPanierCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Panier item = items.get(position);
        holder.binding.tvNomPanier.setText(item.titre);
        holder.binding.tvCommerce.setText(commerceNameById.containsKey(item.commerceId) ? commerceNameById.get(item.commerceId) : "");
        holder.binding.tvPrix.setText(String.format("%.2f €", item.prix));
        double prixOriginal = item.prix * 1.4;
        holder.binding.tvPrixOriginal.setText(String.format("%.2f €", prixOriginal));
        holder.binding.tvPrixOriginal.setPaintFlags(holder.binding.tvPrixOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.tvQuantite.setText(holder.itemView.getContext().getString(R.string.paniers_count, item.quantiteDisponible));
        Glide.with(holder.binding.imgPanier).load(item.imageUrl).placeholder(R.drawable.placeholder_panier).error(R.drawable.placeholder_panier).centerCrop().transition(DrawableTransitionOptions.withCrossFade(250)).into(holder.binding.imgPanier);
        boolean isFavorite = favoritePanierIds.contains(item.id);
        AnimationUtilsHelper.toggleFavorite(holder.binding.btnFavori, isFavorite);
        holder.binding.btnFavori.setOnClickListener(v -> {
            AnimationUtilsHelper.toggleFavorite(holder.binding.btnFavori, !isFavorite);
            onFavoriteClick.onFavoriteClick(item.id);
        });
        holder.binding.btnReserver.setOnClickListener(v -> AnimationUtilsHelper.popAnimation(holder.binding.btnReserver, () -> onPanierClick.onPanierClick(item.id)));
        holder.binding.getRoot().setOnClickListener(v -> onPanierClick.onPanierClick(item.id));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPanierCardBinding binding;
        VH(ItemPanierCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemCommerceCardBinding;

import java.util.List;

public class CommerceAdapter extends RecyclerView.Adapter<CommerceAdapter.VH> {

    private final Context context;
    private final List<CommerceWithPaniers> items;
    private final OnCommerceClickListener onCommerceClick;

    public interface OnCommerceClickListener { void onCommerceClick(long commerceId); }

    public CommerceAdapter(Context context, List<CommerceWithPaniers> items, OnCommerceClickListener onCommerceClick) {
        this.context = context;
        this.items = items != null ? items : new java.util.ArrayList<>();
        this.onCommerceClick = onCommerceClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCommerceCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CommerceWithPaniers item = items.get(position);
        holder.binding.name.setText(item.commerce.nom);
        holder.binding.address.setText(item.commerce.adresse);
        holder.binding.categoryBadge.setText(item.commerce.categorie);
        int count = 0;
        for (com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier p : item.paniers)
            if (p.isAvailable && p.quantiteDisponible > 0) count++;
        holder.binding.paniersCount.setText(context.getString(R.string.paniers_count, count));
        Glide.with(holder.binding.image).load(item.commerce.imageUrl).placeholder(R.drawable.placeholder_commerce).error(R.drawable.placeholder_commerce).centerCrop().transition(DrawableTransitionOptions.withCrossFade(250)).into(holder.binding.image);
        holder.binding.getRoot().setOnClickListener(v -> onCommerceClick.onCommerceClick(item.commerce.id));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemCommerceCardBinding binding;
        VH(ItemCommerceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

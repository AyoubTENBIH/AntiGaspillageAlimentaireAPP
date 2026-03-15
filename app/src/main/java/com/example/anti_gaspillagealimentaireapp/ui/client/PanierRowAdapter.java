package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemPanierRowBinding;

import java.util.List;

public class PanierRowAdapter extends RecyclerView.Adapter<PanierRowAdapter.VH> {

    private final List<Panier> items;
    private final OnPanierClickListener onPanierClick;

    public interface OnPanierClickListener { void onPanierClick(long panierId); }

    public PanierRowAdapter(List<Panier> items, OnPanierClickListener onPanierClick) {
        this.items = items != null ? items : new java.util.ArrayList<>();
        this.onPanierClick = onPanierClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemPanierRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Panier item = items.get(position);
        holder.binding.title.setText(item.titre);
        holder.binding.price.setText(String.format("%.2f €", item.prix));
        holder.binding.getRoot().setOnClickListener(v -> onPanierClick.onPanierClick(item.id));
        Glide.with(holder.binding.image).load(item.imageUrl).placeholder(R.drawable.placeholder_panier).error(R.drawable.placeholder_panier).centerCrop().transition(DrawableTransitionOptions.withCrossFade(250)).into(holder.binding.image);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPanierRowBinding binding;
        VH(ItemPanierRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

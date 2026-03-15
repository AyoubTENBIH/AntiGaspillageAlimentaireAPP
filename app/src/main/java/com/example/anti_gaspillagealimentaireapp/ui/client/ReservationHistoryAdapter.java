package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationWithDetails;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemReservationBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationHistoryAdapter extends RecyclerView.Adapter<ReservationHistoryAdapter.VH> {

    private final List<ReservationWithDetails> items;
    private final OnCancelListener onCancel;

    public interface OnCancelListener { void onCancel(long reservationId); }

    public ReservationHistoryAdapter(List<ReservationWithDetails> items, OnCancelListener onCancel) {
        this.items = items != null ? items : new java.util.ArrayList<>();
        this.onCancel = onCancel;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemReservationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ReservationWithDetails r = items.get(position);
        holder.binding.panierTitle.setText(r.panierTitre != null && !r.panierTitre.isEmpty() ? r.panierTitre : "Réservation #" + r.id);
        holder.binding.commerceName.setText(r.commerceNom);
        holder.binding.date.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(r.dateReservation)));
        Glide.with(holder.binding.image.getContext()).load(r.panierImageUrl).placeholder(R.drawable.placeholder_panier).error(R.drawable.placeholder_panier).centerCrop().transition(DrawableTransitionOptions.withCrossFade(200)).into(holder.binding.image);
        String status = r.statut == ReservationStatut.EN_ATTENTE ? holder.itemView.getContext().getString(R.string.status_pending) : r.statut == ReservationStatut.CONFIRMEE ? holder.itemView.getContext().getString(R.string.status_confirmed) : holder.itemView.getContext().getString(R.string.status_cancelled);
        holder.binding.statusBadge.setText(status);
        holder.binding.getRoot().setOnLongClickListener(v -> {
            if (r.statut == ReservationStatut.EN_ATTENTE) {
                new AlertDialog.Builder(holder.itemView.getContext())
                    .setMessage(R.string.cancel_confirm)
                    .setPositiveButton(R.string.yes, (d, w) -> onCancel.onCancel(r.id))
                    .setNegativeButton(R.string.no, null)
                    .show();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemReservationBinding binding;
        VH(ItemReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

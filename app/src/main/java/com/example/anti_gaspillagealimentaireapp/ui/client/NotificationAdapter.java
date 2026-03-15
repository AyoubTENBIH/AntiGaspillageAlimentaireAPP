package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.AppNotification;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemNotificationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VH> {

    private List<AppNotification> items = new ArrayList<>();
    private final OnItemClickListener onItemClick;

    public interface OnItemClickListener { void onItemClick(AppNotification notification); }

    public NotificationAdapter(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void submitList(List<AppNotification> list) {
        this.items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AppNotification n = items.get(position);
        holder.binding.tvNotificationTitle.setText(n.title);
        holder.binding.tvNotificationMessage.setText(n.message);
        holder.binding.tvNotificationDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(n.dateCreated)));
        holder.binding.getRoot().setAlpha(n.isRead ? 0.85f : 1f);
        holder.binding.getRoot().setOnClickListener(v -> onItemClick.onItemClick(n));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemNotificationBinding binding;
        VH(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

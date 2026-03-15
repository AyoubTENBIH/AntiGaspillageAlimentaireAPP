package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationMerchantDetails;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemReservationMerchantBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationMerchantAdapter extends ListAdapter<ReservationMerchantDetails, ReservationMerchantAdapter.VH> {

    private final boolean showActions;
    private final OnConfirmListener onConfirm;
    private final OnCancelListener onCancel;

    public interface OnConfirmListener { void onConfirm(ReservationMerchantDetails item); }
    public interface OnCancelListener { void onCancel(ReservationMerchantDetails item); }

    public ReservationMerchantAdapter(boolean showActions, OnConfirmListener onConfirm, OnCancelListener onCancel) {
        super(new DiffUtil.ItemCallback<ReservationMerchantDetails>() {
            @Override
            public boolean areItemsTheSame(@NonNull ReservationMerchantDetails a, @NonNull ReservationMerchantDetails b) {
                return a.id == b.id;
            }
            @Override
            public boolean areContentsTheSame(@NonNull ReservationMerchantDetails a, @NonNull ReservationMerchantDetails b) {
                return a.id == b.id && a.dateReservation == b.dateReservation && a.statut == b.statut;
            }
        });
        this.showActions = showActions;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemReservationMerchantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItem(position));
    }

    class VH extends RecyclerView.ViewHolder {
        private final ItemReservationMerchantBinding binding;

        VH(ItemReservationMerchantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ReservationMerchantDetails item) {
            binding.tvPanierTitreResa.setText(item.panierTitre);
            binding.tvClientNom.setText("Client : " + item.clientNom);
            binding.tvDateResa.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH).format(new Date(item.dateReservation)));
            binding.rowActionsResa.setVisibility(showActions ? View.VISIBLE : View.GONE);
            binding.btnConfirmerResa.setOnClickListener(v -> onConfirm.onConfirm(item));
            binding.btnAnnulerResa.setOnClickListener(v -> onCancel.onCancel(item));

            String label;
            int bgRes;
            int textColorRes;
            if (item.statut == ReservationStatut.EN_ATTENTE) {
                label = "En attente";
                bgRes = R.drawable.bg_chip_grey;
                textColorRes = R.color.text_secondary;
            } else if (item.statut == ReservationStatut.CONFIRMEE) {
                label = "Confirmée";
                bgRes = R.drawable.bg_badge_green;
                textColorRes = R.color.status_confirmed;
            } else {
                label = "Annulée";
                bgRes = R.drawable.bg_badge_red;
                textColorRes = R.color.status_cancelled;
            }
            binding.tvBadgeStatut.setText(label);
            binding.tvBadgeStatut.setBackgroundResource(bgRes);
            binding.tvBadgeStatut.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), textColorRes));

            if (item.panierImageUrl != null && !item.panierImageUrl.isEmpty()) {
                int radius = (int) (10 * binding.getRoot().getContext().getResources().getDisplayMetrics().density);
                Glide.with(binding.getRoot().getContext())
                    .load(item.panierImageUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                    .placeholder(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.bg_rounded_image_merchant))
                    .into(binding.imgPanierResa);
            } else {
                binding.imgPanierResa.setImageDrawable(null);
                binding.imgPanierResa.setBackgroundResource(R.drawable.bg_rounded_image_merchant);
            }
        }
    }
}

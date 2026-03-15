package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.view.LayoutInflater;
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
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.ItemPanierMerchantBinding;

public class PanierMerchantAdapter extends ListAdapter<Panier, PanierMerchantAdapter.VH> {

    public interface Listener {
        void onItemClick(Panier panier);
        void onEditClick(Panier panier);
        void onDeleteClick(Panier panier);
    }

    private final Listener listener;

    public PanierMerchantAdapter(Listener listener) {
        super(new DiffUtil.ItemCallback<Panier>() {
            @Override
            public boolean areItemsTheSame(@NonNull Panier a, @NonNull Panier b) {
                return a.id == b.id;
            }
            @Override
            public boolean areContentsTheSame(@NonNull Panier a, @NonNull Panier b) {
                return a.id == b.id && a.titre.equals(b.titre) && a.prix == b.prix && a.quantiteDisponible == b.quantiteDisponible && a.isAvailable == b.isAvailable;
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemPanierMerchantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItem(position));
    }

    class VH extends RecyclerView.ViewHolder {
        private final ItemPanierMerchantBinding binding;

        VH(ItemPanierMerchantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Panier panier) {
            binding.tvNomPanierMerchant.setText(panier.titre);
            binding.tvPrixMerchant.setText(String.format("%.2f €", panier.prix));
            binding.tvQuantiteMerchant.setText(panier.quantiteDisponible + " dispo");
            boolean actif = panier.isAvailable && panier.quantiteDisponible > 0;
            binding.tvStatutPanier.setText(actif ? "Actif" : "Épuisé");
            binding.tvStatutPanier.setBackgroundResource(actif ? R.drawable.bg_badge_green : R.drawable.bg_badge_red);
            binding.tvStatutPanier.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), actif ? R.color.status_confirmed : R.color.status_cancelled));
            if (panier.imageUrl != null && !panier.imageUrl.isEmpty()) {
                int radius = (int) (12 * binding.getRoot().getContext().getResources().getDisplayMetrics().density);
                Glide.with(binding.getRoot().getContext())
                    .load(panier.imageUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                    .placeholder(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.bg_rounded_image_merchant))
                    .into(binding.imgPanierMerchant);
            } else {
                binding.imgPanierMerchant.setImageDrawable(null);
                binding.imgPanierMerchant.setBackgroundResource(R.drawable.bg_rounded_image_merchant);
            }
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(panier));
            binding.btnEditPanier.setOnClickListener(v -> listener.onEditClick(panier));
            binding.btnDeletePanier.setOnClickListener(v -> listener.onDeleteClick(panier));
        }
    }
}

package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentPanierDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PanierDetailFragment extends Fragment {

    private FragmentPanierDetailBinding binding;
    private PanierDetailViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.google.android.material.transition.MaterialContainerTransform enter = new com.google.android.material.transition.MaterialContainerTransform();
        enter.setDuration(400);
        enter.setScrimColor(android.graphics.Color.TRANSPARENT);
        setSharedElementEnterTransition(enter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPanierDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long panierId = getArguments() != null ? getArguments().getLong("panierId", 0L) : 0L;
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(PanierDetailViewModel.class);
        viewModel.load(panierId);

        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.buttonReserve.setOnClickListener(v -> viewModel.reserve());
        binding.btnFavori.setOnClickListener(v -> { /* TODO toggle favori */ });

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            Panier panier = state.panier;
            if (panier != null && binding != null) {
                binding.heroImage.setTransitionName("panier_image_" + panier.id);
                binding.panierTitle.setText(panier.titre);
                binding.commerceName.setText(state.commerceName != null ? state.commerceName : "");
                binding.description.setText(panier.description);
                double prixOriginal = panier.prix * 1.4;
                binding.tvPrix.setText(String.format("%.2f €", panier.prix));
                binding.tvPrixOriginal.setText(String.format("%.2f €", prixOriginal));
                binding.tvPrixOriginal.setPaintFlags(binding.tvPrixOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                int economiePct = prixOriginal > 0 ? (int) ((prixOriginal - panier.prix) / prixOriginal * 100) : 0;
                binding.tvBadgeEconomie.setText("-" + economiePct + "%");
                binding.tvEconomieValeur.setText("-" + economiePct + "%");
                binding.tvQuantiteValeur.setText(getString(R.string.panier_quantity_units, panier.quantiteDisponible));
                long diffMs = panier.dateExpiration - System.currentTimeMillis();
                int diffDays = (int) (diffMs / (1000 * 60 * 60 * 24));
                binding.tvFraicheurValeur.setText(diffDays <= 0 ? getString(R.string.freshness_today) : diffDays == 1 ? getString(R.string.freshness_1day) : getString(R.string.freshness_days, diffDays));
                double co2Kg = panier.quantiteDisponible * 0.5;
                binding.tvCo2Valeur.setText(getString(R.string.co2_kg, String.format("%.1f", co2Kg)));
                binding.tvDateExpiration.setText(getString(R.string.expires_on, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(panier.dateExpiration))));
                Glide.with(binding.heroImage).load(panier.imageUrl).placeholder(R.drawable.placeholder_panier).error(R.drawable.placeholder_panier).centerCrop().transition(DrawableTransitionOptions.withCrossFade(300)).into(binding.heroImage);
            }
            if (state.reserveSuccess != null && state.reserveSuccess) {
                Toast.makeText(requireContext(), getString(R.string.reservation_success), Toast.LENGTH_SHORT).show();
                viewModel.consumeReserveResult();
                NavHostFragment.findNavController(this).navigate(R.id.historyFragment);
            }
            if (state.reserveError) {
                Toast.makeText(requireContext(), getString(R.string.reservation_error), Toast.LENGTH_SHORT).show();
                viewModel.consumeReserveResult();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

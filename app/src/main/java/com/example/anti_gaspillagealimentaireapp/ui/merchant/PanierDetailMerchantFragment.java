package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentPanierDetailMerchantBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PanierDetailMerchantFragment extends Fragment {

    private FragmentPanierDetailMerchantBinding binding;
    private PanierDetailMerchantViewModel viewModel;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    private long getPanierId() {
        return getArguments() != null ? getArguments().getLong("panierId", -1L) : -1L;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPanierDetailMerchantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long panierId = getPanierId();
        if (panierId < 0) {
            NavHostFragment.findNavController(this).navigateUp();
            return;
        }
        viewModel = new ViewModelProvider(this, new PanierDetailMerchantViewModel.Factory(panierId, (SaveAtApplication) requireActivity().getApplication())).get(PanierDetailMerchantViewModel.class);

        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        viewModel.getPanier().observe(getViewLifecycleOwner(), panier -> {
            if (panier == null || binding == null) return;
            binding.tvNomPanier.setText(panier.titre);
            binding.tvPrix.setText(String.format("%.2f €", panier.prix));
            binding.tvDescription.setText(panier.description);
            binding.tvQuantiteRestante.setText(String.valueOf(panier.quantiteDisponible));
            binding.tvDateExpiration.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(new Date(panier.dateExpiration)));
            boolean isActif = panier.isAvailable && panier.quantiteDisponible > 0;
            binding.tvBadgeDisponible.setText(isActif ? "Actif" : "Épuisé");
            binding.tvBadgeDisponible.setBackgroundResource(isActif ? R.drawable.bg_badge_green : R.drawable.bg_badge_red);
            binding.tvBadgeDisponible.setTextColor(ContextCompat.getColor(requireContext(), isActif ? R.color.status_confirmed : R.color.status_cancelled));
            binding.switchDisponibilite.setOnCheckedChangeListener(null);
            binding.switchDisponibilite.setChecked(panier.isAvailable);
            binding.switchDisponibilite.setOnCheckedChangeListener((btn, checked) -> viewModel.toggleDisponibilite(checked));
            String imgUrl = (panier.imageUrl != null && !panier.imageUrl.isEmpty()) ? panier.imageUrl : null;
            Glide.with(this).load(imgUrl).centerCrop().transition(DrawableTransitionOptions.withCrossFade(300)).placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_image_merchant)).into(binding.imgHeroPanierMerchant);
            final long commerceId = panier.commerceId;
            executor.execute(() -> {
                com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce commerce = ((SaveAtApplication) requireActivity().getApplication()).getDatabase().commerceDao().getById(commerceId);
                String nom = commerce != null ? commerce.nom : "";
                mainHandler.post(() -> { if (binding != null) binding.tvCommerce.setText(nom); });
            });
        });

        viewModel.getStatsReservations().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null && binding != null) {
                AnimationUtilsHelper.animateCount(binding.tvTotalReservations, 0, stats.total);
                AnimationUtilsHelper.animateCount(binding.tvEnAttente, 0, stats.enAttente);
            }
        });

        binding.btnModifierPanier.setOnClickListener(v -> AddEditPanierBottomSheet.newInstance(panierId).show(getChildFragmentManager(), "edit_panier"));
        binding.btnSupprimerPanier.setOnClickListener(v -> new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Supprimer ce panier ?")
            .setMessage("Cette action est irréversible.")
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton("Supprimer", (d, w) -> {
                viewModel.deletePanier();
                NavHostFragment.findNavController(this).navigateUp();
            })
            .show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

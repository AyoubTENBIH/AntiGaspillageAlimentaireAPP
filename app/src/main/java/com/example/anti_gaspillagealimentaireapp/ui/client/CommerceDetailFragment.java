package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentCommerceDetailBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;

import java.util.ArrayList;
import java.util.List;

public class CommerceDetailFragment extends Fragment {

    private FragmentCommerceDetailBinding binding;
    private CommerceDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommerceDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long commerceId = getArguments() != null ? getArguments().getLong("commerceId", 0L) : 0L;
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(CommerceDetailViewModel.class);
        viewModel.load(commerceId);

        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.buttonVoirPaniers.setOnClickListener(v -> binding.recyclerPaniers.post(() -> {
            int[] location = new int[2];
            binding.recyclerPaniers.getLocationInWindow(location);
            int[] scrollLocation = new int[2];
            binding.scrollCommerceDetail.getLocationInWindow(scrollLocation);
            int targetScrollY = Math.max(0, binding.scrollCommerceDetail.getScrollY() + (location[1] - scrollLocation[1]) - 120);
            binding.scrollCommerceDetail.smoothScrollTo(0, targetScrollY);
        }));

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null || state.commerceWithPaniers == null) return;
            com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers cwp = state.commerceWithPaniers;
            binding.commerceName.setText(cwp.commerce.nom);
            binding.commerceAddress.setText(cwp.commerce.adresse);
            binding.commerceCategory.setText(cwp.commerce.categorie);
            Glide.with(binding.heroImage).load(cwp.commerce.imageUrl).placeholder(R.drawable.placeholder_commerce).error(R.drawable.placeholder_commerce).centerCrop().transition(DrawableTransitionOptions.withCrossFade(250)).into(binding.heroImage);
            List<Panier> disponibles = new ArrayList<>();
            for (Panier p : cwp.paniers) if (p.isAvailable && p.quantiteDisponible > 0) disponibles.add(p);
            binding.buttonVoirPaniers.setText(getString(R.string.paniers_available_count, disponibles.size()));
            binding.recyclerPaniers.setLayoutManager(new LinearLayoutManager(requireContext()));
            PanierRowAdapter adapter = new PanierRowAdapter(disponibles, panierId -> {
                Bundle args = new Bundle();
                args.putLong("panierId", panierId);
                NavHostFragment.findNavController(CommerceDetailFragment.this).navigate(R.id.panierDetailFragment, args, navOptions());
            });
            binding.recyclerPaniers.setAdapter(adapter);
            AnimationUtilsHelper.animateItems(binding.recyclerPaniers);
        });
    }

    private NavOptions navOptions() {
        return new NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left).setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

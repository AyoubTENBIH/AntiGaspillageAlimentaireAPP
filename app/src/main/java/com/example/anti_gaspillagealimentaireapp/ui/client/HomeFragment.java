package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentHomeBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private NotificationsViewModel notificationsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), insets.getInsets(WindowInsetsCompat.Type.statusBars()).top, v.getPaddingRight(), insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });
        binding.recyclerCommerces.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerPaniers.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(HomeViewModel.class);
        notificationsViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(NotificationsViewModel.class);

        viewModel.load();
        binding.btnNotifications.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.notificationsFragment));
        notificationsViewModel.getUnreadCount().observe(getViewLifecycleOwner(), count -> {
            int c = count != null ? count : 0;
            binding.badgeNotifications.setVisibility(c > 0 ? View.VISIBLE : View.GONE);
            binding.badgeNotifications.setText(c > 99 ? "99+" : (c > 0 ? String.valueOf(c) : ""));
        });
        binding.searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearch(s != null ? s.toString() : "");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            java.util.List<com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers> filteredCommerces = state.filteredCommerces();
            Map<Long, String> commerceNameById = new HashMap<>();
            for (com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers cwp : state.commercesWithPaniers)
                commerceNameById.put(cwp.commerce.id, cwp.commerce.nom);
            java.util.List<com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier> filteredPaniers = state.filteredPaniers(commerceNameById);

            CommerceAdapter commerceAdapter = new CommerceAdapter(requireContext(), filteredCommerces, commerceId -> {
                Bundle args = new Bundle();
                args.putLong("commerceId", commerceId);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.commerceDetailFragment, args, navOptions());
            });
            binding.recyclerCommerces.setAdapter(commerceAdapter);
            AnimationUtilsHelper.animateItems(binding.recyclerCommerces);

            PanierCardAdapter panierAdapter = new PanierCardAdapter(filteredPaniers, state.favoritePanierIds, commerceNameById,
                panierId -> {
                    Bundle args = new Bundle();
                    args.putLong("panierId", panierId);
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.panierDetailFragment, args, navOptions());
                },
                viewModel::toggleFavorite);
            binding.recyclerPaniers.setAdapter(panierAdapter);
            AnimationUtilsHelper.animateItems(binding.recyclerPaniers);
        });
    }

    private NavOptions navOptions() {
        return new NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

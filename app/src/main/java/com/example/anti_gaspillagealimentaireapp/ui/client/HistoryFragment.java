package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationWithDetails;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentHistoryBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), insets.getInsets(WindowInsetsCompat.Type.statusBars()).top, v.getPaddingRight(), insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(HistoryViewModel.class);
        viewModel.load();
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.tab_in_progress)));
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.tab_past)));
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.tab_cancelled)));
        binding.tabs.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                viewModel.setTab(tab != null ? tab.getPosition() : 0);
            }
            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {}
        });
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            List<ReservationWithDetails> filtered;
            if (state.selectedTab == 0) {
                filtered = new ArrayList<>();
                for (ReservationWithDetails r : state.reservations)
                    if (r.statut == ReservationStatut.EN_ATTENTE || r.statut == ReservationStatut.CONFIRMEE) filtered.add(r);
            } else if (state.selectedTab == 1) {
                filtered = new ArrayList<>();
                for (ReservationWithDetails r : state.reservations)
                    if (r.statut == ReservationStatut.CONFIRMEE) filtered.add(r);
            } else if (state.selectedTab == 2) {
                filtered = new ArrayList<>();
                for (ReservationWithDetails r : state.reservations)
                    if (r.statut == ReservationStatut.ANNULEE) filtered.add(r);
            } else {
                filtered = state.reservations;
            }
            boolean hasItems = !filtered.isEmpty();
            binding.emptyState.setVisibility(hasItems ? View.GONE : View.VISIBLE);
            binding.recycler.setVisibility(hasItems ? View.VISIBLE : View.GONE);
            binding.recycler.setAdapter(new ReservationHistoryAdapter(filtered, viewModel::cancelReservation));
            if (hasItems) AnimationUtilsHelper.animateItems(binding.recycler);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

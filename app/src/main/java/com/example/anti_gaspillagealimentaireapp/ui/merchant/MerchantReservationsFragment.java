package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentMerchantReservationsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MerchantReservationsFragment extends Fragment {

    private FragmentMerchantReservationsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMerchantReservationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewPagerReservations.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() { return 3; }
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return ReservationsTabFragment.newInstance(ReservationStatut.EN_ATTENTE);
                if (position == 1) return ReservationsTabFragment.newInstance(ReservationStatut.CONFIRMEE);
                return ReservationsTabFragment.newInstance(ReservationStatut.ANNULEE);
            }
        });
        new TabLayoutMediator(binding.tabsReservations, binding.viewPagerReservations, (tab, position) -> {
            if (position == 0) tab.setText("En attente");
            else if (position == 1) tab.setText("Confirmées");
            else tab.setText("Annulées");
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

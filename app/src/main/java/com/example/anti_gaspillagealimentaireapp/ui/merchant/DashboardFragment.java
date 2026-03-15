package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationCountByDay;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentDashboardBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private MerchantPaniersViewModel paniersViewModel;
    private PanierMerchantAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(DashboardViewModel.class);
        paniersViewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MerchantPaniersViewModel.class);

        viewModel.load();
        binding.rvPaniersDashboard.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PanierMerchantAdapter(new PanierMerchantAdapter.Listener() {
            @Override
            public void onItemClick(com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier panier) {}
            @Override
            public void onEditClick(com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier panier) {
                AddEditPanierBottomSheet.newInstance(panier.id).show(getChildFragmentManager(), "edit_panier");
            }
            @Override
            public void onDeleteClick(com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier panier) {
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Supprimer ce panier ?")
                    .setMessage("\"" + panier.titre + "\" sera définitivement supprimé.")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton("Supprimer", (d, w) -> {
                        paniersViewModel.deletePanier(panier);
                        Snackbar.make(binding.getRoot(), "Panier supprimé", Snackbar.LENGTH_SHORT)
                            .setAction("Annuler", v -> paniersViewModel.addPanier(panier))
                            .show();
                    })
                    .show();
            }
        });
        binding.rvPaniersDashboard.setAdapter(adapter);

        View[] statCards = new View[] { binding.cardPaniersActifs, binding.cardReservations, binding.cardRevenus };
        for (int i = 0; i < statCards.length; i++) {
            View card = statCards[i];
            int delay = i * 80;
            card.setAlpha(0f);
            card.setTranslationY(30f);
            card.animate().alpha(1f).translationY(0f).setDuration(350).setStartDelay(delay).setInterpolator(new DecelerateInterpolator()).start();
        }

        binding.btnAjouterPanier.setOnClickListener(v -> AddEditPanierBottomSheet.newInstance(null).show(getChildFragmentManager(), "add_panier"));
        binding.btnVoirReservations.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.reservationsFragment));
        binding.tvVoirTout.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.paniersFragment));

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            binding.tvGreeting.setText(getString(R.string.hello_user, state.userName));
            String dateStr = new SimpleDateFormat("EEEE d MMMM", Locale.getDefault()).format(new Date());
            if (dateStr.length() > 0) dateStr = dateStr.substring(0, 1).toUpperCase(Locale.getDefault()) + dateStr.substring(1);
            binding.tvDate.setText(dateStr);
            binding.tvAvatar.setText(state.userName.length() > 0 ? String.valueOf(state.userName.charAt(0)).toUpperCase(Locale.getDefault()) : "?");
            updateReservationsChart(binding.chartReservations, state.stats7Days);
        });

        viewModel.countPaniersActifs.observe(getViewLifecycleOwner(), count -> AnimationUtilsHelper.animateCount(binding.tvPaniersActifs, 0, count != null ? count : 0));
        viewModel.countReservationsToday.observe(getViewLifecycleOwner(), count -> AnimationUtilsHelper.animateCount(binding.tvReservationsAujourdhui, 0, count != null ? count : 0));
        viewModel.revenusWeek.observe(getViewLifecycleOwner(), revenus -> binding.tvRevenusText.setText(String.format("%.2f €", revenus != null ? revenus : 0.0)));

        paniersViewModel.getPaniers().observe(getViewLifecycleOwner(), paniers -> {
            adapter.submitList(paniers);
            binding.rvPaniersDashboard.post(() -> binding.rvPaniersDashboard.scheduleLayoutAnimation());
        });
    }

    private static void updateReservationsChart(com.github.mikephil.charting.charts.BarChart chart, List<ReservationCountByDay> stats) {
        if (stats == null) stats = new ArrayList<>();
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < stats.size(); i++) entries.add(new BarEntry(i, stats.get(i).count));
        BarDataSet dataSet = new BarDataSet(entries, "Réservations");
        dataSet.setColor(ContextCompat.getColor(chart.getContext(), R.color.accent_primary));
        dataSet.setValueTextSize(10f);
        chart.setData(new BarData(dataSet));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        List<String> labels = new ArrayList<>();
        for (ReservationCountByDay s : stats) labels.add(s.day);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setFitBars(true);
        chart.animateY(400);
        chart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentPaniersListBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class PaniersListFragment extends Fragment {

    private FragmentPaniersListBinding binding;
    private MerchantPaniersViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPaniersListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new androidx.lifecycle.ViewModelProvider(
            requireActivity(),
            androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(MerchantPaniersViewModel.class);

        binding.rvMesPaniers.setLayoutManager(new LinearLayoutManager(requireContext()));

        PanierMerchantAdapter adapter = new PanierMerchantAdapter(new PanierMerchantAdapter.Listener() {
            @Override
            public void onItemClick(Panier panier) {
                Bundle args = new Bundle();
                args.putLong("panierId", panier.id);
                NavHostFragment.findNavController(PaniersListFragment.this)
                    .navigate(R.id.action_paniers_to_panier_detail, args);
            }

            @Override
            public void onEditClick(Panier panier) {
                AddEditPanierBottomSheet.newInstance(panier.id)
                    .show(getChildFragmentManager(), "edit_panier");
            }

            @Override
            public void onDeleteClick(Panier panier) {
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Supprimer ce panier ?")
                    .setMessage("\"" + panier.titre + "\" sera définitivement supprimé.")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        viewModel.deletePanier(panier);
                        Snackbar.make(binding.getRoot(), "Panier supprimé", Snackbar.LENGTH_SHORT)
                            .setAction("Annuler", v -> viewModel.addPanier(panier))
                            .show();
                    })
                    .show();
            }
        });

        binding.rvMesPaniers.setAdapter(adapter);

        viewModel.getPaniers().observe(getViewLifecycleOwner(), paniers -> {
            adapter.submitList(paniers);
            boolean empty = paniers == null || paniers.isEmpty();
            binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvMesPaniers.setVisibility(empty ? View.GONE : View.VISIBLE);
            binding.rvMesPaniers.scheduleLayoutAnimation();
        });

        binding.fabAjouterPanier.setOnClickListener(v ->
            AddEditPanierBottomSheet.newInstance(null)
                .show(getChildFragmentManager(), "add_panier")
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


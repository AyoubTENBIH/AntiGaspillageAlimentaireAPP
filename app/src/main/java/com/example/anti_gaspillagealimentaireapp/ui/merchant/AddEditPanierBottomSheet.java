package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.databinding.BottomsheetAddEditPanierBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditPanierBottomSheet extends BottomSheetDialogFragment {

    public static final String ARG_PANIER_ID = "panier_id";

    private BottomsheetAddEditPanierBinding binding;
    private MerchantPaniersViewModel viewModel;
    private Long editingPanierId = null;
    private int currentQuantite = 1;
    private long selectedDateMs = System.currentTimeMillis() + 24L * 60 * 60 * 1000;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    public static AddEditPanierBottomSheet newInstance(Long panierId) {
        AddEditPanierBottomSheet f = new AddEditPanierBottomSheet();
        if (panierId != null && panierId > 0) {
            Bundle args = new Bundle();
            args.putLong(ARG_PANIER_ID, panierId);
            f.setArguments(args);
        }
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetAddEditPanierBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MerchantPaniersViewModel.class);

        long argId = getArguments() != null ? getArguments().getLong(ARG_PANIER_ID, -1L) : -1L;
        if (argId > 0) editingPanierId = argId;

        if (editingPanierId != null) {
            binding.tvBottomSheetTitle.setText(getString(R.string.edit_panier));
            binding.btnPublier.setText(getString(R.string.save));
            loadPanier(editingPanierId);
        } else {
            binding.tvBottomSheetTitle.setText(getString(R.string.new_panier));
            binding.btnPublier.setText(getString(R.string.publish_offer));
            binding.etDateExpiration.setText(formatDate(selectedDateMs));
        }

        binding.tvQuantite.setText(String.valueOf(currentQuantite));
        binding.btnQuantiteMoins.setOnClickListener(v -> {
            if (currentQuantite > 1) {
                currentQuantite--;
                binding.tvQuantite.setText(String.valueOf(currentQuantite));
            }
        });
        binding.btnQuantitePlus.setOnClickListener(v -> {
            if (currentQuantite < 99) {
                currentQuantite++;
                binding.tvQuantite.setText(String.valueOf(currentQuantite));
            }
        });

        binding.tilDateExpiration.setEndIconOnClickListener(v -> showDatePicker());
        binding.etDateExpiration.setOnClickListener(v -> showDatePicker());
        binding.btnPublier.setOnClickListener(v -> validateAndSave());
    }

    private void loadPanier(long panierId) {
        executor.execute(() -> {
            Panier panier = ((SaveAtApplication) requireActivity().getApplication()).getDatabase().panierDao().getById(panierId);
            if (panier == null) return;
            mainHandler.post(() -> {
                if (binding == null) return;
                binding.etTitre.setText(panier.titre);
                binding.etDescription.setText(panier.description);
                binding.etPrix.setText(String.valueOf(panier.prix));
                currentQuantite = Math.max(1, Math.min(99, panier.quantiteDisponible));
                binding.tvQuantite.setText(String.valueOf(currentQuantite));
                selectedDateMs = panier.dateExpiration;
                binding.etDateExpiration.setText(formatDate(panier.dateExpiration));
                binding.etImageUrl.setText(panier.imageUrl != null && !panier.imageUrl.isEmpty() ? panier.imageUrl : "");
            });
        });
    }

    private String formatDate(long ms) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(new Date(ms));
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(getString(R.string.panier_expiration));
        builder.setSelection(selectedDateMs);
        MaterialDatePicker<Long> picker = builder.build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectedDateMs = selection;
            binding.etDateExpiration.setText(formatDate(selection));
        });
        picker.show(getParentFragmentManager(), "date_picker");
    }

    private void validateAndSave() {
        boolean isValid = true;
        String titre = binding.etTitre.getText() != null ? binding.etTitre.getText().toString().trim() : "";
        if (titre.length() < 3) {
            binding.tilTitre.setError(getString(R.string.error_min_chars, 3));
            isValid = false;
        } else binding.tilTitre.setError(null);

        String description = binding.etDescription.getText() != null ? binding.etDescription.getText().toString().trim() : "";
        if (description.length() < 10) {
            binding.tilDescription.setError(getString(R.string.error_min_chars, 10));
            isValid = false;
        } else binding.tilDescription.setError(null);

        String prixStr = binding.etPrix.getText() != null ? binding.etPrix.getText().toString() : "";
        double prix = 0;
        try { prix = Double.parseDouble(prixStr); } catch (NumberFormatException ignored) {}
        if (prix <= 0) {
            binding.tilPrix.setError(getString(R.string.error_invalid_price));
            isValid = false;
        } else binding.tilPrix.setError(null);

        if (!isValid) return;

        long commerceId = viewModel.getCommerceIdValue();
        if (commerceId <= 0) {
            new MaterialAlertDialogBuilder(requireContext())
                .setMessage("Aucun commerce associé. Reconnectez-vous.")
                .setPositiveButton(android.R.string.ok, (d, w) -> dismiss())
                .show();
            return;
        }

        String imageUrl = binding.etImageUrl.getText() != null ? binding.etImageUrl.getText().toString().trim() : "";
        if (imageUrl.isEmpty()) imageUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e?w=400&q=80";

        Panier panier = new Panier(
            editingPanierId != null ? editingPanierId : 0L,
            titre,
            description,
            prix,
            currentQuantite,
            commerceId,
            selectedDateMs,
            imageUrl,
            true
        );
        if (editingPanierId == null) viewModel.addPanier(panier);
        else viewModel.updatePanier(panier);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

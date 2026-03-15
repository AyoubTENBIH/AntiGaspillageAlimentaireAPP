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
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentFavorisPlaceholderBinding;
import com.example.anti_gaspillagealimentaireapp.utils.AnimationUtilsHelper;

public class FavorisFragment extends Fragment {

    private FragmentFavorisPlaceholderBinding binding;
    private FavorisViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavorisPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), insets.getInsets(WindowInsetsCompat.Type.statusBars()).top, v.getPaddingRight(), insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });
        binding.recyclerFavorites.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(FavorisViewModel.class);
        viewModel.load();

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            boolean hasItems = !state.paniers.isEmpty();
            binding.emptyState.setVisibility(hasItems ? View.GONE : View.VISIBLE);
            binding.recyclerFavorites.setVisibility(hasItems ? View.VISIBLE : View.GONE);
            FavorisPanierAdapter adapter = new FavorisPanierAdapter(state.paniers,
                panierId -> {
                    Bundle args = new Bundle();
                    args.putLong("panierId", panierId);
                    NavHostFragment.findNavController(FavorisFragment.this).navigate(R.id.panierDetailFragment, args, navOptions());
                },
                viewModel::toggleFavorite);
            binding.recyclerFavorites.setAdapter(adapter);
            if (hasItems) AnimationUtilsHelper.animateItems(binding.recyclerFavorites);
        });
        binding.btnExplore.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
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

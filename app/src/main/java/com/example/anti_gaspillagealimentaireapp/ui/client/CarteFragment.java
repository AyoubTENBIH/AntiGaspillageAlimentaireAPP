package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentCarteBinding;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class CarteFragment extends Fragment {

    private FragmentCarteBinding binding;
    private CarteViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCarteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        binding.mapView.setMultiTouchControls(true);
        binding.mapView.getController().setZoom(14.0);
        binding.mapView.getController().setCenter(new GeoPoint(41.75, -88.15));

        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), binding.mapView);
        locationOverlay.enableMyLocation();
        binding.mapView.getOverlays().add(locationOverlay);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(CarteViewModel.class);
        viewModel.load();
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            List<CommerceWithPaniers> commerces = state.commerces;
            binding.tvCarteEmpty.setVisibility(commerces.isEmpty() ? View.VISIBLE : View.GONE);
            if (!commerces.isEmpty()) {
                binding.mapView.getOverlays().removeIf(overlay -> overlay instanceof Marker);
                for (CommerceWithPaniers cwp : commerces) {
                    Marker marker = new Marker(binding.mapView);
                    marker.setPosition(new GeoPoint(cwp.commerce.latitude, cwp.commerce.longitude));
                    marker.setTitle(cwp.commerce.nom);
                    int count = 0;
                    for (com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier p : cwp.paniers)
                        if (p.isAvailable && p.quantiteDisponible > 0) count++;
                    marker.setSnippet(count + " panier(s) dispo");
                    marker.setOnMarkerClickListener((m, mv) -> {
                        Bundle args = new Bundle();
                        args.putLong("commerceId", cwp.commerce.id);
                        NavHostFragment.findNavController(CarteFragment.this).navigate(R.id.commerceDetailFragment, args);
                        return true;
                    });
                    binding.mapView.getOverlays().add(marker);
                }
                binding.mapView.invalidate();
                CommerceWithPaniers first = commerces.get(0);
                binding.mapView.getController().setCenter(new GeoPoint(first.commerce.latitude, first.commerce.longitude));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

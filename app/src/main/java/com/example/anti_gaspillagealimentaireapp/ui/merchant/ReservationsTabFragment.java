package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationMerchantDetails;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentReservationsTabBinding;

public class ReservationsTabFragment extends Fragment {

    public static final String ARG_STATUT = "statut";

    private FragmentReservationsTabBinding binding;
    private ReservationsMerchantViewModel viewModel;

    public static ReservationsTabFragment newInstance(ReservationStatut statut) {
        ReservationsTabFragment f = new ReservationsTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATUT, statut);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReservationsTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReservationStatut statut = (ReservationStatut) (getArguments() != null ? getArguments().getSerializable(ARG_STATUT) : ReservationStatut.EN_ATTENTE);
        viewModel = new ViewModelProvider(requireParentFragment(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ReservationsMerchantViewModel.class);

        binding.rvReservationsTab.setLayoutManager(new LinearLayoutManager(requireContext()));
        ReservationMerchantAdapter adapter = new ReservationMerchantAdapter(
            statut == ReservationStatut.EN_ATTENTE,
            item -> viewModel.confirmerReservation(item.id),
            item -> viewModel.annulerReservation(item.id)
        );
        binding.rvReservationsTab.setAdapter(adapter);

        androidx.lifecycle.LiveData<java.util.List<ReservationMerchantDetails>> liveData;
        if (statut == ReservationStatut.EN_ATTENTE) liveData = viewModel.getReservationsEnAttente();
        else if (statut == ReservationStatut.CONFIRMEE) liveData = viewModel.getReservationsConfirmees();
        else liveData = viewModel.getReservationsAnnulees();

        liveData.observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

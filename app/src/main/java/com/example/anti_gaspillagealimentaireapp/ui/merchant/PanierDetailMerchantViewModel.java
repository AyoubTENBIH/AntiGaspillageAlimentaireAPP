package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStats;
import com.example.anti_gaspillagealimentaireapp.data.repository.PanierRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.ReservationRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PanierDetailMerchantViewModel extends ViewModel {

    private final long panierId;
    private final PanierRepository panierRepository;
    private final ReservationRepository reservationRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PanierDetailMerchantViewModel(long panierId, PanierRepository panierRepository, ReservationRepository reservationRepository) {
        this.panierId = panierId;
        this.panierRepository = panierRepository;
        this.reservationRepository = reservationRepository;
    }

    public LiveData<Panier> getPanier() {
        return panierRepository.getByIdLiveData(panierId);
    }

    public LiveData<ReservationStats> getStatsReservations() {
        return reservationRepository.getStatsByPanier(panierId);
    }

    public void toggleDisponibilite(final boolean isAvailable) {
        executor.execute(new Runnable() { @Override public void run() { panierRepository.updateDisponibilite(panierId, isAvailable); } });
    }

    public void deletePanier() {
        executor.execute(new Runnable() { @Override public void run() { panierRepository.deleteById(panierId); } });
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final long panierId;
        private final SaveAtApplication application;

        public Factory(long panierId, SaveAtApplication application) {
            this.panierId = panierId;
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            PanierRepository panierRepo = new PanierRepository(application.getDatabase().panierDao());
            ReservationRepository reservationRepo = new ReservationRepository(application.getDatabase());
            return (T) new PanierDetailMerchantViewModel(panierId, panierRepo, reservationRepo);
        }
    }
}

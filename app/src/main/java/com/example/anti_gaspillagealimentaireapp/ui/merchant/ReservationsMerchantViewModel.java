package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationMerchantDetails;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.repository.ReservationRepository;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationsMerchantViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ReservationRepository reservationRepo = new ReservationRepository(app.getDatabase());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Long> commercantId = new MutableLiveData<>();

    public ReservationsMerchantViewModel(Application application) {
        super(application);
        SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        commercantId.setValue(user != null ? user.id : -1L);
    }

    public LiveData<List<ReservationMerchantDetails>> getReservationsEnAttente() {
        return Transformations.switchMap(commercantId, id -> id != null && id > 0
            ? reservationRepo.getReservationsByCommercantAndStatut(id, ReservationStatut.EN_ATTENTE)
            : new MutableLiveData<List<ReservationMerchantDetails>>(Collections.<ReservationMerchantDetails>emptyList()));
    }

    public LiveData<List<ReservationMerchantDetails>> getReservationsConfirmees() {
        return Transformations.switchMap(commercantId, id -> id != null && id > 0
            ? reservationRepo.getReservationsByCommercantAndStatut(id, ReservationStatut.CONFIRMEE)
            : new MutableLiveData<List<ReservationMerchantDetails>>(Collections.<ReservationMerchantDetails>emptyList()));
    }

    public LiveData<List<ReservationMerchantDetails>> getReservationsAnnulees() {
        return Transformations.switchMap(commercantId, id -> id != null && id > 0
            ? reservationRepo.getReservationsByCommercantAndStatut(id, ReservationStatut.ANNULEE)
            : new MutableLiveData<List<ReservationMerchantDetails>>(Collections.<ReservationMerchantDetails>emptyList()));
    }

    public void confirmerReservation(final long reservationId) {
        executor.execute(new Runnable() { @Override public void run() { reservationRepo.updateStatut(reservationId, ReservationStatut.CONFIRMEE); } });
    }

    public void annulerReservation(final long reservationId) {
        executor.execute(new Runnable() { @Override public void run() { reservationRepo.updateStatut(reservationId, ReservationStatut.ANNULEE); } });
    }
}

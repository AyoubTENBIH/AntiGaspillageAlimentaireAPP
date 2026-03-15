package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationWithDetails;
import com.example.anti_gaspillagealimentaireapp.data.repository.ReservationRepository;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final ReservationRepository reservationRepo = new ReservationRepository(app.getDatabase());
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final MutableLiveData<HistoryUiState> uiState = new MutableLiveData<>(new HistoryUiState());

    public HistoryViewModel(Application application) {
        super(application);
    }

    public LiveData<HistoryUiState> getUiState() { return uiState; }

    public void load() {
        final SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        if (user == null) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<ReservationWithDetails> all = reservationRepo.getReservationsByClientWithDetails(user.id);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        HistoryUiState cur = uiState.getValue();
                        uiState.setValue(new HistoryUiState(all, cur != null ? cur.selectedTab : 0));
                    }
                });
            }
        });
    }

    public void setTab(int index) {
        HistoryUiState cur = uiState.getValue();
        if (cur != null) uiState.setValue(new HistoryUiState(cur.reservations, index));
    }

    public void cancelReservation(final long reservationId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                reservationRepo.updateStatut(reservationId, ReservationStatut.ANNULEE);
                mainHandler.post(new Runnable() { @Override public void run() { load(); } });
            }
        });
    }
}

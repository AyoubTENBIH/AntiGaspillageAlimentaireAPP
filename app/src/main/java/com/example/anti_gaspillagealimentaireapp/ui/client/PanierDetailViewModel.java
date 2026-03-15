package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;
import com.example.anti_gaspillagealimentaireapp.data.repository.CommerceRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.PanierRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.ReservationRepository;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PanierDetailViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final PanierRepository panierRepo = new PanierRepository(app.getDatabase().panierDao());
    private final ReservationRepository reservationRepo = new ReservationRepository(app.getDatabase());
    private final CommerceRepository commerceRepo = new CommerceRepository(app.getDatabase().commerceDao());
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final MutableLiveData<PanierDetailUiState> uiState = new MutableLiveData<>(new PanierDetailUiState());

    public PanierDetailViewModel(Application application) {
        super(application);
    }

    public LiveData<PanierDetailUiState> getUiState() { return uiState; }

    public void load(final long panierId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Panier panier = panierRepo.getById(panierId);
                if (panier == null) return;
                String commerceName = commerceRepo.getById(panier.commerceId) != null ? commerceRepo.getById(panier.commerceId).nom : "";
                final Panier p = panier;
                final String name = commerceName;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiState.setValue(new PanierDetailUiState(p, name, null, false));
                    }
                });
            }
        });
    }

    public void reserve() {
        final PanierDetailUiState current = uiState.getValue();
        if (current == null || current.panier == null) return;
        final SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        if (user == null || user.role != UserRole.CLIENT) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long id = reservationRepo.reserver(user.id, current.panier.id);
                final boolean success = id >= 0;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiState.setValue(new PanierDetailUiState(current.panier, current.commerceName, success, !success));
                    }
                });
            }
        });
    }

    public void consumeReserveResult() {
        PanierDetailUiState current = uiState.getValue();
        if (current != null)
            uiState.setValue(new PanierDetailUiState(current.panier, current.commerceName, null, false));
    }
}

package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.repository.CommerceRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.PanierRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.ReservationRepository;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final CommerceRepository commerceRepo = new CommerceRepository(app.getDatabase().commerceDao());
    private final PanierRepository panierRepo = new PanierRepository(app.getDatabase().panierDao());
    private final ReservationRepository reservationRepo = new ReservationRepository(app.getDatabase());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    private final MutableLiveData<Long> commercantId = new MutableLiveData<>(0L);
    private final MutableLiveData<DashboardUiState> uiState = new MutableLiveData<>(new DashboardUiState());

    public final LiveData<Integer> countPaniersActifs = Transformations.switchMap(commercantId, id -> {
        if (id == null || id <= 0) return new MutableLiveData<>(0);
        return panierRepo.countPaniersActifsLiveData(id);
    });
    public final LiveData<Integer> countReservationsToday = Transformations.switchMap(commercantId, id -> {
        if (id == null || id <= 0) return new MutableLiveData<>(0);
        return reservationRepo.countReservationsTodayLiveData(id);
    });
    public final LiveData<Double> revenusWeek = Transformations.switchMap(commercantId, id -> {
        if (id == null || id <= 0) return new MutableLiveData<>(0.0);
        return reservationRepo.sumRevenusThisWeekLiveData(id);
    });

    public DashboardViewModel(Application application) {
        super(application);
    }

    public LiveData<DashboardUiState> getUiState() { return uiState; }

    public void load() {
        final SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        if (user == null) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Commerce> commerces = commerceRepo.getByCommercant(user.id);
                long firstCommerceId = -1;
                if (!commerces.isEmpty()) {
                    firstCommerceId = commerces.get(0).id;
                    sessionManager.setCommerceId(firstCommerceId);
                }
                final long fid = firstCommerceId;
                commercantId.postValue(user.id);
                List<Panier> allPaniers = new ArrayList<>();
                for (Commerce c : commerces) {
                    allPaniers.addAll(panierRepo.getByCommerce(c.id));
                }
                List<Panier> available = new ArrayList<>();
                for (Panier p : allPaniers) if (p.isAvailable) available.add(p);
                int paniersActifs = panierRepo.countPaniersActifs(user.id);
                int reservationsToday = reservationRepo.countReservationsToday(user.id);
                double revenueWeek = reservationRepo.sumRevenusThisWeek(user.id);
                List<com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationCountByDay> stats7Days = reservationRepo.getReservationsCountByDayLast7Days(user.id);
                final DashboardUiState state = new DashboardUiState(user.name, paniersActifs, reservationsToday, revenueWeek, available, stats7Days);
                mainHandler.post(new Runnable() { @Override public void run() { uiState.setValue(state); } });
            }
        });
    }
}

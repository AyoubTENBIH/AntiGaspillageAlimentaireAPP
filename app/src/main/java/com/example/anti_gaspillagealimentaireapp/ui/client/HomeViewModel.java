package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.repository.CommerceRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.FavoriteRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.PanierRepository;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final CommerceRepository commerceRepo = new CommerceRepository(app.getDatabase().commerceDao());
    private final PanierRepository panierRepo = new PanierRepository(app.getDatabase().panierDao());
    private final FavoriteRepository favoriteRepo = new FavoriteRepository(app.getDatabase().favoriteDao());
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    private final MutableLiveData<HomeUiState> uiState = new MutableLiveData<>(new HomeUiState());

    public HomeViewModel(Application application) {
        super(application);
    }

    public LiveData<HomeUiState> getUiState() { return uiState; }

    public void load() {
        mainHandler.post(new Runnable() { @Override public void run() {
            uiState.setValue(new HomeUiState(null, null, null, null, true));
        }});
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final java.util.List<com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers> listCommerces = commerceRepo.getAllCommercesWithPaniers();
                final java.util.List<com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier> listPaniers = panierRepo.getAllPaniersDisponibles();
                final SessionManager.CurrentUser currentUser = sessionManager.getCurrentUser();
                Set<Long> favorites = new HashSet<>();
                if (currentUser != null) {
                    for (Long id : favoriteRepo.getFavoritePanierIds(currentUser.id)) favorites.add(id);
                }
                final Set<Long> fav = favorites;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiState.setValue(new HomeUiState(listCommerces, listPaniers, fav, "", false));
                    }
                });
            }
        });
    }

    public void setSearch(String query) {
        HomeUiState current = uiState.getValue();
        if (current == null) return;
        uiState.setValue(current.copyWithSearch(query));
    }

    public void toggleFavorite(final long panierId) {
        final SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        if (user == null) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteRepo.toggleFavorite(user.id, panierId);
                final Set<Long> updated = new HashSet<>(favoriteRepo.getFavoritePanierIds(user.id));
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        HomeUiState current = uiState.getValue();
                        if (current != null) uiState.setValue(current.copyWithFavorites(updated));
                    }
                });
            }
        });
    }
}

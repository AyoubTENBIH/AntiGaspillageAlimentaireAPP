package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.repository.FavoriteRepository;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavorisViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final FavoriteRepository favoriteRepo = new FavoriteRepository(app.getDatabase().favoriteDao());
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final MutableLiveData<FavorisUiState> uiState = new MutableLiveData<>(new FavorisUiState());

    public FavorisViewModel(Application application) {
        super(application);
    }

    public LiveData<FavorisUiState> getUiState() { return uiState; }

    public void load() {
        final SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        if (user == null) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Panier> paniers = favoriteRepo.getFavoritePaniers(user.id);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiState.setValue(new FavorisUiState(paniers));
                    }
                });
            }
        });
    }

    public void toggleFavorite(final long panierId) {
        final SessionManager.CurrentUser user = sessionManager.getCurrentUser();
        if (user == null) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                favoriteRepo.toggleFavorite(user.id, panierId);
                final List<Panier> paniers = favoriteRepo.getFavoritePaniers(user.id);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiState.setValue(new FavorisUiState(paniers));
                    }
                });
            }
        });
    }
}

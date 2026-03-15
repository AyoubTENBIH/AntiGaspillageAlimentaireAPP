package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;
import com.example.anti_gaspillagealimentaireapp.data.repository.CommerceRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommerceDetailViewModel extends AndroidViewModel {

    private final CommerceRepository commerceRepo = new CommerceRepository(((SaveAtApplication) getApplication()).getDatabase().commerceDao());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final MutableLiveData<CommerceDetailUiState> uiState = new MutableLiveData<>(new CommerceDetailUiState());

    public CommerceDetailViewModel(Application application) {
        super(application);
    }

    public LiveData<CommerceDetailUiState> getUiState() { return uiState; }

    public void load(final long commerceId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final CommerceWithPaniers cwp = commerceRepo.getCommerceWithPaniers(commerceId);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiState.setValue(new CommerceDetailUiState(cwp));
                    }
                });
            }
        });
    }
}

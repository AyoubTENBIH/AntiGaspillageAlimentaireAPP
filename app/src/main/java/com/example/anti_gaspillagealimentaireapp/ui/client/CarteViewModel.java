package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.dao.CommerceWithPaniers;
import com.example.anti_gaspillagealimentaireapp.data.repository.CommerceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarteViewModel extends AndroidViewModel {

    private final CommerceRepository commerceRepo = new CommerceRepository(((SaveAtApplication) getApplication()).getDatabase().commerceDao());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final MutableLiveData<CarteUiState> uiState = new MutableLiveData<>(new CarteUiState());

    public CarteViewModel(Application application) {
        super(application);
    }

    public LiveData<CarteUiState> getUiState() { return uiState; }

    public void load() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<CommerceWithPaniers> all = commerceRepo.getAllCommercesWithPaniers();
                List<CommerceWithPaniers> withPaniersDispo = new ArrayList<>();
                for (CommerceWithPaniers cwp : all) {
                    if (cwp.commerce.latitude != 0.0 && cwp.commerce.longitude != 0.0) {
                        for (com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier p : cwp.paniers) {
                            if (p.isAvailable && p.quantiteDisponible > 0) {
                                withPaniersDispo.add(cwp);
                                break;
                            }
                        }
                    }
                }
                final List<CommerceWithPaniers> result = withPaniersDispo;
                mainHandler.post(new Runnable() { @Override public void run() {
                    uiState.setValue(new CarteUiState(result));
                }});
            }
        });
    }
}

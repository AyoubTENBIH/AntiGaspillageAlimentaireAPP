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
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MerchantPaniersViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final CommerceRepository commerceRepo = new CommerceRepository(app.getDatabase().commerceDao());
    private final PanierRepository panierRepo = new PanierRepository(app.getDatabase().panierDao());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final MutableLiveData<Long> commerceId = new MutableLiveData<>(sessionManager.getCommerceId());

    public MerchantPaniersViewModel(Application application) {
        super(application);
        if (commerceId.getValue() != null && commerceId.getValue() <= 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SessionManager.CurrentUser user = sessionManager.getCurrentUser();
                    if (user == null) return;
                    List<Commerce> commerces = commerceRepo.getByCommercant(user.id);
                    if (!commerces.isEmpty()) {
                        Commerce first = commerces.get(0);
                        sessionManager.setCommerceId(first.id);
                        mainHandler.post(new Runnable() { @Override public void run() { commerceId.setValue(first.id); } });
                    }
                }
            });
        }
    }

    public long getCommerceIdValue() {
        long cid = sessionManager.getCommerceId();
        if (cid > 0) return cid;
        Long v = commerceId.getValue();
        return v != null ? v : 0L;
    }

    public LiveData<List<Panier>> getPaniers() {
        return Transformations.switchMap(commerceId, id -> {
            if (id == null || id <= 0) return new MutableLiveData<List<Panier>>();
            return panierRepo.getPaniersByCommerceLiveData(id);
        });
    }

    public void addPanier(final Panier panier) {
        executor.execute(new Runnable() { @Override public void run() { panierRepo.insert(panier); } });
    }

    public void updatePanier(final Panier panier) {
        executor.execute(new Runnable() { @Override public void run() { panierRepo.update(panier); } });
    }

    public void deletePanier(final Panier panier) {
        executor.execute(new Runnable() { @Override public void run() { panierRepo.deleteById(panier.id); } });
    }
}

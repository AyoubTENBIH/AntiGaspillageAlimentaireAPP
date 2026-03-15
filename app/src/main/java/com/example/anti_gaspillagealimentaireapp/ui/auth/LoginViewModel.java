package com.example.anti_gaspillagealimentaireapp.ui.auth;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.User;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;
import com.example.anti_gaspillagealimentaireapp.data.repository.UserRepository;
import com.example.anti_gaspillagealimentaireapp.utils.HashUtils;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final UserRepository userRepo = new UserRepository(app.getDatabase().userDao());
    private final SessionManager sessionManager = new SessionManager(getApplication());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<LoginNavigation> navigation = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
    }

    public LiveData<String> getError() { return error; }
    public LiveData<LoginNavigation> getNavigation() { return navigation; }

    public void login(final String email, final String password) {
        error.postValue(null);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final String hash = HashUtils.md5(password);
                final User user = userRepo.login(email.trim(), hash);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (user == null) {
                            error.setValue(app.getString(R.string.login_error));
                            return;
                        }
                        sessionManager.saveSession(user.id, user.role, user.nom, user.email);
                        if (user.role == UserRole.CLIENT) {
                            navigation.setValue(new LoginNavigation.GoToClient(user.role));
                        } else {
                            navigation.setValue(new LoginNavigation.GoToMerchant(user.role));
                        }
                    }
                });
            }
        });
    }

    public void goToRegister() {
        navigation.setValue(new LoginNavigation.GoToRegister());
    }

    public void consumeNavigation() {
        navigation.setValue(null);
    }

    public void clearError() {
        error.setValue(null);
    }
}

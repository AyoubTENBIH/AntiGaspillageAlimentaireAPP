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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterViewModel extends AndroidViewModel {

    private final SaveAtApplication app = (SaveAtApplication) getApplication();
    private final UserRepository userRepo = new UserRepository(app.getDatabase().userDao());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    public RegisterViewModel(Application application) {
        super(application);
    }

    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }

    public void register(final String nom, final String email, final String password,
                         final String confirmPassword, final boolean isMerchant) {
        error.postValue(null);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final String err;
                if (!password.equals(confirmPassword)) {
                    err = app.getString(R.string.register_error_passwords);
                    mainHandler.post(new Runnable() { @Override public void run() { error.setValue(err); } });
                    return;
                }
                if (userRepo.getByEmail(email.trim()) != null) {
                    err = app.getString(R.string.register_error_email_exists);
                    mainHandler.post(new Runnable() { @Override public void run() { error.setValue(err); } });
                    return;
                }
                User user = new User(nom.trim(), email.trim(), HashUtils.md5(password),
                    isMerchant ? UserRole.COMMERCANT : UserRole.CLIENT, System.currentTimeMillis());
                userRepo.insert(user);
                mainHandler.post(new Runnable() { @Override public void run() { registerSuccess.setValue(true); } });
            }
        });
    }

    public void consumeSuccess() {
        registerSuccess.setValue(false);
    }

    public void clearError() {
        error.setValue(null);
    }
}

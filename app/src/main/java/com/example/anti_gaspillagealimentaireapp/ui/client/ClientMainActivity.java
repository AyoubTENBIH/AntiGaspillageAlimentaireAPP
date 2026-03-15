package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;
import com.example.anti_gaspillagealimentaireapp.databinding.ActivityClientMainBinding;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;
import com.example.anti_gaspillagealimentaireapp.worker.ReservationReminderWorker;

import java.util.concurrent.TimeUnit;

public class ClientMainActivity extends AppCompatActivity {

    private ActivityClientMainBinding binding;
    private final ActivityResultLauncher<String> requestNotificationPermission =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityClientMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            v.setPadding(
                0,
                insets.getInsets(WindowInsetsCompat.Type.statusBars()).top,
                0,
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            );
            return insets;
        });

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_client);
        if (navHost != null) {
            NavigationUI.setupWithNavController(binding.bottomNav, navHost.getNavController());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.getCurrentUser() != null && sessionManager.getCurrentUser().role == UserRole.CLIENT) {
            OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(ReservationReminderWorker.class)
                .setInitialDelay(15, TimeUnit.SECONDS)
                .build();
            WorkManager.getInstance(this).enqueue(work);
        }
    }
}

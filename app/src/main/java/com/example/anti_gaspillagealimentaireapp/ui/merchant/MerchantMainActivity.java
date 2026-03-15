package com.example.anti_gaspillagealimentaireapp.ui.merchant;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.databinding.ActivityMerchantMainBinding;

public class MerchantMainActivity extends AppCompatActivity {

    private ActivityMerchantMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityMerchantMainBinding.inflate(getLayoutInflater());
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

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_merchant);
        if (navHostFragment == null) return;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.merchantBottomNav, navController);
        binding.merchantBottomNav.setOnItemSelectedListener(item -> {
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });
        binding.merchantBottomNav.setOnItemReselectedListener(item -> {});

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.panierDetailMerchantFragment) {
                binding.merchantBottomNav.setVisibility(View.GONE);
            } else {
                binding.merchantBottomNav.setVisibility(View.VISIBLE);
            }
        });
    }
}

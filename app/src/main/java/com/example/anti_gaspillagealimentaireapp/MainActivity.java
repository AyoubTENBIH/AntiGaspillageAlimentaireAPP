package com.example.anti_gaspillagealimentaireapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.anti_gaspillagealimentaireapp.databinding.ActivityMainBinding;
import com.example.anti_gaspillagealimentaireapp.ui.client.ClientMainActivity;
import com.example.anti_gaspillagealimentaireapp.ui.merchant.MerchantMainActivity;

/**
 * Activity principale : Splash → Onboarding / Login → Client ou Merchant selon le rôle.
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "SaveAtDebug";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "[2/2] MainActivity.onCreate() démarré");
        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d(DEBUG_TAG, "[2/2] MainActivity.onCreate() setContentView OK");
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "[2/2] MainActivity.onCreate() ERREUR", e);
            throw e;
        }
    }

    public void navigateToClientHome() {
        Log.d(DEBUG_TAG, "[NAV] -> ClientMainActivity");
        Intent intent = new Intent(this, ClientMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void navigateToMerchantHome() {
        Log.d(DEBUG_TAG, "[NAV] -> MerchantMainActivity");
        Intent intent = new Intent(this, MerchantMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

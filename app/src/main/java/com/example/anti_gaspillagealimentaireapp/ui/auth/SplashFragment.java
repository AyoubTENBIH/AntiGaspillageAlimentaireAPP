package com.example.anti_gaspillagealimentaireapp.ui.auth;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.anti_gaspillagealimentaireapp.MainActivity;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentSplashBinding;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;

public class SplashFragment extends Fragment {

    private static final String DEBUG_TAG = "SaveAtDebug";
    private FragmentSplashBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(DEBUG_TAG, "[3/3] SplashFragment.onViewCreated()");
        binding.logoText.setAlpha(0f);
        binding.logoText.setScaleX(0.5f);
        binding.logoText.setScaleY(0.5f);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(binding.logoText, View.ALPHA, 0f, 1f);
        fadeIn.setDuration(400);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.logoText, View.SCALE_X, 0.5f, 1.2f);
        scaleX.setDuration(600);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.logoText, View.SCALE_Y, 0.5f, 1.2f);
        scaleY.setDuration(600);
        ObjectAnimator scaleBackX = ObjectAnimator.ofFloat(binding.logoText, View.SCALE_X, 1.2f, 1f);
        scaleBackX.setDuration(200);
        ObjectAnimator scaleBackY = ObjectAnimator.ofFloat(binding.logoText, View.SCALE_Y, 1.2f, 1f);
        scaleBackY.setDuration(200);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeIn, scaleX, scaleY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet setBack = new AnimatorSet();
        setBack.playTogether(scaleBackX, scaleBackY);
        set.start();
        set.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {}
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                setBack.start();
            }
            @Override
            public void onAnimationCancel(android.animation.Animator animation) {}
            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {}
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(DEBUG_TAG, "[3/3] Splash Handler exécuté (800ms)");
                if (!isAdded()) {
                    Log.w(DEBUG_TAG, "[3/3] Splash: fragment non attaché, skip");
                    return;
                }
                try {
                    Log.d(DEBUG_TAG, "[3/3] Splash: lecture session...");
                    SessionManager sessionManager = new SessionManager(requireContext());
                    if (!(requireActivity() instanceof MainActivity)) return;
                    MainActivity activity = (MainActivity) requireActivity();
                    if (sessionManager.isLoggedIn() && sessionManager.getCurrentUser() != null) {
                        Log.d(DEBUG_TAG, "[3/3] Splash: utilisateur connecté -> Home");
                        UserRole role = sessionManager.getUserRole();
                        if (role == UserRole.CLIENT) {
                            activity.navigateToClientHome();
                        } else if (role == UserRole.COMMERCANT) {
                            activity.navigateToMerchantHome();
                        } else {
                            goToOnboardingOrLogin();
                        }
                    } else {
                        Log.d(DEBUG_TAG, "[3/3] Splash: pas connecté -> onboarding ou login");
                        goToOnboardingOrLogin();
                    }
                } catch (Exception e) {
                    Log.e(DEBUG_TAG, "[3/3] Splash Handler ERREUR", e);
                    if (isAdded()) goToOnboardingOrLogin();
                }
            }
        }, 800);
    }

    private void goToOnboardingOrLogin() {
        if (!isAdded()) return;
        try {
            boolean done = requireContext().getSharedPreferences("saveat_prefs", android.content.Context.MODE_PRIVATE)
                .getBoolean("onboarding_done", false);
            Log.d(DEBUG_TAG, "[3/3] goToOnboardingOrLogin: onboarding_done=" + done);
            NavController nav = NavHostFragment.findNavController(this);
            if (done) {
                Log.d(DEBUG_TAG, "[3/3] Navigation -> loginFragment");
                nav.navigate(R.id.loginFragment);
            } else {
                Log.d(DEBUG_TAG, "[3/3] Navigation -> onboardingFragment");
                nav.navigate(R.id.onboardingFragment);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "[3/3] goToOnboardingOrLogin ERREUR", e);
            try {
                NavHostFragment.findNavController(this).navigate(R.id.onboardingFragment);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

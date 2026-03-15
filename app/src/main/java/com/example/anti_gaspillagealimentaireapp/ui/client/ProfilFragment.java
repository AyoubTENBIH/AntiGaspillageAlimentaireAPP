package com.example.anti_gaspillagealimentaireapp.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.anti_gaspillagealimentaireapp.MainActivity;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.SaveAtApplication;
import com.example.anti_gaspillagealimentaireapp.data.repository.FavoriteRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.ReservationRepository;
import com.example.anti_gaspillagealimentaireapp.data.repository.UserRepository;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentProfilPlaceholderBinding;
import com.example.anti_gaspillagealimentaireapp.utils.HashUtils;
import com.example.anti_gaspillagealimentaireapp.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilFragment extends Fragment {

    private FragmentProfilPlaceholderBinding binding;
    private UserRepository userRepository;
    private ReservationRepository reservationRepository;
    private FavoriteRepository favoriteRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            binding = FragmentProfilPlaceholderBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            FrameLayout fallback = new FrameLayout(requireContext());
            fallback.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
            TextView tv = new TextView(requireContext());
            tv.setText(getString(R.string.profile_title));
            tv.setPadding(48, 48, 48, 48);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_body));
            fallback.addView(tv);
            return fallback;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;
        SaveAtApplication app = (SaveAtApplication) requireContext().getApplicationContext();
        userRepository = new UserRepository(app.getDatabase().userDao());
        reservationRepository = new ReservationRepository(app.getDatabase());
        favoriteRepository = new FavoriteRepository(app.getDatabase().favoriteDao());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), insets.getInsets(WindowInsetsCompat.Type.statusBars()).top, v.getPaddingRight(), insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom);
            return insets;
        });

        SessionManager.CurrentUser session = new SessionManager(requireContext()).getCurrentUser();
        binding.textUserName.setText(session != null ? session.name : getString(R.string.unknown_user));
        binding.textUserRole.setText(session != null && session.role != null ? session.role.name() : "");
        String emailToShow = session != null && session.email != null && !session.email.isEmpty() ? session.email : null;
        if ((emailToShow == null || emailToShow.isEmpty()) && session != null && session.id > 0) {
            executor.execute(() -> {
                com.example.anti_gaspillagealimentaireapp.data.local.entities.User user = userRepository.getById(session.id);
                String email = user != null && user.email != null && !user.email.isEmpty() ? user.email : "—";
                mainHandler.post(() -> binding.textUserEmail.setText(email));
            });
        } else {
            binding.textUserEmail.setText(emailToShow != null ? emailToShow : "—");
        }
        binding.textStatReservations.setText("0");
        binding.textStatFavorites.setText("0");
        binding.textStatSavings.setText("0 kg");
        if (session != null && session.id > 0) {
            executor.execute(() -> {
                int paniersRecuperes = reservationRepository.countReservationsConfirmeesByClient(session.id);
                int favorisCount = favoriteRepository.getFavoritePanierIds(session.id).size();
                mainHandler.post(() -> {
                    binding.textStatReservations.setText(String.valueOf(paniersRecuperes));
                    binding.textStatFavorites.setText(String.valueOf(favorisCount));
                    binding.textStatSavings.setText(paniersRecuperes + " kg");
                });
            });
        }

        binding.rowPhoto.setOnClickListener(v -> Toast.makeText(requireContext(), getString(R.string.change_photo), Toast.LENGTH_SHORT).show());
        binding.rowChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        binding.buttonLogout.setOnClickListener(v -> {
            new SessionManager(requireContext()).clearSession();
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
        binding.rowNotifications.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.notificationsFragment));
    }

    private void showChangePasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        TextInputLayout currentPasswordLayout = dialogView.findViewById(R.id.current_password_layout);
        TextInputEditText currentPassword = dialogView.findViewById(R.id.current_password);
        TextInputLayout newPasswordLayout = dialogView.findViewById(R.id.new_password_layout);
        TextInputEditText newPassword = dialogView.findViewById(R.id.new_password);
        TextInputLayout confirmPasswordLayout = dialogView.findViewById(R.id.confirm_password_layout);
        TextInputEditText confirmPassword = dialogView.findViewById(R.id.confirm_password);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_change_password))
            .setView(dialogView)
            .setPositiveButton(getString(android.R.string.ok), null)
            .setNegativeButton(android.R.string.cancel, null)
            .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (currentPasswordLayout != null) currentPasswordLayout.setError(null);
                if (newPasswordLayout != null) newPasswordLayout.setError(null);
                if (confirmPasswordLayout != null) confirmPasswordLayout.setError(null);
                String current = currentPassword != null && currentPassword.getText() != null ? currentPassword.getText().toString().trim() : "";
                String newP = newPassword != null && newPassword.getText() != null ? newPassword.getText().toString().trim() : "";
                String confirm = confirmPassword != null && confirmPassword.getText() != null ? confirmPassword.getText().toString().trim() : "";
                if (current.isEmpty()) {
                    if (currentPasswordLayout != null) currentPasswordLayout.setError(getString(R.string.password_error_wrong));
                    return;
                }
                if (newP.length() < 4) {
                    if (newPasswordLayout != null) newPasswordLayout.setError(getString(R.string.password_error_short));
                    return;
                }
                if (!newP.equals(confirm)) {
                    if (confirmPasswordLayout != null) confirmPasswordLayout.setError(getString(R.string.password_error_match));
                    return;
                }
                dialog.dismiss();
                runChangePassword(current, newP);
            });
        });
        dialog.show();
    }

    private void runChangePassword(String current, String newPassword) {
        SessionManager.CurrentUser session = new SessionManager(requireContext()).getCurrentUser();
        if (session == null) return;
        executor.execute(() -> {
            com.example.anti_gaspillagealimentaireapp.data.local.entities.User user = userRepository.login(session.email, HashUtils.md5(current));
            boolean success = false;
            if (user != null) {
                userRepository.updatePassword(session.id, HashUtils.md5(newPassword));
                success = true;
            }
            boolean s = success;
            mainHandler.post(() -> {
                if (s) Toast.makeText(requireContext(), getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
                else Toast.makeText(requireContext(), getString(R.string.password_error_wrong), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.anti_gaspillagealimentaireapp.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.anti_gaspillagealimentaireapp.MainActivity;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(LoginViewModel.class);
        binding.toggleRole.check(R.id.btnClient);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.clearError();
                String email = binding.email.getText() != null ? binding.email.getText().toString() : "";
                String password = binding.password.getText() != null ? binding.password.getText().toString() : "";
                viewModel.login(email, password);
            }
        });
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.goToRegister();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String msg) {
                binding.errorText.setText(msg);
                binding.errorText.setVisibility(msg == null || msg.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
        viewModel.getNavigation().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<LoginNavigation>() {
            @Override
            public void onChanged(LoginNavigation nav) {
                if (nav == null) return;
                viewModel.consumeNavigation();
                if (!(requireActivity() instanceof MainActivity)) return;
                MainActivity activity = (MainActivity) requireActivity();
                if (nav instanceof LoginNavigation.GoToRegister) {
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.registerFragment);
                } else if (nav instanceof LoginNavigation.GoToClient) {
                    activity.navigateToClientHome();
                } else if (nav instanceof LoginNavigation.GoToMerchant) {
                    activity.navigateToMerchantHome();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

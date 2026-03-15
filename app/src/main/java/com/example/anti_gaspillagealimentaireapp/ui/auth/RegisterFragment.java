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
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(RegisterViewModel.class);
        binding.toggleRole.check(R.id.btnClient);

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.clearError();
                String name = binding.name.getText() != null ? binding.name.getText().toString() : "";
                String email = binding.email.getText() != null ? binding.email.getText().toString() : "";
                String password = binding.password.getText() != null ? binding.password.getText().toString() : "";
                String confirmPassword = binding.confirmPassword.getText() != null ? binding.confirmPassword.getText().toString() : "";
                boolean isMerchant = binding.toggleRole.getCheckedButtonId() == R.id.btnMerchant;
                viewModel.register(name, email, password, confirmPassword, isMerchant);
            }
        });
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RegisterFragment.this).navigateUp();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String msg) {
                binding.errorText.setText(msg);
                binding.errorText.setVisibility(msg == null || msg.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
        viewModel.getRegisterSuccess().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (Boolean.TRUE.equals(success)) {
                    viewModel.consumeSuccess();
                    NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.loginFragment);
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

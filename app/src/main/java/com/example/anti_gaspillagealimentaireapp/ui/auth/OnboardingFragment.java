package com.example.anti_gaspillagealimentaireapp.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.anti_gaspillagealimentaireapp.R;
import com.example.anti_gaspillagealimentaireapp.databinding.FragmentOnboardingBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingFragment extends Fragment {

    private static final String DEBUG_TAG = "SaveAtDebug";
    private FragmentOnboardingBinding binding;

    private static final int[][] SLIDES = new int[][] {
        { R.string.onboarding_slide1_title, R.string.onboarding_slide1_desc },
        { R.string.onboarding_slide2_title, R.string.onboarding_slide2_desc },
        { R.string.onboarding_slide3_title, R.string.onboarding_slide3_desc }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(DEBUG_TAG, "[4/4] OnboardingFragment.onViewCreated()");
        List<int[]> slidesList = new ArrayList<>();
        for (int[] s : SLIDES) slidesList.add(s);
        binding.viewPager.setAdapter(new OnboardingAdapter(slidesList));
        new TabLayoutMediator(binding.dotsIndicator, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(com.google.android.material.tabs.TabLayout.Tab tab, int position) {}
        }).attach();
        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "[4/4] Bouton Commencer cliqué");
                if (!isAdded()) return;
                try {
                    requireContext().getSharedPreferences("saveat_prefs", android.content.Context.MODE_PRIVATE)
                        .edit().putBoolean("onboarding_done", true).apply();
                    Log.d(DEBUG_TAG, "[4/4] Navigation -> loginFragment");
                    NavHostFragment.findNavController(OnboardingFragment.this).navigate(R.id.loginFragment);
                } catch (Exception e) {
                    Log.e(DEBUG_TAG, "[4/4] Onboarding click ERREUR", e);
                    try {
                        NavHostFragment.findNavController(OnboardingFragment.this).navigate(R.id.loginFragment);
                    } catch (Exception ignored) {}
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {
        private final List<int[]> slides;

        OnboardingAdapter(List<int[]> slides) {
            this.slides = slides;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_slide, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int[] s = slides.get(position);
            holder.title.setText(s[0]);
            holder.description.setText(s[1]);
        }

        @Override
        public int getItemCount() {
            return slides.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView title;
            final TextView description;

            ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                description = itemView.findViewById(R.id.description);
            }
        }
    }
}

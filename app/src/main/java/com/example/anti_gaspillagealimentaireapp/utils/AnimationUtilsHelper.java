package com.example.anti_gaspillagealimentaireapp.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.anti_gaspillagealimentaireapp.R;

import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public final class AnimationUtilsHelper {

    private AnimationUtilsHelper() {}

    public static void animateCount(TextView textView, int from, int to) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> textView.setText(String.valueOf(animation.getAnimatedValue())));
        animator.start();
    }

    public static void animateItems(RecyclerView recyclerView) {
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_fall_down));
        recyclerView.scheduleLayoutAnimation();
    }

    public static void popAnimation(View view, final Runnable onEnd) {
        view.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().scaleX(1.15f).scaleY(1.15f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(80).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                if (onEnd != null) onEnd.run();
                            }
                        }).start();
                    }
                }).start();
            }
        }).start();
    }

    public static void toggleFavorite(ImageView imageView, boolean isFavorite) {
        imageView.animate().scaleX(0f).scaleY(0f).setDuration(120).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
                imageView.setImageTintList(ColorStateList.valueOf(
                    isFavorite ? Color.parseColor("#E53935") : ContextCompat.getColor(imageView.getContext(), R.color.text_hint)));
                imageView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        imageView.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    }
                }).start();
            }
        }).start();
    }
}

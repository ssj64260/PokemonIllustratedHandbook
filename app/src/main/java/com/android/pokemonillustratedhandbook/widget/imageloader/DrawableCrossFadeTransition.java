package com.android.pokemonillustratedhandbook.widget.imageloader;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import com.bumptech.glide.request.transition.Transition;

public class DrawableCrossFadeTransition implements Transition<Drawable> {

    private final int duration;
    private final boolean isCrossFadeEnabled;

    @SuppressWarnings("WeakerAccess")
    public DrawableCrossFadeTransition(int duration,
                                       boolean isCrossFadeEnabled) {
        this.duration = duration;
        this.isCrossFadeEnabled = isCrossFadeEnabled;
    }

    @Override
    public boolean transition(Drawable current, ViewAdapter adapter) {
        Drawable previous = adapter.getCurrentDrawable();
        if (previous == null) {
            previous = new ColorDrawable(Color.TRANSPARENT);
        }
        TransitionDrawable transitionDrawable =
                new TransitionDrawable(new Drawable[] { previous, current });
        transitionDrawable.setCrossFadeEnabled(isCrossFadeEnabled);
        transitionDrawable.startTransition(duration);
        adapter.setDrawable(transitionDrawable);
        return true;
    }

}

package com.musiquitaapp.services;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

public class Animations {
    public void clickAnimation(ImageView imageView) {

        ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0.8f);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.8f);

        ObjectAnimator animation3 = ObjectAnimator.ofFloat(imageView, "scaleX", 0.8f, 1f);
        ObjectAnimator animation4 = ObjectAnimator.ofFloat(imageView, "scaleY", 0.8f, 1f);

        int duration = 175;

        animation1.setDuration(duration);
        animation2.setDuration(duration);

        animation3.setStartDelay(duration);
        animation4.setStartDelay(duration);
        animation3.setDuration(duration);
        animation4.setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animation1, animation2, animation3, animation4);
        animatorSet.start();
    }
}

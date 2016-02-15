package com.github.windekirun.imagedismiss;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * ImageDismiss
 * class: ImageDismissActivity
 * Created by WindSekirun on 2016. 1. 1..
 */
public class ImageDismissActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dismiss);

        ImageView swipeImage = (ImageView) findViewById(R.id.swipe_image);
        RelativeLayout swipeParent = (RelativeLayout) findViewById(R.id.view_parent);

        swipeParent.setOnTouchListener(new SwipeImageTouchListener(swipeImage));
    }

    public class SwipeImageTouchListener implements View.OnTouchListener{
        private final View swipeView;

        public SwipeImageTouchListener(View swipeView) {
            this.swipeView = swipeView;
        }

        private boolean tracking = false;
        private float startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Rect hitRect = new Rect();
                    swipeView.getHitRect(hitRect);
                    if (hitRect.contains((int) event.getX(), (int) event.getY())) {
                        tracking = true;
                    }
                    startY = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    tracking = false;
                    animateSwipeView(v.getHeight());
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (tracking) {
                        swipeView.setTranslationY(event.getY() - startY);
                    }
                    return true;
            }
            return false;
        }

        private void animateSwipeView(int parentHeight) {
            int quarterHeight = parentHeight / 4;
            float currentPosition = swipeView.getTranslationY();
            float animateTo = 0.0f;
            if (currentPosition < -quarterHeight) {
                animateTo = -parentHeight;
            } else if (currentPosition > quarterHeight) {
                animateTo = parentHeight;
            }
            ObjectAnimator.ofFloat(swipeView, "translationY", currentPosition, animateTo)
                    .setDuration(200)
                    .start();

            if (currentPosition != 0.0f) {
                if (currentPosition >= 400.0f) {
                    finish();
                } else if (currentPosition <= -400.0f) {
                    finish();
                }
            }
        }
    }
}

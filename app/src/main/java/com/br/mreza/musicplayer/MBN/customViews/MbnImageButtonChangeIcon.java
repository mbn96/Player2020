package com.br.mreza.musicplayer.MBN.customViews;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import androidx.appcompat.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.animation.AnticipateOvershootInterpolator;

import com.br.mreza.musicplayer.R;

public class MbnImageButtonChangeIcon extends MbnImageButton {


    private boolean modeForThis = false;
    private ValueAnimator animator;

    public MbnImageButtonChangeIcon(Context context) {
        super(context);
    }

    public MbnImageButtonChangeIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MbnImageButtonChangeIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setMode(boolean mode) {

//        Log.i("InVIEW0", " " + mode + "  " + modeForThis);

        if (modeForThis != mode) {

//            Log.i("InVIEW1", " " + mode + "  " + modeForThis);

            modeForThis = mode;
//            Log.i("InVIEW3", " " + mode + "  " + modeForThis);

            animator();
        }


//        Log.i("InVIEW4", " " + mode + "  " + modeForThis);


//        if (modeForThis) {
//            setImageResource(R.drawable.ic_music_player_pause_lines);
//        } else {
//            setImageResource(R.drawable.ic_music_player_play);
//        }

    }

    private void animator() {

//        if (getAnimation().isInitialized()) {


//        getAnimation().cancel();
//        getAnimation().reset();

        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(1, 0).setDuration(200);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(1);
        animator.setInterpolator(new AnticipateOvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setAlpha((float) valueAnimator.getAnimatedValue());
                setScaleX((float) valueAnimator.getAnimatedValue());
                setScaleY((float) valueAnimator.getAnimatedValue());

            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

                if (modeForThis) {
                    setImageResource(R.drawable.ic_music_player_pause_lines);
                } else {
                    setImageResource(R.drawable.ic_music_player_play);
                }

            }
        });

//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//                MbnImageButtonChangeIcon.this.animate().alpha(0).setDuration(200);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//
//                if (modeForThis) {
//                    setImageResource(R.drawable.ic_music_player_pause_lines);
//                } else {
//                    setImageResource(R.drawable.ic_music_player_play);
//                }
//
//                MbnImageButtonChangeIcon.this.animate().alpha(1f).setDuration(200);
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });

        animator.start();


//        animate().alpha(0).setDuration(250).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//
//                if (modeForThis) {
//                    setImageResource(R.drawable.ic_music_player_pause_lines);
//                } else {
//                    setImageResource(R.drawable.ic_music_player_play);
//                }
//
//                animate().alpha(1f).setDuration(250);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });

    }


}


package mbn.libs.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

public class AndroidUtils {

    public static void coolViewReveal(int eachTime, int betweenTime, int direction, View... views) {
        long startTime = 0;
        for (View v : views) {
            v.setAlpha(0);
            v.setTranslationY(v.getResources().getDisplayMetrics().density * 120);
            v.animate().alpha(1).translationY(0).setDuration(eachTime).setInterpolator(new OvershootInterpolator(4)).setStartDelay(startTime).start();
            startTime += betweenTime;
        }
    }

    public static void coolViewExit(int eachTime, int betweenTime, int direction, View... views) {
        long startTime = 0;
        for (View v : views) {
            v.setAlpha(1);
            v.setTranslationY(0);
            v.animate().alpha(0).translationY(-v.getTop()).setDuration(eachTime).setInterpolator(new AnticipateInterpolator()).setStartDelay(startTime).start();
            startTime += betweenTime;
        }
    }

}

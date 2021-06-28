package mbn.libs.utils.views;

import androidx.annotation.FloatRange;

public class ProgressBar_base {

    public interface MBN_ProgressBar {
        void setProgress(@FloatRange(from = 0f, to = 1f) float progress);

        float getProgress();
    }

    public interface MBN_SeekBar extends MBN_ProgressBar {
        void setListener(SeekBarListener listener);
    }

    public interface SeekBarListener {

        void onTouchStart(MBN_SeekBar seekBar);

        void onProgressChanged(MBN_SeekBar seekBar, float progress, boolean isUserTouch);

        void onTouchEnd(MBN_SeekBar seekBar);

    }
}

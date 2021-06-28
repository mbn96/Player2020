package mbn.packfragmentmanager.fragmanager;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.FrameLayout;


public abstract class BaseFrameLayoutForFrags extends FrameLayout {

    public BaseFrameLayoutForFrags(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Math.abs(right - left) > 0) layoutWithSize();
    }

    abstract void layoutWithSize();

}

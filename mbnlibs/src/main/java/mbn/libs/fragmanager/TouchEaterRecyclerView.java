package mbn.libs.fragmanager;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class TouchEaterRecyclerView extends RecyclerView {

    private String TAG = "RECYCLER_MY";

    public TouchEaterRecyclerView(Context context) {
        super(context);
    }

    public TouchEaterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEaterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * The Default implementation checks if recycler view can scroll vertically in the up direction. sub-classes should overwrite this method to change the behaviour.
     */
    private boolean shouldConsumeEvent(MotionEvent event) {
        return canScrollVertically(-1);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        boolean out;
//        if ((out = super.onInterceptTouchEvent(e)) && shouldConsumeEvent(e)) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return out;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (shouldConsumeEvent(e) && e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(e);
    }

}

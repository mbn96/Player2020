package mbn.libs.fragmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import mbn.libs.R;


@SuppressLint("ViewConstructor")
public class CustomAppBar extends FrameLayout {

    public static final int MODE_NONE = 0;
    public static final int MODE_CUSTOM = 1;
    public static final int MODE_DEFAULT = 2;

    public static final int UNIT_DP = 1;
    public static final int UNIT_VIEW_SIZE = 50;

    private float statusBarSize;
    private BaseFragment currentFrag;
    private ToolbarHolder currentToolbarHolder;
    private int currentHeight;
    private HashMap<BaseFragment, ToolbarHolder> toolbarHoldersMap = new HashMap<>();
    private CustomFragmentSwipeBackAnimator manager;
    private float density = getContext().getResources().getDisplayMetrics().density;


    public CustomAppBar(@NonNull Context context, CustomFragmentSwipeBackAnimator manager) {
        super(context);
        this.manager = manager;
    }

    void setStatusBarSize(float statusBarSize) {
        this.statusBarSize = statusBarSize;
        if (currentFrag != null) setCurrentFrag(currentFrag);
    }

    void fragmentAdded(BaseFragment fragment) {
        toolbarHoldersMap.put(fragment, new ToolbarHolder());
    }

    void fragmentRemoved(BaseFragment fragment) {
        ToolbarHolder h = toolbarHoldersMap.remove(fragment);
        if (h != null) {
            h.pop();
        }
    }

    ToolbarHolder getToolbarForFragment(BaseFragment fragment) {
        return toolbarHoldersMap.get(fragment);
    }

    private ToolbarHolder getValidToolbar(BaseFragment fragment) {
        if (fragment != null && toolbarHoldersMap.get(fragment).mode != MODE_NONE) {
            return toolbarHoldersMap.get(fragment);
        }
        BaseFragment f = manager.getTheOneBefore(fragment);
        if (f != null) return getValidToolbar(f);
        return null;
    }

    void setCurrentFrag(BaseFragment currentFrag) {
        this.currentFrag = currentFrag;
        currentToolbarHolder = getValidToolbar(currentFrag);
        if (currentToolbarHolder != null) {
            currentToolbarHolder.setVisibility(true);
            setHeight(currentToolbarHolder.calculateHeight());
            setElevation(currentToolbarHolder.elevation);
            for (ToolbarHolder h : toolbarHoldersMap.values()) {
                if (h != currentToolbarHolder)
                    h.setVisibility(false);
            }
            return;
        }
        setHeight(0);
        setElevation(0);
        for (ToolbarHolder h : toolbarHoldersMap.values()) {
            h.setVisibility(false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(currentHeight, MeasureSpec.EXACTLY));

        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, currentHeight);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(currentHeight, MeasureSpec.EXACTLY);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(widthMeasureSpec, newHeightMeasureSpec);
            }
        }
    }

    private void setHeight(float height) {
        /*
         * This was the original implementation of the method.
         *
         ViewGroup.LayoutParams layoutParams = getLayoutParams();
         layoutParams.height = (int) height;
         requestLayout();
         */

        currentHeight = (int) height;
        requestLayout();

    }

    public void fractionGetter(float fraction, int animType, BaseFragment topFrag) {
        if (toolbarHoldersMap.get(topFrag).mode == MODE_NONE) return;
        ToolbarHolder top = getValidToolbar(topFrag);
        ToolbarHolder bottom = getValidToolbar(manager.getTheOneBefore(topFrag));

        top.setVisibility(true);
        if (bottom != null) {
            bottom.setVisibility(true);
        }

        top.setFraction(fraction, animType, true);
        if (bottom != null) bottom.setFraction(fraction, animType, false);

//        float internalFraction = (float) Math.pow(fraction, 2f);

        float topH = top.calculateHeight() * (1 - fraction);
        float bottomH = bottom == null ? 0 : bottom.calculateHeight();
        bottomH = bottomH * fraction;

        setHeight(topH + bottomH);
    }

    interface ToolbarListener {
        void onSizeChanged(int fixedPart, int scrollingPart, float scrollFraction);
    }

    public class ToolbarHolder {
        private int mode;
        private boolean visibility = false;
        private float userViewSize, scrollingPartSize;
        private float elevation;
        private float fractionOfScroll = 1;
        private FrameLayout wholeToolbar;
        private ImageView background;
        private LinearLayout baseLinearLayout;
        private ToolbarListener listener;


        public void setListener(ToolbarListener listener) {
            this.listener = listener;
        }

        void invokeListener() {
            if (listener != null)
                listener.onSizeChanged((int) (statusBarSize + userViewSize), (int) scrollingPartSize, fractionOfScroll);
        }

        public LinearLayout getBaseLayout() {
            return baseLinearLayout;
        }

        public void setMode(int mode) {
            this.mode = mode;
            if (mode == MODE_DEFAULT) {
                wholeToolbar = (FrameLayout) manager.getInflater().inflate(R.layout.user_appbar_layout, CustomAppBar.this, false);
                background = wholeToolbar.findViewById(R.id.imageView);
                baseLinearLayout = wholeToolbar.findViewById(R.id.linear_layout);

                addView(wholeToolbar);
                if (visibility) {
                    wholeToolbar.setVisibility(VISIBLE);
                } else {
                    wholeToolbar.setVisibility(GONE);
                }
            }
        }

        void setBackgroundBitmap(Bitmap backgroundBitmap) {
            this.background.setImageBitmap(backgroundBitmap);
        }

        void setBackgroundColor(@ColorInt int color) {
            wholeToolbar.setBackgroundColor(color);
        }

        void setAppBarView(View view) {
            if (baseLinearLayout.getChildCount() == 2) {
                baseLinearLayout.removeViewAt(1);
            }
            baseLinearLayout.addView(view);

        }

        private void setFraction(float fraction, int animMode, boolean top) {
            if (wholeToolbar != null) {
                if (top) {
                    wholeToolbar.setAlpha(1 - fraction);
                } else {
                    wholeToolbar.setAlpha(fraction);
                }
                /*
                This is a part that animates the appBar for each fragment according to its unique animation , but it turned out it looks better if we just change the alpha alone.
                *****
                ****
                ***
                **
                *
                switch (animMode) {
                    case ANIM_BOTTOM_FANCY:
                    case ANIM_CIRCLE_REVEAL:
                    case ANIM_CUSTOM_VERTICAL:
                    case ANIM_DIALOG_TTB:
                    case ANIM_BOTTOM_NORMAL:
                        if (top) {
                            baseLinearLayout.setTranslationY(getHeight() * fraction * 0.3f);
                        } else {
                            baseLinearLayout.setTranslationY((float) (getHeight() * (-0.3 + (0.3 * fraction))));
                        }
                        break;
                    case ANIM_DEFAULT:
                    case ANIM_CUSTOM_HORIZONTAL:
                    case ANIM_DIALOG_RTL:
                        if (top) {
                            baseLinearLayout.setTranslationX(getWidth() * fraction * 0.3f);
                        } else {
                            baseLinearLayout.setTranslationX((float) (getWidth() * (-0.3 + (0.3 * fraction))));
                        }
                        break;
                }

                 */
            }
        }

        void pop() {
            if (wholeToolbar != null) removeView(wholeToolbar);
        }

        void setToolbarHeight(float value, int unit) {
            userViewSize = value * unit * density;

            invokeListener();

            if (this == currentToolbarHolder) setHeight(calculateHeight());
        }

        void setScrollingPartSize(int value) {
            scrollingPartSize = value * density;

            invokeListener();

            if (this == currentToolbarHolder) setHeight(calculateHeight());
        }

        void setFractionOfScroll(float fractionOfScroll) {
            this.fractionOfScroll = fractionOfScroll;

            invokeListener();

            if (this == currentToolbarHolder) setHeight(calculateHeight());
        }

        public void setElevation(float elevation) {
            this.elevation = elevation;
            if (this == currentToolbarHolder) setElevation(elevation);
        }

        int calculateHeight() {
            if (mode == MODE_DEFAULT) {
                return (int) (statusBarSize + userViewSize + (scrollingPartSize * fractionOfScroll));
            }
            return 0;
        }

        int getMode() {
            return mode;
        }

        float getFractionOfScroll() {
            return fractionOfScroll;
        }

        float getScrollingPartSize() {
            return scrollingPartSize;
        }

        public void setVisibility(boolean visibility) {
            this.visibility = visibility;
            if (wholeToolbar != null) {
                if (visibility) {
                    wholeToolbar.setVisibility(VISIBLE);
                } else {
                    wholeToolbar.setVisibility(GONE);
                }
            }
        }
    }
}

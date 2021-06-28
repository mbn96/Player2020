//package com.br.mreza.musicplayer.MBN.customViews;
//
//
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.os.Build;
//import android.support.annotation.Nullable;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.animation.FastOutSlowInInterpolator;
//import android.text.TextPaint;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.HorizontalScrollView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.br.mreza.musicplayer.PlayAndQueueFragment;
//import com.br.mreza.musicplayer.R;
//
//import java.util.ArrayList;
//
//import static android.widget.LinearLayout.HORIZONTAL;
//
//public class MbnTabLayout extends HorizontalScrollView implements View.OnClickListener, ViewPager.OnPageChangeListener {
//
//
//    private ViewPager mViewPager;
//
//    private PlayAndQueueFragment.Adapter mAdapter;
//
//    private int tabsCount;
//    private LinearLayout mRootLinear;
//    private ArrayList<MbnTab> tabs = new ArrayList<>();
//
//    private int scrollFactor;
//
//    private boolean rightOrLeft = true;
//    private float minus;
//
//    public MbnTabLayout(Context context) {
//        super(context);
//    }
//
//    public MbnTabLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public MbnTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    public MbnTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }
//
//    private void init() {
//
//        mRootLinear = new LinearLayout(getContext());
//        mRootLinear.setOrientation(HORIZONTAL);
//        addView(mRootLinear);
//        setHorizontalScrollBarEnabled(false);
////        setHorizontalFadingEdgeEnabled(true);
////        setFadingEdgeLength(100);
//
//
//    }
//
//    public void setmViewPager(ViewPager mViewPager) {
//        this.mViewPager = mViewPager;
//        mAdapter = (PlayAndQueueFragment.Adapter) mViewPager.getAdapter();
//        populate();
//        mViewPager.addOnPageChangeListener(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            mViewPager.setOnScrollChangeListener(this);
//        }
//    }
//
//
//    private void populate() {
//        tabsCount = mAdapter.getCount();
//        scrollFactor = getWidth() / tabsCount;
//        tabs.clear();
//        for (int i = 0; i < tabsCount; i++) {
//
////            LinearLayout tab = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_layout, this, false);
//            MbnTab tab = new MbnTab(getContext());
////            tab.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.day_rain_2));
//            tab.setOnClickListener(this);
//            tab.setText(mAdapter.getPageTitle(i));
//            tab.setImage(mAdapter.getTabImage(i));
//            tab.number = i;
//            tabs.add(tab);
//        }
//
//        for (LinearLayout button : tabs) {
//
//            mRootLinear.addView(button);
//
//        }
//        tabChanged();
//
//    }
//
//
//    private void tabChanged() {
//
//        for (MbnTab tab : tabs) {
//
//            if (tab.number == mViewPager.getCurrentItem()) {
//                tab.textView.setTextColor(Color.CYAN);
//                tab.imageView.setImageTintList(ColorStateList.valueOf(Color.CYAN));
//                tab.setMode(true);
//            } else {
//                tab.textView.setTextColor(Color.GRAY);
//                tab.imageView.setImageTintList(ColorStateList.valueOf(Color.GRAY));
//                tab.setMode(false);
//
//            }
//
//        }
//
//
//    }
//
//    private void getScrollAmount() {
//
//        int temp = 0;
//        for (MbnTab t : tabs) {
//
//            temp += t.getWidth();
//
//        }
//
//        scrollFactor = temp / tabsCount;
//
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//        MbnTab t = (MbnTab) v;
//
//        mViewPager.setCurrentItem(t.number);
//
//
//    }
//
//    private void tabSizeChanger(float offset) {
//
//        if (offset != 0) {
//
//            minus = offset;
//        }
//
//
//        try {
//            if (!rightOrLeft) {
//
//                tabs.get(mViewPager.getCurrentItem()).textView.setTextSize(15 - (5 * minus));
//                tabs.get(mViewPager.getCurrentItem() + 1).textView.setTextSize(15 + (5 * minus));
//
//            } else {
//
//                tabs.get(mViewPager.getCurrentItem()).textView.setTextSize(15 - (5 * minus));
//                tabs.get(mViewPager.getCurrentItem() - 1).textView.setTextSize(15 - (5 * minus));
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
////        System.out.println("pppppppppppppppp  "+position);
//
////        tabSizeChanger(positionOffset);
//
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//
////        scrollFactor = getscroll / tabsCount;
//        getScrollAmount();
////        System.out.println(scrollFactor);
//        smoothScrollTo(scrollFactor * position + 1, 0);
//        tabChanged();
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
////        System.out.println("ssssssssssssss  "+state);
//
//    }
//
////    @Override
////    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
////
//////        rightOrLeft = ((i / view.getWidth()) > mViewPager.getCurrentItem());
////
////
////    }
//
//
//    private class MbnTab extends LinearLayout {
//
//        private TextView textView;
//        private ImageView imageView;
//        private int number;
//        private boolean mode = true;
//
//
//        public MbnTab(Context context) {
//            super(context);
//            initiate();
//        }
//
//        public MbnTab(Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//            initiate();
//        }
//
//        public MbnTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            initiate();
//        }
//
//        public MbnTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//            super(context, attrs, defStyleAttr, defStyleRes);
//            initiate();
//        }
//
//
//        private void initiate() {
//
//            setOrientation(HORIZONTAL);
//
//            LinearLayout temp = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_layout, this, false);
//            addView(temp);
//            textView = temp.findViewById(R.id.tab_text_view);
//            imageView = temp.findViewById(R.id.tab_image_view);
////            textView.setPivotX(0);
////            textView.getLayoutParams().width = MbnTabLayout.this.getWidth();
////            textView.requestLayout();
//
//
//        }
//
//        public void setText(CharSequence text) {
//
//
//            textView.setText(text);
//
//        }
//
//        @Override
//        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//            super.onSizeChanged(w, h, oldw, oldh);
//
////            for (MbnTab tab : tabs) {
////
////                tab.getLayoutParams().width = MbnTabLayout.this.getWidth();
////
//////            Object param = tab.getLayoutParams();
////                tab.setLayoutParams(tab.getLayoutParams());
////
////
////            }
//
//        }
//
//        public void setImage(Bitmap image) {
//
//            imageView.setImageBitmap(image);
//
//
//        }
//
//
//        public void setMode(boolean mode) {
//
//            if (this.mode != mode) {
//
//                this.mode = mode;
//
//                animateTheText();
//
//            }
//
//        }
//
//        private int getLength() {
//
//            return mAdapter.getPageTitle(number).length();
//        }
//
//
//        private void animateTheText() {
//
//            final String text = mAdapter.getPageTitle(number).toString() + "a";
//
//            TextPaint paint = textView.getPaint();
//            Rect rect = new Rect();
//
//            paint.getTextBounds(text, 0, getLength() + 1, rect);
//
//
//            int i1, i2;
//            if (mode) {
//                i1 = 0;
////                i2 = 1;
//                i2 = rect.width();
//            } else {
////                i1 = 1;
//                i1 = rect.width();
//                i2 = 0;
//            }
//
//            ValueAnimator animator = ValueAnimator.ofInt(i1, i2).setDuration(500);
//            animator.setInterpolator(new FastOutSlowInInterpolator());
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//
//                    textView.getLayoutParams().width = (int) valueAnimator.getAnimatedValue();
//                    textView.requestLayout();
//
////                    textView.setScaleX((Float) valueAnimator.getAnimatedValue());
//
//                }
//            });
//
//            animator.start();
//
//
//        }
//
//    }
//
//
//}

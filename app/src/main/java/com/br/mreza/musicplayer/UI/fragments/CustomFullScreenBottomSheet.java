//package com.br.mreza.musicplayer.UI.fragments;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.os.Bundle;
//import androidx.annotation.LayoutRes;
//import androidx.annotation.NonNull;
//import androidx.annotation.StyleRes;
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.core.view.AccessibilityDelegateCompat;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
//import androidx.core.widget.NestedScrollView;
//import androidx.appcompat.app.AppCompatDialog;
//import androidx.appcompat.app.AppCompatDialogFragment;
//import android.util.TypedValue;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//
//import com.br.mreza.musicplayer.R;
//
//
//public class CustomFullScreenBottomSheet extends AppCompatDialogFragment {
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new BottomDialog(getContext(), getTheme());
//    }
//
//
//    private static class BottomDialog extends AppCompatDialog {
//
//
//        private BottomSheetBehavior<FrameLayout> mBehavior;
//
//        private boolean shouldDismiss = false;
//
//        boolean mCancelable = true;
//        private boolean mCanceledOnTouchOutside = true;
//        private boolean mCanceledOnTouchOutsideSet;
//        private CoordinatorLayout coordinator;
//
//        public BottomDialog(@NonNull Context context) {
//            this(context, 0);
//        }
//
//        public BottomDialog(@NonNull Context context, @StyleRes int theme) {
//            super(context, getThemeResId(context, theme));
//            // We hide the title bar for any style configuration. Otherwise, there will be a gap
//            // above the bottom sheet when it is expanded.
//            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        }
//
//        protected BottomDialog(@NonNull Context context, boolean cancelable,
//                               OnCancelListener cancelListener) {
//            super(context, cancelable, cancelListener);
//            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//            mCancelable = cancelable;
//        }
//
//        @Override
//        public void setContentView(@LayoutRes int layoutResId) {
//            super.setContentView(wrapInBottomSheet(layoutResId, null, null));
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            getWindow().setLayout(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        }
//
//        @Override
//        public void setContentView(View view) {
//            super.setContentView(wrapInBottomSheet(0, view, null));
//        }
//
//        @Override
//        public void setContentView(View view, ViewGroup.LayoutParams params) {
//            super.setContentView(wrapInBottomSheet(0, view, params));
//        }
//
//        @Override
//        public void setCancelable(boolean cancelable) {
//            super.setCancelable(cancelable);
//            if (mCancelable != cancelable) {
//                mCancelable = cancelable;
//                if (mBehavior != null) {
//                    mBehavior.setHideable(cancelable);
//                }
//            }
//        }
//
//        @Override
//        protected void onStart() {
//            super.onStart();
//            if (mBehavior != null) {
//                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        }
//
//
//        @Override
//        public void cancel() {
////        coordinator.animate().setDuration(500).
//            if (!shouldDismiss) {
//                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//            } else {
//                super.cancel();
//            }
//        }
//
//        @Override
//        public void setCanceledOnTouchOutside(boolean cancel) {
//            super.setCanceledOnTouchOutside(cancel);
//            if (cancel && !mCancelable) {
//                mCancelable = true;
//            }
//            mCanceledOnTouchOutside = cancel;
//            mCanceledOnTouchOutsideSet = true;
//        }
//
//        private View wrapInBottomSheet(int layoutResId, View view, ViewGroup.LayoutParams params) {
//            coordinator = (CoordinatorLayout) View.inflate(getContext(),
//                    R.layout.layout_for_bottomsheet_base, null);
//            if (layoutResId != 0 && view == null) {
//                view = getLayoutInflater().inflate(layoutResId, coordinator, false);
//            }
//            FrameLayout bottomSheet = coordinator.findViewById(R.id.frame_in_sheet);
////            bottomSheet.setClipToOutline(true);
//            mBehavior = BottomSheetBehavior.from(bottomSheet);
////        float des = getContext().getResources().getDisplayMetrics().density;
//            int disH = getContext().getResources().getDisplayMetrics().heightPixels;
//            mBehavior.setPeekHeight(disH);
//            mBehavior.setBottomSheetCallback(mBottomSheetCallback);
//            mBehavior.setHideable(mCancelable);
////            NestedScrollView placeForFrag = bottomSheet.findViewById(R.id.place_for_views);
////        getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
//
//            coordinator.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
////        getWindow().setStatusBarColor(Color.argb(0, 0, 0, 0));
//
//            try {
//                //noinspection ConstantConditions
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            if (params == null) {
//                bottomSheet.addView(view);
//            } else {
//                bottomSheet.addView(view, params);
//            }
//            // We treat the CoordinatorLayout as outside the dialog though it is technically inside
//            coordinator.findViewById(android.support.design.R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
//                        cancel();
//                    }
//                }
//            });
//            // Handle accessibility events
//            ViewCompat.setAccessibilityDelegate(bottomSheet, new AccessibilityDelegateCompat() {
//                @Override
//                public void onInitializeAccessibilityNodeInfo(View host,
//                                                              AccessibilityNodeInfoCompat info) {
//                    super.onInitializeAccessibilityNodeInfo(host, info);
//                    if (mCancelable) {
//                        info.addAction(AccessibilityNodeInfoCompat.ACTION_DISMISS);
//                        info.setDismissable(true);
//                    } else {
//                        info.setDismissable(false);
//                    }
//                }
//
//                @Override
//                public boolean performAccessibilityAction(View host, int action, Bundle args) {
//                    if (action == AccessibilityNodeInfoCompat.ACTION_DISMISS && mCancelable) {
//                        cancel();
//                        return true;
//                    }
//                    return super.performAccessibilityAction(host, action, args);
//                }
//            });
//            bottomSheet.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent event) {
//                    // Consume the event and prevent it from falling through
//                    return true;
//                }
//            });
//            return coordinator;
//        }
//
//        boolean shouldWindowCloseOnTouchOutside() {
//            if (!mCanceledOnTouchOutsideSet) {
//                TypedArray a = getContext().obtainStyledAttributes(
//                        new int[]{android.R.attr.windowCloseOnTouchOutside});
//                mCanceledOnTouchOutside = a.getBoolean(0, true);
//                a.recycle();
//                mCanceledOnTouchOutsideSet = true;
//            }
//            return mCanceledOnTouchOutside;
//        }
//
//        private static int getThemeResId(Context context, int themeId) {
//            if (themeId == 0) {
//                // If the provided theme is 0, then retrieve the dialogTheme from our theme
//                TypedValue outValue = new TypedValue();
//                if (context.getTheme().resolveAttribute(
//                        android.support.design.R.attr.bottomSheetDialogTheme, outValue, true)) {
//                    themeId = outValue.resourceId;
//                } else {
//                    // bottomSheetDialogTheme is not provided; we default to our light theme
//                    themeId = android.support.design.R.style.Theme_Design_Light_BottomSheetDialog;
//                }
//            }
//            return themeId;
//        }
//
//        private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
//                = new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet,
//                                       @BottomSheetBehavior.State int newState) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    shouldDismiss = true;
//                    cancel();
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        };
//
//    }
//
//}
//
//

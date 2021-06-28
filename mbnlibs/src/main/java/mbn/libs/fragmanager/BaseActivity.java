package mbn.libs.fragmanager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import mbn.libs.R;

import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;


@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    CustomFragmentSwipeBackAnimator swipeManager;

    private boolean timeToQuit = false;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private TouchDispatch touchListener;

    public CustomFragmentSwipeBackAnimator getSwipeManager() {
        return swipeManager;
    }

    public void setSwipeManager(CustomFragmentSwipeBackAnimator swipeManager) {
        this.swipeManager = swipeManager;
    }

    private final Runnable exitTimer = () -> timeToQuit = false;

    private boolean checkForExit() {
        if (timeToQuit) {
            return true;
        }
        timeToQuit = true;
        Snackbar.make(swipeManager, "Press again to exit !", Snackbar.LENGTH_LONG).setAction("Exit Now", v -> finish()).setActionTextColor(Color.RED).show();
        mainHandler.postDelayed(exitTimer, 2000);
        return false;
    }

    @Override
    public void onBackPressed() {
        if ((swipeManager == null || !swipeManager.popFragment()) && checkForExit()) {
            super.onBackPressed();
        }
    }


    protected int layoutID() {
        return R.layout.base_activity;
    }

    public CustomFragmentSwipeBackAnimator introduceTheManager() {
        return findViewById(R.id.base_container);
    }

    public abstract BaseFragment getFirstFragment();

    public void hideStatusBar(boolean hide) {
        if (hide) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            }
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() - (decor.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN));
        }
    }

    public void setUI_style(boolean fullscreen, boolean light) {
        int visibility = 0;
        if (fullscreen) {
            visibility |= (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (light) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
            }
        }
        getWindow().getDecorView().setSystemUiVisibility(visibility);
    }

    public void lightUI(boolean light) {
        int visibility = getWindow().getDecorView().getSystemUiVisibility();
        if (!light) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                visibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility = visibility & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                visibility = visibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility = visibility | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
            }
        }
        getWindow().getDecorView().setSystemUiVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(layoutID());
        setSwipeManager(introduceTheManager());
        CustomFragmentSwipeBackAnimator.setINSTANCE(getSwipeManager());
        if (savedInstanceState == null) {
            getSwipeManager().start(getSupportFragmentManager(), getFirstFragment(), this);
            getSwipeManager().manageFragsStates();
            getSwipeManager().getAppBar().setCurrentFrag(getSwipeManager().getTopFrag());
        }

    }

    private final SparseArray<ActivityCompat.OnRequestPermissionsResultCallback> permissionsResultCallbacks = new SparseArray<>();

    public void addPermissionResultCallback(String[]
                                                    permissions, ActivityCompat.OnRequestPermissionsResultCallback callback, int requestCode) {
        permissionsResultCallbacks.put(requestCode, callback);
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsResultCallbacks.get(requestCode).onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsResultCallbacks.remove(requestCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSwipeManager().setActivityStarted(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        getSwipeManager().setActivityStarted(false);
    }


    @Override
    protected void onPause() {
        super.onPause();
        getSwipeManager().onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSwipeManager().manageFragsStates();
    }

    @Override
    protected void onDestroy() {
        getSwipeManager().onDestroy();
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSwipeManager().createSavedInstanceState(outState);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null) {
            getSwipeManager().start(getSupportFragmentManager(), getFirstFragment(), this);
            getSwipeManager().manageFragsStates();
        } else {
            getSwipeManager().resumeFromSaveState(savedInstanceState, getSupportFragmentManager(), this);
        }

    }

    interface TouchDispatch {
        void onTouchDispatch(MotionEvent event);
    }

    void setTouchListener(TouchDispatch touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (touchListener != null) touchListener.onTouchDispatch(ev);
        return super.dispatchTouchEvent(ev);
    }
}

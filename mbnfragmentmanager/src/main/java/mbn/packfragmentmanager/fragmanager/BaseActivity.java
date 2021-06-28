package mbn.packfragmentmanager.fragmanager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    CustomFragmentSwipeBackAnimator swipeManager;

    private boolean timeToQuit = false;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public CustomFragmentSwipeBackAnimator getSwipeManager() {
        return swipeManager;
    }

    public void setSwipeManager(CustomFragmentSwipeBackAnimator swipeManager) {
        this.swipeManager = swipeManager;
    }

    private Runnable exitTimer = () -> timeToQuit = false;

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


    public abstract int layoutID();

    public abstract CustomFragmentSwipeBackAnimator introduceTheManager();

    public abstract BaseFragment getFirstFragment();

    public void hideStatusBar(boolean hide) {
        if (hide) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() - (decor.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID());
        setSwipeManager(introduceTheManager());
        CustomFragmentSwipeBackAnimator.setINSTANCE(getSwipeManager());
        if (savedInstanceState == null) {
            getSwipeManager().start(getSupportFragmentManager(), getFirstFragment(), this);
            getSwipeManager().manageFragsStates();
            getSwipeManager().getAppBar().setCurrentFrag(getSwipeManager().getTopFrag());
        }

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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
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
}

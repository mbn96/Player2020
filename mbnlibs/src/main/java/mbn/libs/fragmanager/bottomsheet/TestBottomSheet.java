package mbn.libs.fragmanager.bottomsheet;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import mbn.libs.fragmanager.BaseBottomSheetDialog;

public class TestBottomSheet extends BaseBottomSheetDialog {
    @Override
    protected View getBottomLayout(ViewGroup c) {
        int color = Color.RED;
//        int color = Color.argb(220, 255, 255, 255);
        View view = new View(getContext());
        view.setBackgroundColor(Color.BLUE);
        getStatusBarExtender().setAccentColor(color);
        getDialogBase().postDelayed(this::invalidateBackBlur, 500);
        return view;
    }

    static class TestP implements Parcelable {
        protected TestP(Parcel in) {
        }

        public static final Creator<TestP> CREATOR = new Creator<TestP>() {
            @Override
            public TestP createFromParcel(Parcel in) {
                return new TestP(in);
            }

            @Override
            public TestP[] newArray(int size) {
                return new TestP[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }

}

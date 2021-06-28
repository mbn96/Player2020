package mbn.libs.fragmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.atomic.AtomicInteger;

import mbn.libs.R;
import mbn.libs.imagelibs.imageworks.Effects;
import mbn.libs.utils.views.NeonButton;

public class PermissionRequestFrag extends BaseBottomSheetDialog implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final AtomicInteger ID_MAKER = new AtomicInteger(100);

    private int color, color_text;
    private String[] perms;
    private String message;
    private int id;

    public static PermissionRequestFrag newInstance(String message, String[] permissions, int result_id, int backgroundColor, int color_text) {

        Bundle args = new Bundle();
        args.putString("MSG", message);
        args.putStringArray("perms", permissions);
        args.putInt("color", backgroundColor);
        args.putInt("color_text", color_text);
        args.putInt("ID", ID_MAKER.getAndIncrement());
        args.putInt("result_receiver", result_id);
        PermissionRequestFrag fragment = new PermissionRequestFrag();
        fragment.setArguments(args);
        return fragment;
    }

    private int getId() {
        if (id == 0) {
            id = getArguments().getInt("ID");
        }
        return id;
    }

    @Override
    protected View getBottomLayout(ViewGroup container) {
        color = getArguments().getInt("color");
        color_text = getArguments().getInt("color_text");
        perms = getArguments().getStringArray("perms");
        message = getArguments().getString("MSG");

        getStatusBarExtender().setAccentColor(color);
        ViewGroup parentView = (ViewGroup) getLayoutInflater().inflate(R.layout.permission_layout, container, false);
        parentView.setBackgroundColor(color);

        TextView textView = parentView.findViewById(R.id.message);
        textView.setText(message);
        textView.setTextColor(color_text);

        NeonButton button = parentView.findViewById(R.id.ok_btn);
        button.setStyle(Effects.getMonochromePercentage(color) >= 0.5 ? NeonButton.Style.Light : NeonButton.Style.Dark);
        button.setAccentColor(button.getStyle() == NeonButton.Style.Light ? Color.BLACK : Color.WHITE);
        button.setOnClickListener(v -> PermissionRequestFrag.this.getActivity().addPermissionResultCallback(perms, PermissionRequestFrag.this, PermissionRequestFrag.this.getId()));

        return parentView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ResultReceivingManager.sendResult(getArguments().getInt("result_receiver"), new Object[]{permissions, grantResults});
        getFragmentSwipeBackManager().popFragment();
    }
}

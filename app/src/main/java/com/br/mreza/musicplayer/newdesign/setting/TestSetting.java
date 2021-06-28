package com.br.mreza.musicplayer.newdesign.setting;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;

import java.util.ArrayList;

import mbn.libs.fragmanager.MbnBaseSettingAdapter;
import mbn.libs.utils.views.BubbleSwitch;
import mbn.libs.utils.views.NeonButton;


public class TestSetting extends MbnBaseSettingAdapter<BubbleSwitch> {

    public TestSetting(Context context) {
        super(context);
    }

    @Override
    protected void setColors() {
        emptySpaceColor = 0xff304050;
        itemsBackColor = 0xff405060;
        titlesColor = Color.WHITE;
        subTextsColor = Color.LTGRAY;
        accentColor = Color.WHITE;

    }

    @Override
    protected void setTextsSize() {
        titleTextSize = 15;
        subTextSize = 12;
    }

    @Override
    public void onCreateListItems(ArrayList<Item> items) {
        items.clear();
        items.add(new Item("Design & Appearance", "see below"));
        items.add(new ClickableItem("Notification settings", "click to see the details", ContextCompat.getDrawable(getAdapterContext(), R.drawable.ic_share_png)));
        items.add(new SwitchableItem("Show album cover on lock-screen", StorageUtils.isShowAlbumArt()));
        if (StorageUtils.isShowAlbumArt())
            items.add(new SwitchableItem("Show blur album cover on lock-screen", "Shows album cover in a blurry mode on lock-screen.", StorageUtils.isShowBlurAlbumArt()));
        items.add(new SwitchableItem("Light Theme", "Activates light theme.You must restart the app for it to work properly.", StorageUtils.getThemeType() == 1));
        items.add(new Item("FAQ"));
        items.add(new InfoItem(null, "Mbn 2018 CopyRight"));

    }

    @Override
    public void onClickableItemClicked(ArrayList<Item> items, int pos, ClickableItem item) {

    }


    @Override
    protected BubbleSwitch instantiateSwitch(Context context) {
        return new BubbleSwitch(context);
    }

    @Override
    protected void prepareSwitch(BubbleSwitch switchView) {
        switchView.setStyle(NeonButton.Style.Dark);
        switchView.setAccentColor(accentColor);
        switchView.setTouchable(false);
    }

    @Override
    protected void setChecked(boolean check, BubbleSwitch switchView) {
        switchView.setCheck(check);
    }

    @Override
    public boolean onSwitchItemClicked(ArrayList<Item> items, int pos, SwitchableItem item, BubbleSwitch switchView) {
        switch (pos) {
            case 2:
                StorageUtils.setShowAlbumArt(getAdapterContext(), !switchView.isChecked());
                if (switchView.isChecked()) {
                    items.remove(3);
                    notifyItemRemoved(3);
                } else {
                    if (StorageUtils.isShowAlbumArt())
                        items.add(3, new SwitchableItem("Show blur album cover on lock-screen", "Shows album cover in a blurry mode on lock-screen.", StorageUtils.isShowBlurAlbumArt()));
                    notifyItemInserted(3);
                }
                break;
            case 3:
                StorageUtils.setShowBlurArt(getAdapterContext(), !switchView.isChecked());
                break;
            case 4:
                StorageUtils.setThemeType(getAdapterContext(), !switchView.isChecked() ? 1 : 0);
                break;
        }
        return !switchView.isChecked();
    }
}

package mbn.packfragmentmanager.fragmanager.popmenu;


import androidx.appcompat.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import mbn.packfragmentmanager.fragmanager.BasicMbnPopupMenu;

public abstract class MbnPopupMenuWithMenuApi extends BasicMbnPopupMenu {

    private Menu menu;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    public abstract int onGetMenuId();

    protected void onPrepareMenu(Menu menu) {
    }

    @Override
    public BasicMbnPopupMenu.PopupItem[] getItems() {
        PopupMenu popupMenu = new PopupMenu(getContext(), getDialogBase());
        popupMenu.inflate(onGetMenuId());
        menu = popupMenu.getMenu();
        popupMenu.dismiss();
        onPrepareMenu(menu);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isVisible()) {
                menuItems.add(menu.getItem(i));
            }
        }
        PopupItem[] items = new PopupItem[menuItems.size()];

        for (int i = 0; i < items.length; i++) {
            items[i] = new PopupItem(menuItems.get(i).getIcon(), menuItems.get(i).getTitle().toString());
        }
        return items;
    }

    public abstract void onItemClicked(int id);

    @Override
    public void onItemClicked(PopupItem[] items, int position) {
        onItemClicked(menuItems.get(position).getItemId());
    }


}

package mbn.libs.UI;

import java.util.ArrayList;
import java.util.EnumMap;

import mbn.libs.utils.BatchFunctionList;

public class ThemeCoordinator<E extends Enum, I extends ThemeCoordinator.ProfileItem<E>> {

    private BatchFunctionList<ProfileListener<E, I>> listeners = new BatchFunctionList<>();
    private BatchFunctionList<I> items = new BatchFunctionList<>();

    public void registerListener(ProfileListener<E, I> listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);

        items.batch(element -> {
            listener.onProfileItemChanged(element.getType(), element);
        });
    }

    public void unregisterListener(ProfileListener<E, I> listener) {
        listeners.remove(listener);
    }

    private I getItem(E type) {
        for (I it : items) {
            if (it.getType() == type) {
                return it;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    /**
     * @Hide
     */
    void changeItemValue(E type, Object v) {
        I item = getItem(type);
        if (item != null) {
            item.setValue(v);
            listeners.batch(element -> {
                element.onProfileItemChanged(item.getType(), item);
            });
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(I item) {
        I pre = getItem(item.getType());
        if (pre != null) {
            items.remove(pre);
        }
        items.add(item);
        listeners.batch(element -> {
            element.onProfileItemChanged(item.getType(), item);
        });
    }


    public static abstract class ProfileItem<E extends Enum> {

        public abstract E getType();

        public abstract void setValue(Object v);

        public abstract Object getValue();

//        public abstract Class getValueType();

    }

    public interface ProfileListener<E extends Enum, I extends ThemeCoordinator.ProfileItem<E>> {
        void onProfileItemChanged(E type, I changed);
    }


    public enum DefaultThemeTypes {
        BackgroundColor, AccentColor, TitleTextColor, ContextTextColor
    }

    public static class DefaultThemeCoordinator extends ThemeCoordinator<DefaultThemeTypes, ColorItem> {

        public DefaultThemeCoordinator(int backgroundColor, int accentColor, int titleTextColor, int contextTextColor) {
            addItem(new ColorItem(DefaultThemeTypes.BackgroundColor, backgroundColor));
            addItem(new ColorItem(DefaultThemeTypes.AccentColor, accentColor));
            addItem(new ColorItem(DefaultThemeTypes.TitleTextColor, titleTextColor));
            addItem(new ColorItem(DefaultThemeTypes.ContextTextColor, contextTextColor));
        }

        public void setValues(int backgroundColor, int accentColor, int titleTextColor, int contextTextColor) {
            addItem(new ColorItem(DefaultThemeTypes.BackgroundColor, backgroundColor));
            addItem(new ColorItem(DefaultThemeTypes.AccentColor, accentColor));
            addItem(new ColorItem(DefaultThemeTypes.TitleTextColor, titleTextColor));
            addItem(new ColorItem(DefaultThemeTypes.ContextTextColor, contextTextColor));
        }
    }

    public static class ColorItem extends ProfileItem<DefaultThemeTypes> {
        private DefaultThemeTypes type;
        private int color;

        public ColorItem(DefaultThemeTypes type, int color) {
            this.type = type;
            this.color = color;
        }

        @Override
        public DefaultThemeTypes getType() {
            return type;
        }

        @Override
        public void setValue(Object v) {
            color = (int) v;
        }

        @Override
        public Integer getValue() {
            return color;
        }

//        @Override
//        public Class getValueType() {
//            return int.class;
//        }
    }
}

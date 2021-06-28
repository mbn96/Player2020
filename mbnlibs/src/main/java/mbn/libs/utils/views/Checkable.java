package mbn.libs.utils.views;

public class Checkable {

    public interface CheckableView {
        void setCheck(boolean check);

        boolean isChecked();

        void setCheckableListener(CheckableListener listener);
    }

    public interface CheckableListener {
        void onCheckChanged(boolean check, boolean isUserTouch);
    }

}

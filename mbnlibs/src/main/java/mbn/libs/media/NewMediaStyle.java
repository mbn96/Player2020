package mbn.libs.media;

import android.annotation.SuppressLint;
import android.app.Notification;

import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;

public class NewMediaStyle extends NotificationCompat.Style {

    int[] indices;

    public NewMediaStyle(int... indices) {
        this.indices = indices;
    }

    @Override
    public void apply(NotificationBuilderWithBuilderAccessor builder) {
        @SuppressLint("RestrictedApi") Notification.MediaStyle style = new Notification.MediaStyle(builder.getBuilder());
        style.setShowActionsInCompactView(indices);
    }
}

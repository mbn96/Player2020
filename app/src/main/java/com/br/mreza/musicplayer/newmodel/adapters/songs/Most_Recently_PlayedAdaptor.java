package com.br.mreza.musicplayer.newmodel.adapters.songs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.MySongDAO;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.p2020.management.MostPlayedItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.utils.JavaUtils;

public class Most_Recently_PlayedAdaptor extends RecentlyAddedAdapter implements BaseTaskHolder.ResultReceiver {

    boolean shouldUpdate = false;
    int type;

    public Most_Recently_PlayedAdaptor(Context context, RecyclerView recyclerView, int type) {
        super(context, recyclerView);
        this.type = type;
        shouldUpdate = true;
        update();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResult(Object result, Object info) {
        ArrayList<MostPlayedItem> items = (ArrayList<MostPlayedItem>) result;
        ArrayList<Long> ids = new ArrayList<>();
        if (items != null) {
            for (MostPlayedItem item : items) {
                ids.add(item.getId());
            }
            setSongsIDs(ids);
        }
    }

    @Override
    protected void update() {
        if (shouldUpdate) {
//            ArrayList<Long> ids = new ArrayList<>();
//            ArrayList<MostPlayedItem> items = null;

            String methodName = null;

            if (type == 0) {
//                items = (ArrayList<MostPlayedItem>) DataBaseHolder.getInstance().getSongDAO().getRecentlyPlayed();
//                try {
//                    Method method = MySongDAO.class.getMethod("getRecentlyPlayed", null);
//                    items = (ArrayList<MostPlayedItem>) method.invoke(DataBaseHolder.getInstance().getSongDAO());
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }

//                try {
//                    DataBaseManager.getManager().executeCustomTask(MySongDAO.class.getMethod("getRecentlyPlayed"), null, true, this);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }

                methodName = "getRecentlyPlayed";

            } else if (type == 1) {
//                items = (ArrayList<MostPlayedItem>) DataBaseHolder.getInstance().getSongDAO().getMostPlayed();

//                try {
//                    DataBaseManager.getManager().executeCustomTask(MySongDAO.class.getMethod("getMostPlayed"), null, true, this);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }

                methodName = "getMostPlayed";
            }

            if (methodName != null) {
                DataBaseManager.getManager().executeCustomTask(JavaUtils.getMethod(MySongDAO.class, methodName), null, true, this);
            }

//            if (items != null) {
//                for (MostPlayedItem item : items) {
//                    ids.add(item.getId());
//                }
//            }
//
//            setSongsIDs(ids);

        }
    }

}

package com.br.mreza.musicplayer.newmodel.service.player;


import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;

import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newmodel.database.A_B_Manager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;
import com.br.mreza.musicplayer.newmodel.service.NewModelPlayerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;

public class PlayerCommunicates {

    private static final PlayerCommunicates INSTANCE = new PlayerCommunicates();

    public static PlayerCommunicates getINSTANCE() {
        return INSTANCE;
    }

    private PlayerCommunicates() {
    }

    private WeakReference<Context> contextWeakReference;
    private PlayerManager playerManager;
    private ArrayList<PlayerCallbacks> callbacks = new ArrayList<>();

    public void init(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    private boolean haveContext() {
        return contextWeakReference.get() != null;
    }

    private Context getContext() {
        return contextWeakReference.get();
    }

    void registerPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    void releasePlayerManager() {
        playerManager = null;
    }


    public void setA() {
        if (isPlaying()) {
            A_B_Manager.INSTANCE.request(A_B_Manager.SET_A, (int) playerManager.getCurrentPos(), null);
        } else {
            A_B_Manager.INSTANCE.request(A_B_Manager.SET_A, (int) StorageUtils.getStartFromPos(getContext()), null);
        }
    }

    public void setB() {
        if (isPlaying()) {
            A_B_Manager.INSTANCE.request(A_B_Manager.SET_B, (int) playerManager.getCurrentPos(), null);
        } else {
            A_B_Manager.INSTANCE.request(A_B_Manager.SET_B, (int) StorageUtils.getStartFromPos(getContext()), null);
        }
    }

    public void checkForPrevious() {
        if (isPlaying()) {
            long pos = playerManager.getCurrentPos();
            if (pos > 5000) {
                playerManager.seek(0);
                return;
            }
        }
        DataBaseManager.getManager().performPrevious();
    }

    public void play() {
        if (isPlaying()) {
            return;
        }
        if (playerManager != null) {
            playerManager.tryStarting();
            return;
        }
        if (haveContext()) {
            Intent intent = new Intent(getContext(), NewModelPlayerService.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                getContext().startForegroundService(intent);
            } else {
                getContext().startService(intent);
            }
        }
    }

    public void pause() {
        if (isPlaying()) playerManager.tryPausing();
    }

    public boolean isPlaying() {
        return playerManager != null && playerManager.isPlaying();
    }

    public void seek(long pos) {
        if (isPlaying()) {
            playerManager.seek(pos);
        } else if (haveContext()) {
            StorageUtils.setStartFromPos(getContext(), pos);
        }
    }

    public void rewind() {
        if (isPlaying()) {
            playerManager.rewind();
        } else if (haveContext()) {
            StorageUtils.setStartFromPos(getContext(), StorageUtils.getStartFromPos(getContext()) - 5000);
        }
    }

    public void fastForward() {
        if (isPlaying()) {
            playerManager.fastForward();
        } else if (haveContext()) {
            StorageUtils.setStartFromPos(getContext(), StorageUtils.getStartFromPos(getContext()) + 5000);
        }
    }

    void setCurrentPos(long currentPos) {
        for (PlayerCallbacks cb : callbacks) {
            cb.onCurrentPosChange(currentPos);
        }
    }

    void setPlayingState(boolean state) {
        stateChanged(state);
        for (PlayerCallbacks cb : callbacks) {
            cb.onPlayerStateChange(state);
        }
    }

    public void registerCallback(PlayerCallbacks playerCallback) {
        if (!callbacks.contains(playerCallback)) callbacks.add(playerCallback);
    }

    public void unRegisterCallback(PlayerCallbacks playerCallback) {
        callbacks.remove(playerCallback);
    }

    public interface PlayerCallbacks {
        void onCurrentPosChange(long pos);

        void onPlayerStateChange(boolean state);

    }


    /*---------------------------- Played Time Management --------------------------*/
    //------------------------------------------------------------------------------//

    private boolean currentState = false;
    private long currentID;

    private void stateChanged(boolean state) {
        if (state) {
            put(currentID, (int) (System.currentTimeMillis() / 1000), DataBaseUtils.PLAY_TIME);
        } else if (currentState) {
            put(currentID, (int) (System.currentTimeMillis() / 1000), DataBaseUtils.PAUSE_TIME);
        }
        currentState = state;
    }

    void songChanged(long id) {
        if (currentState && currentID != 0) {
            put(currentID, (int) (System.currentTimeMillis() / 1000), DataBaseUtils.PAUSE_TIME);
        }
        currentID = id;
    }

    private void put(long id, int time, int op) {
        ThreadManager.getAppGlobalTask().StartTask(new PlayTimeUpdater(id, time, op), null);
    }

    private static class PlayTimeUpdater implements BaseTaskHolder.BaseTask {

        private long id;
        private int time;
        private int op;

        PlayTimeUpdater(long id, int time, int op) {
            this.id = id;
            this.time = time;
            this.op = op;
        }

        @Override
        public Object onRun() {
            DataBaseUtils.setPlayTime(id, time, op);
            return null;
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }
}

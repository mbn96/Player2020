package com.br.mreza.musicplayer;

import com.br.mreza.musicplayer.MBN.customViews.VisualizerView;

public class VisyHelper {

    private VisualizerView visualizerView;
    private VisualizerView visualizerNewDesign;

    public void setVisualizerView(VisualizerView visualizerView) {
        this.visualizerView = visualizerView;
    }

    public void setVisualizerNewDesign(VisualizerView visualizerNewDesign) {
        this.visualizerNewDesign = visualizerNewDesign;
        try {
//            reset(MbnController.audioID);
        } catch (Exception ignored) {
        }
    }

    public void inAct() {
        if (visualizerView != null) {
            visualizerView.inActive();
        }
        if (visualizerNewDesign != null) {
            visualizerNewDesign.inActive();
        }

    }

    public void reset(int id) {
        if (visualizerView != null) {
            visualizerView.active(id);
        }
        if (visualizerNewDesign != null) {
            visualizerNewDesign.active(id);
        }

    }

}
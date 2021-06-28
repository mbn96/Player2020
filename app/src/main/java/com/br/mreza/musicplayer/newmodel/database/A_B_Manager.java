package com.br.mreza.musicplayer.newmodel.database;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class A_B_Manager {
    public static final A_B_Manager INSTANCE = new A_B_Manager();

    private A_B_Manager() {
    }

    public static final int SET_STATE_OFF = 0;
    public static final int SET_STATE_ON = 1;
    public static final int SET_AGENT = 2;
    public static final int SET_A = 3;
    public static final int SET_B = 4;

    private final A_B_Object a_b_object = new A_B_Object();
    private ArrayList<A_B_Interface> interfaces = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    for (A_B_Interface a_b_interface : interfaces) {
                        a_b_interface.stateChanged(a_b_object);
                    }
                    break;
                case 1:
                    for (A_B_Interface a_b_interface : interfaces) {
                        a_b_interface.posChanged(a_b_object);
                    }
                    break;
            }
            return true;
        }
    });

    public synchronized void request(int type, int pos, @Nullable A_B_Interface a_b_interface) {
        switch (type) {
            case SET_STATE_OFF:
                a_b_object.state = false;
                handler.sendEmptyMessage(0);
                break;
            case SET_STATE_ON:
                a_b_object.state = true;
                a_b_object.aState = false;
                a_b_object.bState = false;
                handler.sendEmptyMessage(0);
                break;
            case SET_AGENT:
                if (!interfaces.contains(a_b_interface)) {
                    if (a_b_interface != null) {
                        interfaces.add(a_b_interface);
                        a_b_interface.stateChanged(a_b_object);
                        a_b_interface.posChanged(a_b_object);
                    }
                }
                break;
            case SET_A:
                if (a_b_object.state) {
                    if (a_b_object.bState && a_b_object.b < pos) {
                        a_b_object.bState = false;
                    }
                    if (!a_b_object.aState) {
                        a_b_object.aState = true;
                        a_b_object.a = pos;
                    } else {
                        a_b_object.aState = false;
                    }
                    handler.sendEmptyMessage(1);
                }
                break;
            case SET_B:
                if (a_b_object.state) {
                    if (a_b_object.aState && a_b_object.a > pos) {
                        a_b_object.aState = false;
                    }
                    if (!a_b_object.bState) {
                        a_b_object.bState = true;
                        a_b_object.b = pos;
                    } else {
                        a_b_object.bState = false;
                    }
                    handler.sendEmptyMessage(1);
                }
                break;
        }
    }

    public void unregister(A_B_Interface a_b_interface) {
        interfaces.remove(a_b_interface);
    }

    public interface A_B_Interface {

        void stateChanged(A_B_Object a_b_object);

        void posChanged(A_B_Object a_b_object);
    }

    public class A_B_Object {
        private boolean state = false, aState = false, bState = false;
        private int a, b;

        public boolean isState() {
            return state;
        }

        public boolean isaState() {
            return aState;
        }

        public boolean isbState() {
            return bState;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }
    }
}

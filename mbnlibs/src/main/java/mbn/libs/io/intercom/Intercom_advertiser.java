package mbn.libs.io.intercom;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Intercom_advertiser extends Intercom_Base {

    private NsdManager nsdManager;
    private String serviceType = "_http._tcp.";
    private static final String TAG = "INTERCOM_ADVERTISER";
    private boolean isAdvertising = false;

    public Intercom_advertiser(String userNickName, String serviceName, File receivedFilesDir, int processThreads, int sendingThreads, int checkInterval, Context context) throws IOException {
        super(userNickName, serviceName, receivedFilesDir, processThreads, sendingThreads, checkInterval);
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void startAdvertising() {
        if (isAdvertising) {
            return;
        }
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName(getServiceName());
        serviceInfo.setServiceType(serviceType);
        serviceInfo.setPort(getSelf_peer().getServerPort());
        nsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    public boolean isAdvertising() {
        return isAdvertising;
    }

    @Override
    public void stop() {
        super.stop();
        stopAdvertising();
    }

    public void stopAdvertising() {
        if (isAdvertising) {
            nsdManager.unregisterService(registrationListener);
        }
    }

    private final NsdManager.RegistrationListener registrationListener = new NsdManager.RegistrationListener() {

        @Override
        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
            isAdvertising = true;
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
//            serviceName = NsdServiceInfo.getServiceName();
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Registration failed! Put debugging code here to determine why.
            Log.i(TAG, "onRegistrationFailed: " + errorCode);
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo arg0) {
            isAdvertising = false;
            // Service has been unregistered. This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Unregistration failed. Put debugging code here to determine why.
            Log.i(TAG, "onUnregistrationFailed: " + errorCode);
        }
    };

}

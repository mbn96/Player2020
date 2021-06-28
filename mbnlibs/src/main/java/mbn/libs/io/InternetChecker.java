package mbn.libs.io;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

public class InternetChecker extends ConnectivityManager.NetworkCallback {

    private ConnectivityManager connectivityManager;
    private InternetListener listener;
    private boolean hasInternet;

    public InternetChecker(InternetListener listener, @NonNull Context context) {
        this.listener = listener;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        connectivityManager.registerNetworkCallback(builder.build(), this);
        checkForInternet();
    }

    public void shutDown() {
        connectivityManager.unregisterNetworkCallback(this);
    }

    @Override
    public void onAvailable(Network network) {
        checkForInternet();
    }

    @Override
    public void onLost(Network network) {
        checkForInternet();
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        checkForInternet();
    }

    private void checkForInternet() {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            hasInternet = true;
            listener.onInternetAvailable();
        } else {
            hasInternet = false;
            listener.onInternetUnavailable();
        }
    }

    public interface InternetListener {
        void onInternetAvailable();

        void onInternetUnavailable();
    }
}

package org.ministryofhealth.newimci.app;

import android.app.Application;

import org.ministryofhealth.newimci.receiver.ConnectivityReceiver;
import org.ministryofhealth.newimci.util.FontsOverride;

import java.util.Locale;

/**
 * Created by chriz on 10/9/2017.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FontsOverride.setDefaultFont(this, "MONOSPACE", String.format(Locale.US, "fonts/%s", "SourceSansPro-Regular.otf"));
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

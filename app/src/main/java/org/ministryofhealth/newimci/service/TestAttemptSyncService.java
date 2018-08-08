package org.ministryofhealth.newimci.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.ministryofhealth.newimci.syncadapter.TestSyncAdapter;

public class TestAttemptSyncService extends Service {
    private static TestSyncAdapter sSyncAdapter = null;

    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock){
            if (sSyncAdapter == null){
                sSyncAdapter = new TestSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}

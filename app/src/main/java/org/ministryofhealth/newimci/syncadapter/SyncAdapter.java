package org.ministryofhealth.newimci.syncadapter;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.model.UserUsage;
import org.ministryofhealth.newimci.provider.UsageContract;
import org.ministryofhealth.newimci.server.Service.UserUsageService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;
    private static final String TAG = "SYNC_ADAPTER";
    private boolean server_response;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w(TAG, "Starting synchronization...");
        Cursor c = mContentResolver.query(UsageContract.Usages.CONTENT_URI, null, null, null, null);
        assert c != null;
        Log.d(TAG, "Cursor Length: " + c.getCount());

        if(c.moveToFirst()) {
            do {
                syncResult.stats.numEntries++;

                UserUsage usage = new UserUsage();

                usage.setId(c.getInt(c.getColumnIndex(UsageContract.Usages.COL_ID)));
                usage.setUser_id(c.getInt(c.getColumnIndex(UsageContract.Usages.COL_USER_ID)));
                usage.setDevice_id(c.getString(c.getColumnIndex(UsageContract.Usages.COL_DEVICE_ID)));
                usage.setTimestamp(c.getString(c.getColumnIndex(UsageContract.Usages.COL_TIMESTAMP)));
                usage.setType(c.getString(c.getColumnIndex(UsageContract.Usages.COL_TYPE)));



                new UsageAsynTask(usage).execute();

                if (this.server_response) {
                    syncResult.stats.numDeletes++;
                }

            } while (c.moveToNext());

            c.close();

            mContentResolver.notifyChange(UsageContract.Usages.CONTENT_URI, null, false);
        }else{
            Log.e(TAG, "There are no items to sync");
        }
    }

    public static void performSync(){
        Log.d(TAG, "Perform Sync called");
        try {

            Bundle bundle = new Bundle();

            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

            ContentResolver.requestSync(AccountGeneral.getAccount(), UsageContract.CONTENT_AUTHORITY, bundle);
        }catch(Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    private void serverResult(boolean response){
        this.server_response = response;
    }

    @SuppressLint("StaticFieldLeak")
    class UsageAsynTask extends AsyncTask<String, String, Boolean>{
        private UserUsage usage;

        UsageAsynTask(UserUsage usage){
            this.usage = usage;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Preparing Async Task");
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                Log.d(TAG, "Async Task Started for: " + this.usage.getId());
                Retrofit retrofit = RetrofitHelper.getInstance().createHelper();
                UserUsageService client = retrofit.create(UserUsageService.class);
                Call<UserUsage> userUsageCall = client.add(this.usage);
                userUsageCall.execute().body();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Log.d(TAG, "Async Task for: " + this.usage.getId() + " completed successfully!");
                mContentResolver.delete(UsageContract.Usages.CONTENT_URI, UsageContract.Usages.COL_ID + "='" + String.valueOf(this.usage.getId()) + "'", null);
            }else{
                Log.d(TAG, "Async Task for: " + this.usage.getId() + " did not complete successfully!");
            }
            serverResult(aBoolean);
        }
    }
}

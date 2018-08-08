package org.ministryofhealth.newimci.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.model.TestResponse;
import org.ministryofhealth.newimci.model.UserUsage;
import org.ministryofhealth.newimci.provider.TestAttemptContract;
import org.ministryofhealth.newimci.provider.UsageContract;
import org.ministryofhealth.newimci.server.Service.TestAttemptService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestSyncAdapter extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;
    private static final String TAG = "TEST_SYNC_ADAPTER";
    private boolean server_response;
    private DatabaseHandler db;

    public TestSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
        db = new DatabaseHandler(context);
    }

    public TestSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        db = new DatabaseHandler(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w(TAG, "Starting synchronization...");
        Cursor c = mContentResolver.query(TestAttemptContract.TestAttempt.CONTENT_URI, null, TestAttemptContract.TestAttempt.COL_UPLOADED + "=? OR " + TestAttemptContract.TestAttempt.COL_DELETED + "=?", new String[]{String.valueOf(0), String.valueOf(1)}, null);
        assert c != null;
        Log.d(TAG, "Cursor Length: " + c.getCount());
        if(c.moveToFirst()) {
            do {
                syncResult.stats.numEntries++;
                final TestAttempt testAttempt = db.getTestAttemptAttempt(c.getInt(c.getColumnIndex(TestAttemptContract.TestAttempt.COL_ID)));
                List<TestResponse> testResponses = db.getTestResponses(testAttempt.getId());
                testAttempt.setResponses(testResponses);

                Retrofit retrofit = RetrofitHelper.getInstance().createHelper();

                TestAttemptService client = retrofit.create(TestAttemptService.class);
                Call<TestAttempt> testAttemptCall = client.addTestAttempt(testAttempt);

                testAttemptCall.enqueue(new Callback<TestAttempt>() {
                    @Override
                    public void onResponse(Call<TestAttempt> call, Response<TestAttempt> response) {
                        testAttempt.setUploaded(1);

                        db.updateTestAttempt(testAttempt);
                        if (testAttempt.getDeleted() == 1){
                            mContentResolver.delete(TestAttemptContract.TestAttempt.CONTENT_URI, TestAttemptContract.TestAttempt.COL_ID + "=?", new String[]{String.valueOf(testAttempt.getId())});
                        }
                    }

                    @Override
                    public void onFailure(Call<TestAttempt> call, Throwable t) {
                        Log.e(TAG, "There was an error uploading the test");
                    }
                });

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
}

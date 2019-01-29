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
//        Cursor c = mContentResolver.query(TestAttemptContract.TestAttempt.CONTENT_URI, null, TestAttemptContract.TestAttempt.COL_UPLOADED + "=0 OR " + TestAttemptContract.TestAttempt.COL_DELETED + "=1", null, null);
        Cursor c = db.getReadableDatabase().query(TestAttemptContract.TestAttempt.NAME, null, TestAttemptContract.TestAttempt.COL_UPLOADED + "=? OR " + TestAttemptContract.TestAttempt.COL_DELETED + "=?",new String[]{String.valueOf(0), String.valueOf(1)}, null, null, null);
        assert c != null;
        Log.d(TAG, "Cursor Length: " + c.getCount());
        if(c.moveToFirst()) {
            do {
                syncResult.stats.numEntries++;
                final TestAttempt testAttempt = db.getTestAttemptAttempt(c.getInt(c.getColumnIndex(TestAttemptContract.TestAttempt.COL_ID)));
                List<TestResponse> testResponses = db.getTestResponses(testAttempt.getId());
                testAttempt.setResponses(testResponses);

                testAttempt.setTest_started((testAttempt.getTest_started() == null) ? "1995-01-01 00:00:00" : testAttempt.getTest_started());
                testAttempt.setTest_completed((testAttempt.getTest_completed() == null) ? "1995-01-01 00:00:00" : testAttempt.getTest_completed());
                testAttempt.setTest_cancelled((testAttempt.getTest_cancelled() == null) ? "1995-01-01 00:00:00" : testAttempt.getTest_cancelled());
                testAttempt.setQuestions_attempted((testAttempt.getQuestions_attempted() == null) ? String.valueOf(0) : testAttempt.getQuestions_attempted());
                testAttempt.setWrong_answers((testAttempt.getWrong_answers() == null) ? String.valueOf(0) : testAttempt.getWrong_answers());
                testAttempt.setTotal_score((testAttempt.getTotal_score() == null) ? String.valueOf(0) : testAttempt.getTotal_score());

                Retrofit retrofit = RetrofitHelper.getInstance().createHelper();

                TestAttemptService client = retrofit.create(TestAttemptService.class);
                Call<Void> testAttemptCall = client.addTestAttempt(testAttempt);

                testAttemptCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            testAttempt.setUploaded(1);

                            db.updateTestAttempt(testAttempt);
                            if (testAttempt.getDeleted() == 1) {
                                mContentResolver.delete(TestAttemptContract.TestAttempt.CONTENT_URI, TestAttemptContract.TestAttempt.COL_ID + "=?", new String[]{String.valueOf(testAttempt.getId())});
                            }
                        }else{
                            String MyResult = response.raw().message();
                            Log.e(TAG, "There was an error"  + MyResult);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
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

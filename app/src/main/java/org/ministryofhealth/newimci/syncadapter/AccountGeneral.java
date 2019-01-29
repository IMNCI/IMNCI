package org.ministryofhealth.newimci.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.ministryofhealth.newimci.provider.TestAttemptContract;
import org.ministryofhealth.newimci.provider.UsageContract;

public class AccountGeneral {
    public static final String ACCOUNT_TYPE = "org.ministryofhealth.newimci";
    public static final String ACCOUNT_NAME = "IMNCI";

    public static Account getAccount() {
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }

    public static void createSyncAccount(Context c) {
        boolean created = false;

        Account account = getAccount();
        AccountManager manager = (AccountManager)c.getSystemService(Context.ACCOUNT_SERVICE);

        if (manager.addAccountExplicitly(account, null, null)) {
            Log.d("SYNC_ADAPTER", "Creating an account");
            final String AUTHORITY = UsageContract.CONTENT_AUTHORITY;
            final String TEST_ATTEMPT_AUTHORITY = TestAttemptContract.CONTENT_AUTHORITY;
            final long SYNC_FREQUENCY = 60 * 5; // 5 minutes (seconds)

            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, AUTHORITY, 1);
            ContentResolver.setIsSyncable(account, TEST_ATTEMPT_AUTHORITY, 1);

            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
            ContentResolver.setSyncAutomatically(account, TEST_ATTEMPT_AUTHORITY, true);

            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(account, AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            ContentResolver.addPeriodicSync(account, TEST_ATTEMPT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            created = true;
        }else{
            Log.d("SYNC_ADAPTER", "Account already created");
        }

        // Force a sync if the account was just created
        if (created) {
            SyncAdapter.performSync();
        }
    }
}

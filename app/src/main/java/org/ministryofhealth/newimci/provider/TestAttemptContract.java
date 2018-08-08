package org.ministryofhealth.newimci.provider;

import android.net.Uri;

import org.ministryofhealth.newimci.database.DatabaseHandler;

public class TestAttemptContract {
    public static final String CONTENT_AUTHORITY = "org.ministryofhealth.newimci.provider.testattempt";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_TEST_ATTEMPT = "testattempt";

    public static abstract class TestAttempt{
        public static final String NAME = DatabaseHandler.TABLE_TEST_ATTEMPT;
        public static final String COL_ID = DatabaseHandler.KEY_ID;
        public static final String COL_USER_ID = DatabaseHandler.KEY_USER_ID;
        public static final String COL_TEST_STARTED = DatabaseHandler.KEY_TEST_STARTED;
        public static final String COL_TEST_COMPLETED = DatabaseHandler.KEY_TEST_COMPLETED;
        public static final String COL_TEST_CANCELLED = DatabaseHandler.KEY_TEST_CANCELLED;
        public static final String COL_QUESTIONS_ATTEMPTED = DatabaseHandler.KEY_TEST_QUESTIONS_ATTEMPTED;
        public static final String COL_TOTAL_SCORE= DatabaseHandler.KEY_TEST_TOTAL_SCORE;
        public static final String COL_WRONG_ANSWERS = DatabaseHandler.KEY_WRONG_ANSWERS;
        public static final String COL_UPLOADED = DatabaseHandler.KEY_UPLOADED;
        public static final String COL_DELETED = DatabaseHandler.KEY_DELETED;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEST_ATTEMPT).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_TEST_ATTEMPT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_TEST_ATTEMPT;
    }
}

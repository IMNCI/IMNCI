package org.ministryofhealth.newimci.provider;

import android.net.Uri;

import org.ministryofhealth.newimci.database.DatabaseHandler;

public final class UsageContract {
    public static final String CONTENT_AUTHORITY = "org.ministryofhealth.newimci.provider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_USAGE = "userusage";

    public static abstract class Usages{
        public static final String NAME = DatabaseHandler.TABLE_USER_USAGE;
        public static final String COL_ID = DatabaseHandler.KEY_ID;
        public static final String COL_USER_ID = DatabaseHandler.KEY_USER_ID;
        public static final String COL_DEVICE_ID = DatabaseHandler.KEY_DEVICE_ID;
        public static final String COL_TIMESTAMP = DatabaseHandler.KEY_TIMESTAMP;
        public static final String COL_TYPE = DatabaseHandler.KEY_TYPE;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USAGE).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_USAGE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_USAGE;
    }
}

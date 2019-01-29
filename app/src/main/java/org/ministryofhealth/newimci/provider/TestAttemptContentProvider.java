package org.ministryofhealth.newimci.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ministryofhealth.newimci.database.DatabaseHandler;

public class TestAttemptContentProvider extends ContentProvider {
    public static final int TESTATTEMPT = 1;
    public static final int TESTATTEMPT_ID = 2;

    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(UsageContract.CONTENT_AUTHORITY, TestAttemptContract.PATH_TEST_ATTEMPT, TESTATTEMPT);
        uriMatcher.addURI(UsageContract.CONTENT_AUTHORITY, TestAttemptContract.PATH_TEST_ATTEMPT + "/#", TESTATTEMPT_ID);
    }

    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        this.db = DatabaseHandler.getInstance(getContext()).getDb();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor c;
        switch(uriMatcher.match(uri)){
            case TESTATTEMPT:
                c = db.query(TestAttemptContract.TestAttempt.NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TESTATTEMPT_ID:
                long _id = ContentUris.parseId(uri);
                c = db.query(TestAttemptContract.TestAttempt.NAME,
                        projection,
                        TestAttemptContract.TestAttempt.COL_ID + "=?",
                        new String[] { String.valueOf(_id) },
                        null,
                        null,
                        sortOrder);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");

        }
        assert getContext() != null;
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case TESTATTEMPT:
                return TestAttemptContract.TestAttempt.CONTENT_TYPE;
            case TESTATTEMPT_ID:
                return TestAttemptContract.TestAttempt.CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("Invalid URI!");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        long _id;

        switch (uriMatcher.match(uri)) {
            case TESTATTEMPT:
                _id = db.insert(TestAttemptContract.TestAttempt.NAME, null, values);
                returnUri = ContentUris.withAppendedId(UsageContract.Usages.CONTENT_URI, _id);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }
        assert getContext() != null;
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
            case TESTATTEMPT:
                rows = db.delete(TestAttemptContract.TestAttempt.NAME, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rows;
        switch (uriMatcher.match(uri)) {
            case TESTATTEMPT:
                rows = db.update(TestAttemptContract.TestAttempt.NAME, values, selection, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Invalid URI!");
        }

        // Notify any observers to update the UI
        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}

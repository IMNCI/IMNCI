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
import android.util.Log;

import org.ministryofhealth.newimci.database.DatabaseHandler;

public class UsageContentProvider extends ContentProvider {
    public static final int USERUSAGE = 1;
    public static final int USERUSAGE_ID = 2;

    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(UsageContract.CONTENT_AUTHORITY, UsageContract.PATH_USAGE, USERUSAGE);
        uriMatcher.addURI(UsageContract.CONTENT_AUTHORITY, UsageContract.PATH_USAGE + "/#", USERUSAGE_ID);
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
            case USERUSAGE:
                c = db.query(UsageContract.Usages.NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USERUSAGE_ID:
                long _id = ContentUris.parseId(uri);
                c = db.query(UsageContract.Usages.NAME,
                        projection,
                        UsageContract.Usages.COL_ID + "=?",
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
            case USERUSAGE:
                return UsageContract.Usages.CONTENT_TYPE;
            case USERUSAGE_ID:
                return UsageContract.Usages.CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("Invalid URI!");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        long _id;
        switch (uriMatcher.match(uri)) {
            case USERUSAGE:
                _id = db.insert(UsageContract.Usages.NAME, null, values);
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
            case USERUSAGE:
                rows = db.delete(UsageContract.Usages.NAME, selection, selectionArgs);
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
            case USERUSAGE:
                rows = db.update(UsageContract.Usages.NAME, values, selection, selectionArgs);
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

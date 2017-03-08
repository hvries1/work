package com.example.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import static com.example.hadev.myfrontend.main.MainActivity.TAG;

/**
 * Created from example on 8-2-2017.
 */
public class ContactProvider extends ContentProvider {
    static final String PROVIDER_NAME = "ContactProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/contacts";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";

    private static HashMap<String, String> CONTACTS_PROJECTION_MAP;

    public static final int CONTACTS = 1;
    public static final int CONTACT_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "contacts", CONTACTS);
        uriMatcher.addURI(PROVIDER_NAME, "contacts/id", CONTACT_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "MyFrontend";
    static final String CONTACTS_TABLE_NAME = "contacts";
    static final int DATABASE_VERSION = 3;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + CONTACTS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " phone TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
            Log.i(TAG, "Table " + CONTACTS_TABLE_NAME + " created.");

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(NAME, "dummy");
            values.put(PHONE, "0123456789");
            db.insert(CONTACTS_TABLE_NAME, "", values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null ? false : true);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new contact record
         */
        long rowID = db.insert(CONTACTS_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CONTACTS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                qb.setProjectionMap(CONTACTS_PROJECTION_MAP);
                break;

            case CONTACT_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
        }

        if (sortOrder == null || sortOrder == "") {
            //By default sort on contact names
            sortOrder = NAME;
        }
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        //register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                count = db.delete(CONTACTS_TABLE_NAME, selection, selectionArgs);
                break;

            case CONTACT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(CONTACTS_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                count = db.update(CONTACTS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case CONTACT_ID:
                count = db.update(CONTACTS_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all contact records
             */
            case CONTACTS:
                return "vnd.android.cursor.dir/vnd.example.contacts";

            /**
             * Get a particular contact
             */
            case CONTACT_ID:
                return "vnd.android.cursor.item/vnd.example.contacts";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
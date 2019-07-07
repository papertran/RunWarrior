package edu.fsu.cs.runwarrior;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RWContentProvider extends ContentProvider {

    private String TAG = RWContentProvider.class.getCanonicalName();
    public static final int DBVERSION = 1;

    public final static String DBNAME = "RunWarriorDB";
    public final static String TABLE_NAMESTABLE = "userSesions";

    // Columns of the Database
    public final static String RUN_SESSION = "RUN_SESSION";
    public final static String DISTANCE_RAN = "DISTANCE_RAN";
    public final static String TIME_ELAPSED = "TIME_ELAPSED";
    public final static String EXP_EARNED = "EXP_EARNED";
    public final static String DATE = "DATE";



    // From L10 ContentProvider
    private static final String SQL_CREATE_MAIN =
            "CREATE TABLE " + TABLE_NAMESTABLE + "( "+
                    "_ID INTEGER PRIMARY KEY, " +
                    RUN_SESSION +" INTEGER, " +
                    DISTANCE_RAN + " FLOAT, " +
                    TIME_ELAPSED + " TIME, " +
                    EXP_EARNED + " INTEGER, " +
                    DATE + " DATE )";

    private static final String authority = "edu.fsu.cs.runwarrior";
    public static final Uri CONTENT_URI = Uri.parse("content://" + authority);


    // Using Helper from L10
    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context){
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
    private MainDatabaseHelper mOpenHelper;



    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = mOpenHelper
                .getWritableDatabase()
                .insert(TABLE_NAMESTABLE, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        return mOpenHelper
                .getWritableDatabase()
                .update(TABLE_NAMESTABLE, values, selection, selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return mOpenHelper
                .getWritableDatabase()
                .delete(TABLE_NAMESTABLE, selection, selectionArgs);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mOpenHelper
                .getReadableDatabase()
                .query(TABLE_NAMESTABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}

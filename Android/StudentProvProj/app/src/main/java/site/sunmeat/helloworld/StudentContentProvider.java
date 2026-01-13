package site.sunmeat.helloworld;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class StudentContentProvider extends ContentProvider {

    public static final String AUTHORITY =
            "site.sunmeat.helloworld.provider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/students");

    private AppDatabase db;

    @Override
    public boolean onCreate() {
        db = AppDatabase.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        return db.studentDao().getAllStudentsBlocking();
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/students";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        return 0;
    }
}

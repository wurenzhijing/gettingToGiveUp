package com.example.administrator.contentprovicer;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class MyProvider extends ContentProvider {

    MyDBHelper myDBHelper ;
    SQLiteDatabase db;

    private static final UriMatcher sMatcher ;

    static{
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(BookU.AUTOHORITY,BookU.TABLE_NAME, BookU.ITEM);
        sMatcher.addURI(BookU.AUTOHORITY, BookU.TABLE_NAME+"/#", BookU.ITEM_ID);

    }

    @Override
    public boolean onCreate() {
        this.myDBHelper = new MyDBHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        db = myDBHelper.getWritableDatabase();
        Cursor cursor;
        Log.d("-------", String.valueOf(sMatcher.match(uri)));
        switch (sMatcher.match(uri)) {
            case BookU.ITEM:
                cursor = db.query(BookU.TABLE_NAME, strings, s, strings1, null, null, null);

                break;
            case BookU.ITEM_ID:
                String name = uri.getPathSegments().get(1);
                cursor = db.query(BookU.TABLE_NAME, strings, BookU.NAME+"="+name+(!TextUtils.isEmpty(s)?"AND("+s+')':""),strings1, null, null, s1);
                break;
            default:
                Log.d("!!!!!!", "Unknown URI"+uri);
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case BookU.ITEM:
                return BookU.CONTENT_TYPE;
            case BookU.ITEM_ID:
                return BookU.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = myDBHelper.getWritableDatabase();
        long rowId;
        if(sMatcher.match(uri)!=BookU.ITEM){
            throw new IllegalArgumentException("Unknown URI"+uri);
        }
        rowId = db.insert(BookU.TABLE_NAME,BookU.NAME,contentValues);
        if(rowId>0){
            Uri noteUri= ContentUris.withAppendedId(BookU.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new IllegalArgumentException("Unknown URI"+uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        db = myDBHelper.getWritableDatabase();
        int count = 0 ;
        switch (sMatcher.match(uri)) {
            case BookU.ITEM:
                count = db.delete(BookU.TABLE_NAME,s, strings);
                break;
            case BookU.ITEM_ID:
                String name = uri.getPathSegments().get(1);
                count = db.delete(BookU.NAME, BookU.NAME+"="+name+(!TextUtils.isEmpty(BookU.NAME="?")?"AND(" + s + ')':""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}

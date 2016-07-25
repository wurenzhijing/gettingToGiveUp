package com.example.administrator.loaderlearning;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.PeriodicSync;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class PeopleContentProvider extends ContentProvider {

    MySQLiteHelper mySQLiteHelper;
    SQLiteDatabase db ;

    static final Uri CONTENT_URI = Uri.parse("content://com.example.administrator.loaderlearning.PeopleContentProvider/people");

    static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static final int PERSON = 1; // 操作单行记录
    static final int PERSONS = 2; // 操作多行记录
    static{
        URI_MATCHER.addURI("com.example.administrator.loaderlearning.PeopleContentProvider","people",PERSONS);

        URI_MATCHER.addURI("com.example.administrator.loaderlearning.PeopleContentProvider","people/#",PERSON);
    }

    public PeopleContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db =mySQLiteHelper.getWritableDatabase();
        int count = 0;
        switch (URI_MATCHER.match(uri)){
            case PERSON:
                count = db.delete(MySQLiteHelper.TABLE_NAME,selection,selectionArgs);
                break;
            case PERSONS:
                String name = uri.getPathSegments().get(1);
                count = db.delete(MySQLiteHelper.TABLE_NAME,"name="+name+(!TextUtils.isEmpty("name=?")?"AND("+selection+')':""),selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)){
            case PERSON:
                return "vnd.android.cursor.item/people";
            case PERSONS:
                return "vnd.android.cursor.dir/people";
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mySQLiteHelper.getWritableDatabase();
        long rowId;

        if (URI_MATCHER.match(uri)!=PERSON){
            throw new IllegalArgumentException("Unknown URI"+uri);
        }
        rowId = db.insert(MySQLiteHelper.TABLE_NAME,"name",values);
        if (rowId>0){
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new IllegalArgumentException("Unknown URI"+uri);
    }

    @Override
    public boolean onCreate() {

        this.mySQLiteHelper = new MySQLiteHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        db = mySQLiteHelper.getWritableDatabase();
        Cursor cursor = null;

        int flag = URI_MATCHER.match(uri);
        switch (flag){
            case PERSON:
                cursor = db.query(MySQLiteHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            case PERSONS:
                String name = uri.getPathSegments().get(0);
                Log.i("URI--------",uri.toString());
                Log.i("URI--------",name);
                cursor = db.query(MySQLiteHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);//"name="+name+(!TextUtils.isEmpty(selection)?"AND("+selection+')':"")


                Log.i("URI",uri.toString());

                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = -1;
        db = mySQLiteHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)){
            case PERSON:
                count = db.update(MySQLiteHelper.TABLE_NAME,values,selection,selectionArgs);
                return count;

            case PERSONS:
                count = db.update(MySQLiteHelper.TABLE_NAME,values,selection,selectionArgs);
                return count;
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Log.i(this.toString(), "--->>" + method);
        Bundle bundle = new Bundle();
        bundle.putString("returnCall", "call被执行了");
        return bundle;
    }
}

package com.example.administrator.testLearning.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.administrator.testLearning.sqlite.BookSQLiteHelper;

public class BookContentProvider extends ContentProvider {
    Context context ;
    static final int MATCH_ITEM = 1;
    static final int MATCH_ITEM_NAME = 2 ;
    static final String AUTHORITIE = "com.example.administrator.testLearning.provider" ;

    static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH) ;
    static {
        MATCHER.addURI(AUTHORITIE , BookSQLiteHelper.TABLE_NAME , MATCH_ITEM);
        MATCHER.addURI(AUTHORITIE , BookSQLiteHelper.TABLE_NAME + "/#" , MATCH_ITEM_NAME);
    }

    public BookContentProvider(Context context) {
        this.context = context ;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = new BookSQLiteHelper(context).getWritableDatabase();
        String isbn = uri.getPathSegments().get(1);
        return db.delete(BookSQLiteHelper.TABLE_NAME , BookSQLiteHelper.BOOK_ISBN +"=?" , new String[]{isbn});
    }

    @Override
    public String getType(Uri uri) {
        return null ;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = new BookSQLiteHelper(context).getWritableDatabase();

        long result = db.insert(BookSQLiteHelper.TABLE_NAME , null ,values);
        Uri insertUri = ContentUris.withAppendedId(uri , result) ;
        return insertUri ;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch(MATCHER.match(uri)){
            case MATCH_ITEM:
                SQLiteDatabase db = new BookSQLiteHelper(context).getWritableDatabase();
                Cursor cursor = db.query(BookSQLiteHelper.TABLE_NAME , projection ,selection , selectionArgs , null , null , sortOrder);
                return cursor ;
            case MATCH_ITEM_NAME:
                SQLiteDatabase db1 = new BookSQLiteHelper(context).getWritableDatabase();
                String name = uri.getPathSegments().get(1);
                Cursor cursor1 = db1.query(BookSQLiteHelper.TABLE_NAME , projection , BookSQLiteHelper.BOOK_ISBN + "=?", new String[]{name} , null , null , sortOrder);
                return cursor1 ;
            default:
                Log.i(this.toString() , "cannot match") ;
                break;
        }
        return  null ;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = new BookSQLiteHelper(context).getWritableDatabase();
        int result = db.update(BookSQLiteHelper.TABLE_NAME , values , selection , selectionArgs) ;
        return  result ;
    }
}

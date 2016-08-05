/**
 * User: wurenzhijing
 * Date: 2016-08-04
 * Time: 15:09
 * FIXME
 */
package com.example.administrator.testLearning.sqlite;

import android.app.backup.BackupHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.ViewGroup;

import com.example.administrator.testLearning.MyApplication;

public class BookSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "book.db" ;
    public static final String TABLE_NAME = "book" ;
    public static final String BOOK_NAME = "name" ;
    public static final String BOOK_AUTHOR = "author" ;
    public static final String BOOK_ISBN = "isbn" ;
    public static final String BOOK_PRICE = "price" ;

    static BookSQLiteHelper bookSQLiteHelper ;


    static final int VERSION = 1 ;

    public BookSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, VERSION);
    }
    public BookSQLiteHelper(Context context){
        super(context, DB_NAME , null , VERSION);
    }

//    public static BookSQLiteHelper getInstance() {
//        if (null == bookSQLiteHelper) {
//            bookSQLiteHelper = new BookSQLiteHelper(MyApplication.getmContext(), BookSQLiteHelper.DB_NAME, null, BookSQLiteHelper.VERSION);
//        }
//        return bookSQLiteHelper;
//    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table book ("+
                "_id" + " integer primary key autoincrement,"+
                BOOK_NAME +" varchar ,"+
                BOOK_AUTHOR +" varchar,"+
                BOOK_ISBN +" varchar,"+
                BOOK_PRICE +" double)";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}


package com.example.administrator.contentprovicer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    static final String DATEBASE_NAME = "book.db";
    static final String TABLE_NAME = "book";
    static final int version = 1;


    public MyDBHelper(Context context) {
        super(context, BookU.DATEBASE_NAME, null,version );
        // TODO Auto-generated constructor stub
    }
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE book (name varchar(20) primary key not null,author varchar(20),isbn varchar(20),price varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS book");
        onCreate(sqLiteDatabase);
    }
}

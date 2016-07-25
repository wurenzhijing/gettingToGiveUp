package com.example.administrator.sqlitelearning;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "students.db" ;
    static final String TABLE_NAME = "student" ;
    static final  int VERSION = 1 ;

    public MySQLiteHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table student (" +
                "_id integer primary key autoincrement," +
                "name varchar(10)," +
                "gender varchar(5)," +
                "class varchar(5)," +
                "score integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS student");
        onCreate(sqLiteDatabase);
    }
}

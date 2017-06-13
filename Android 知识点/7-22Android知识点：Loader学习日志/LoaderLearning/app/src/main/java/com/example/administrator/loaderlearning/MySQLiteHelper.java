package com.example.administrator.loaderlearning;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    static final int VERSION = 5;
    static final String DB_NAME = "people.db";
    static final String TABLE_NAME = "people";

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null,VERSION );
        // TODO Auto-generated constructor stub
    }

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table people  (" +
               "_id integer primary key autoincrement,"+
                "name varchar(20) ," +
                "gender varchar(10)," +
                "age integer," +
                "height double," +
                "weight double," +
                "number varchar(20))" );

//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ("
//                + "_id" + " INTEGER PRIMARY KEY,"
//                +"df" + " TEXT,"
//                + "df3" + " TEXT"
//                + ");");

        Log.i("DB——Table 创建成功",sqLiteDatabase.getSyncedTables().toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS people");
        onCreate(sqLiteDatabase);
    }
}

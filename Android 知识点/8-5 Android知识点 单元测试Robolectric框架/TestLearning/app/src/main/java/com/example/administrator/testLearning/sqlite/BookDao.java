/**
 * User: wurenzhijing
 * Date: 2016-08-04
 * Time: 15:30
 * FIXME
 */
package com.example.administrator.testLearning.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BookDao {

    Context context ;

    public BookDao(Context context){
        this.context = context ;
    }




    public  long save(Book book){
        Log.i("_________", context+"");
        BookSQLiteHelper bookSQLiteHelper = new BookSQLiteHelper(context) ;

        SQLiteDatabase db = bookSQLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());

        long a  = db.insert(BookSQLiteHelper.TABLE_NAME ,null , contentValues) ;
        db.close();

        return  a ;
    }

    public  int update (Book book){
        BookSQLiteHelper bookSQLiteHelper = new BookSQLiteHelper(context) ;
         SQLiteDatabase db = bookSQLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());

        int a = db.update(BookSQLiteHelper.TABLE_NAME , contentValues , BookSQLiteHelper.BOOK_NAME+"=?" , new String[]{book.getName()} );
        db.close();
        Log.i("-----",book.getName()+" "+book.getAuthor()+" "+book.getIsbn()+ " "+book.getPrice());

        return  a ;
    }

    public   Book query(String name){
        BookSQLiteHelper bookSQLiteHelper = new BookSQLiteHelper(context) ;
         SQLiteDatabase db = bookSQLiteHelper.getWritableDatabase() ;
        Book book = null ;
        Cursor cursor = db.query(BookSQLiteHelper.TABLE_NAME , null ,BookSQLiteHelper.BOOK_NAME+"=?" , new String[]{name} , null , null , null) ;
        if (null != cursor && cursor.moveToFirst()){
            book = new Book();
            book.name = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_NAME)) ;
            book.author = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_AUTHOR)) ;
            book.isbn = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_ISBN)) ;
            book.price = cursor.getDouble(cursor.getColumnIndex(BookSQLiteHelper.BOOK_PRICE)) ;
        }
        cursor.close();
        db.close();
        return book ;
    }

    public   List<Book> getAll(){
         List<Book> list = new ArrayList<>() ;
        BookSQLiteHelper bookSQLiteHelper = new BookSQLiteHelper(context) ;
        SQLiteDatabase db = bookSQLiteHelper.getWritableDatabase() ;
        Cursor cursor = db.query(BookSQLiteHelper.TABLE_NAME , null ,null ,null , null , null , null);
        if (cursor != null){
             while (cursor.moveToNext()){
                 Book book = new Book() ;
                 book.name = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_NAME)) ;
                 book.author = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_AUTHOR)) ;
                 book.isbn = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_ISBN)) ;
                 book.price = cursor.getDouble(cursor.getColumnIndex(BookSQLiteHelper.BOOK_PRICE)) ;
                 list.add(book);
             }
        }
        return list ;
    }

    public   int delete(String name){
        BookSQLiteHelper bookSQLiteHelper = new BookSQLiteHelper(context) ;
         SQLiteDatabase db = bookSQLiteHelper.getWritableDatabase() ;
        int a = db.delete(BookSQLiteHelper.TABLE_NAME , BookSQLiteHelper.BOOK_NAME+"=?" , new String[]{name}) ;
        return  a ;
    }


}  


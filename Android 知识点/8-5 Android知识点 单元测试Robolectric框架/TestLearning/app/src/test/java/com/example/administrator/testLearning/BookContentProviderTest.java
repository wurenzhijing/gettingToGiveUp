/**
 * User: wurenzhijing
 * Date: 2016-08-05
 * Time: 10:35
 * FIXME
 */
package com.example.administrator.testLearning;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


import com.example.administrator.testLearning.provider.BookContentProvider;
import com.example.administrator.testLearning.sqlite.Book;
import com.example.administrator.testLearning.sqlite.BookSQLiteHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowLog;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class ,sdk =18)
public class BookContentProviderTest {

    ContentResolver mContentResolver ;
    ShadowContentResolver mShadowContentResolver ;
    BookContentProvider mProvider ;
    String AUTHORITY = "com.example.administrator.testLearning.provider" ;
    Uri URI_INFO = Uri.parse("content://"+ AUTHORITY +"/"+ BookSQLiteHelper.TABLE_NAME) ;


    @Before
    public void setUp(){
        ShadowLog.stream = System.out ;
        Application application = RuntimeEnvironment.application ;
        mProvider = new BookContentProvider(application) ;

        mContentResolver = application.getContentResolver() ;
        // 创建ContentProvider的  shadow 对象
        mShadowContentResolver = Shadows.shadowOf(mContentResolver) ;
        mProvider.onCreate() ;
        // 注册 ContentProvider对象 和 Uri
        ShadowContentResolver.registerProvider(AUTHORITY , mProvider);
    }

    @After
    public void tearDown(){
        Field instance;
        try {
            instance = BookSQLiteHelper.class.getDeclaredField("bookSQLiteHelper");
            Log.d("------",instance+"");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void query(){
        Book book = new Book();
        book.setName("android");
        book.setAuthor("heibn");
        book.setIsbn("8054335");
        book.setPrice(78.00);
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());

        mShadowContentResolver.insert(URI_INFO , contentValues) ;

        Book book1 = new Book();
        book1.setName("androidstudio");
        book1.setAuthor("heibin");
        book1.setIsbn("65465");
        book1.setPrice(999.00);
        ContentValues cv = new ContentValues() ;
        cv.put(BookSQLiteHelper.BOOK_NAME , book1.getName());
        cv.put(BookSQLiteHelper.BOOK_AUTHOR , book1.getAuthor());
        cv.put(BookSQLiteHelper.BOOK_ISBN ,book1.getIsbn());
        cv.put(BookSQLiteHelper.BOOK_PRICE, book1.getPrice());

        mShadowContentResolver.insert(URI_INFO , cv) ;
        //查询所有数据
        Cursor cursor = mShadowContentResolver.query(URI_INFO , null ,null ,null ,null);
        assertEquals(cursor.getCount() ,2) ;

        // 查询ISBN为 8054335 的数据
        Uri uri = ContentUris.withAppendedId(URI_INFO , 8054335) ;
        Cursor cursor1 = mShadowContentResolver.query(uri , null , null , null , null);
        assertEquals(cursor1.getCount() , 1);

        cursor.close();
        cursor1.close();
    }

    @Test
    public void insert(){
        Book book = new Book();
        book.setName("android");
        book.setAuthor("heibn");
        book.setIsbn("8054335");
        book.setPrice(78.00);
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());
        mShadowContentResolver.insert(URI_INFO , contentValues) ;

        Cursor cursor = mShadowContentResolver.query(URI_INFO , null ,BookSQLiteHelper.BOOK_ISBN + "=?" ,new String[]{"8054335"} , null );
        assertEquals(cursor.getCount() , 1 );
        cursor.close();
    }

    @Test
    public void updata(){
        Book book = new Book();
        book.setName("android");
        book.setAuthor("heibn");
        book.setIsbn("8054335");
        book.setPrice(78.00);
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());
        Uri uri = mShadowContentResolver.insert(URI_INFO , contentValues) ;

        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , "hhhhhh");
        int result = mShadowContentResolver.update( uri , contentValues ,null ,null) ;
        assertEquals(result ,1 );

        Cursor cursor = mShadowContentResolver.query(URI_INFO ,null ,BookSQLiteHelper.BOOK_ISBN +"=?" , new String[]{"8054335"} , null) ;
        cursor.moveToFirst() ;
        String author = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_AUTHOR));
        assertEquals(author , "hhhhhh");
        cursor.close();
    }

    @Test
    public void delete(){

        Book book = new Book();
        book.setName("android");
        book.setAuthor("heibn");
        book.setIsbn("8054335");
        book.setPrice(78.00);
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());

        mShadowContentResolver.insert(URI_INFO , contentValues) ;


        Uri uri = ContentUris.withAppendedId(URI_INFO , 8054335) ;
        mShadowContentResolver.delete(uri , null , null) ;

        Uri uri1 = ContentUris.withAppendedId(URI_INFO , 8054335) ;
        Cursor cursor1 = mShadowContentResolver.query(uri1 , null , null , null , null);
        assertEquals(cursor1.getCount() , 0);
//        try{
//
//            fail("Exeption");
//        }catch (Exception e){
//            e.printStackTrace();
//            assertEquals(e.getMessage() , "Delete not supported");
//        }
    }


}  


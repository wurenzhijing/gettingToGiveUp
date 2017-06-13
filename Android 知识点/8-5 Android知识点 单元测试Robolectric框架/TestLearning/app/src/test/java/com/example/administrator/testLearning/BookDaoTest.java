/**
 * User: wurenzhijing
 * Date: 2016-08-04
 * Time: 16:30
 * FIXME
 */
package com.example.administrator.testLearning;

import android.app.Application;
import android.content.Context;

import com.example.administrator.testLearning.sqlite.Book;
import com.example.administrator.testLearning.sqlite.BookDao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class ,sdk =18)
public class BookDaoTest {

    Application application = RuntimeEnvironment.application ;
    Context context = application.getApplicationContext() ;

    @After
    public void tearDown(){

    }
    @Test
    public void save(){
        Book book = new Book();
        book.setName("android");
        book.setAuthor("heibn");
        book.setIsbn("80543_35");
        book.setPrice(78.00);
        long resault = new BookDao(context).save(book) ;
        assertTrue(resault>0) ;
    }

    @Test
    public void update(){
        Book book = new Book();
        book.setName("androidstudio");
        book.setAuthor("heibin");
        book.setIsbn("8054d3_35");
        book.setPrice(748.00);
        new BookDao(context).save(book);

        Book book1 = new Book();
        book1.setName("androidstudio");
        book1.setAuthor("heibin");
        book1.setIsbn("8054d3_35");
        book1.setPrice(999.00);
        long result = new BookDao(context).update(book1);

        assertEquals(result , 1);

        Book newbook = new BookDao(context).query("androidstudio");
        assertEquals((int)newbook.getPrice()+"" , (int)(999.00)+"");
    }

    @Test
    public void getAll(){
        Book book = new Book();
        book.setName("androidstudio");
        book.setAuthor("heibin");
        book.setIsbn("8054d3_35");
        book.setPrice(748.00);
        new BookDao(context).save(book);

        Book book1 = new Book();
        book1.setName("androidstudio");
        book1.setAuthor("heibin");
        book1.setIsbn("8054d3_35");
        book1.setPrice(999.00);
        new BookDao(context).save(book1);

        Book book2 = new Book();
        book2.setName("androidstudio1.0");
        book2.setAuthor("heijbin");
        book2.setIsbn("805e454d3_35");
        book2.setPrice(666.00);
        new BookDao(context).save(book2);

        List<Book> list = new BookDao(context).getAll() ;
        assertEquals(list.size() , 3);
    }

    @Test
    public void delete(){
        Book book = new Book();
        book.setName("androidstudio");
        book.setAuthor("heibin");
        book.setIsbn("8054d3_35");
        book.setPrice(748.00);
        new BookDao(context).save(book);

        int result = new BookDao(context).delete("androidstudio");
        assertTrue(result>0);
    }

}  


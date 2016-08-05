/**
 * User: wurenzhijing
 * Date: 2016-08-04
 * Time: 15:07
 * FIXME
 */
package com.example.administrator.testLearning.sqlite;

public class Book {
    String name ;
    String author ;
    String isbn;
    double price ;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}


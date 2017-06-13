package com.example.administrator.testLearning;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class SerializableBook implements Serializable {
    String name ;
    String author ;
    String ISBN;
    String price;

    public SerializableBook(String name , String author, String ISBN, String price){
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}

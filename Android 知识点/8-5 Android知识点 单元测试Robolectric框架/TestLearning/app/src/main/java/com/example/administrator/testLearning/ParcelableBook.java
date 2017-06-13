package com.example.administrator.testLearning;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class ParcelableBook implements Parcelable {

    String name ;
    String author ;
    String ISBN;
    String price;

    public static final ParcelableBook.Creator<ParcelableBook> CREATOR = new Creator<ParcelableBook>() {
        @Override
        public ParcelableBook createFromParcel(Parcel parcel) {
            ParcelableBook book = new ParcelableBook();
            book.name = parcel.readString();
            book.author = parcel.readString();
            book.ISBN = parcel.readString();
            book.price = parcel.readString();
            return book;
        }

        @Override
        public ParcelableBook[] newArray(int i) {
            return new ParcelableBook[i];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(author);
        parcel.writeString(ISBN);
        parcel.writeString(price);
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

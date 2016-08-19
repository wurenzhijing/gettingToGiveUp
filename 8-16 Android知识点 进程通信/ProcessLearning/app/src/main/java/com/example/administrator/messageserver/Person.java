/**
 * User: wurenzhijing
 * Date: 2016-08-08
 * Time: 09:50
 * FIXME
 */
package com.example.administrator.messageserver;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    String name ;
    String gender ;
    int age ;

    public Person(String name , String gender , int age ){
        this.name = name ;
        this.gender = gender ;
        this.age = age ;
    }
    public Person(){

    }

    protected Person(Parcel in) {
        this.name = in.readString();
        this.gender = in.readString() ;
        this.age = in.readInt() ;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeInt(age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person: "+name+" "+gender+" "+age;
    }
}


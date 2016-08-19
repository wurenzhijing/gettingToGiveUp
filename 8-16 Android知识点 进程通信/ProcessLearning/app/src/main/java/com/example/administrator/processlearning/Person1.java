/**
 * User: wurenzhijing
 * Date: 2016-08-08
 * Time: 09:50
 * FIXME
 */
package com.example.administrator.processlearning;

import android.os.Parcel;
import android.os.Parcelable;

public class Person1 implements Parcelable {

    String name ;
    String gender ;
    int age ;

    public Person1(String name , String gender , int age ){
        this.name = name ;
        this.gender = gender ;
        this.age = age ;
    }
    public Person1(){

    }

    protected Person1(Parcel in) {
        this.name = in.readString();
        this.gender = in.readString() ;
        this.age = in.readInt() ;
    }

    public static final Creator<Person1> CREATOR = new Creator<Person1>() {
        @Override
        public Person1 createFromParcel(Parcel in) {
            return new Person1(in);
        }

        @Override
        public Person1[] newArray(int size) {
            return new Person1[size];
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
        return "Person1: "+name+" "+gender+" "+age;
    }
}


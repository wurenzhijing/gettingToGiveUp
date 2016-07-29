package com.example.administrator.parcelablelearning;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SerializablePeople implements Serializable {
    String name ;
    String gender ;
    int age ;
    static final long serialVersionUID = 90090990 ;

    public SerializablePeople(String name , String gender , int  age  ){
        this.name = name ;
        this.gender = gender ;
        this.age =age ;
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

}

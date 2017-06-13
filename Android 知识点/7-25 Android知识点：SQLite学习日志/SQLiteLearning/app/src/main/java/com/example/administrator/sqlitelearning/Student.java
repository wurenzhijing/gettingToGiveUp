package com.example.administrator.sqlitelearning;

import javax.sql.StatementEvent;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class Student {
    String name ;
    String gender ;
    String classes ;
    int  score ;

    public Student(String name , String gender , String classes , int score){
        this.name = name ;
        this.gender = gender ;
        this.classes = classes ;
        this.score = score ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getClasses() {
        return classes;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

}

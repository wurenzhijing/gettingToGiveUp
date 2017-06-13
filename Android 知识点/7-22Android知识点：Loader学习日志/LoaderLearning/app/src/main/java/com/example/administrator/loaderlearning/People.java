package com.example.administrator.loaderlearning;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class People{
    String name;
    String gender;
    int age;
    double height;
    double weight;
    String number;

    public People(String name,String gender,int age,double height,double weight,String number){
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.number = number;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getGender(){
        return gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }

    public  int getAge(){
        return age;
    }
    public void setAge(int age){
        this.age = age;
    }

    public double getHeight(){
        return height;
    }
    public void setHeight(double height){
        this.height = height ;
    }

    public double getWeight(){
        return weight;
    }
    public void setWeight(double weight){
        this.weight = weight;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

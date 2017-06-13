package com.example.administrator.parcelablelearning;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class ParcelablePeople implements Parcelable {
    String name ;
    String gender ;
    int  age ;

    public ParcelablePeople(String name , String gender , int  age  ){
        this.name = name ;
        this.gender = gender ;
        this.age =age ;
    }

    public ParcelablePeople(Parcel source){
        name = source.readString();
        gender = source.readString();
        age = source.readInt() ;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeInt(age);
    }

    public final static Parcelable.Creator<ParcelablePeople> CREATOR = new Parcelable.Creator<ParcelablePeople>() {


        @Override
        public ParcelablePeople createFromParcel(Parcel parcel) {
            return new ParcelablePeople(parcel);
        }

        @Override
        public ParcelablePeople[] newArray(int i) {
            return new ParcelablePeople[i];
        }
    };
}

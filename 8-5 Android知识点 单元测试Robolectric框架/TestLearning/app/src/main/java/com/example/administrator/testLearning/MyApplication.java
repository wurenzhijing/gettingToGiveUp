/**
 * User: wurenzhijing
 * Date: 2016-08-04
 * Time: 17:32
 * FIXME
 */
package com.example.administrator.testLearning;

import android.app.Application;

public class MyApplication extends Application {
    static MyApplication mContext ;

    public static MyApplication getmContext(){
        return mContext ;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this ;
    }
}


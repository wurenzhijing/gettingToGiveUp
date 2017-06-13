package com.example.administrator.testLearning;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class MyService extends Service {

    public class MyLocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }

    String message ;

    final MyLocalBinder myLocalBinder = new MyLocalBinder();
    NotificationManager mNF;

    @Override
    public void onCreate(){
        Log.i(this.toString(),"onCreate()_______--");
        super.onCreate();

        mNF = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        showNotifocation();
    }

    private void showNotifocation() {
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("MyService")
                .setContentText("intent启动Service"+"  "+message)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        mNF.notify(0,notification);
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int id){
        Log.i(this.toString(),"onStart()_______--");
        message = intent.getStringExtra("message");

        SharedPreferences.Editor editor = this.getSharedPreferences("message" , Context.MODE_PRIVATE).edit() ;
        editor.putString("content" , message);
        editor.commit() ;

//        showNotifocation();
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalBinder;
    }
}

package com.example.administrator.servicelearning;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MyLocalService extends Service {

    private NotificationManager mNF;

    public static String NOTIFICATION ="Service Start";
    public static final String TAG = "MyLocalService";


    private final IBinder mBinder = new LocalBinder() ;

    public class LocalBinder extends Binder{
        MyLocalService getService(){
            return MyLocalService.this;
        }

        public String getState(){
            String str = "Service has connencted";
            return str;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG , "[Service] onCreate...");
        super.onCreate();
        mNF = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        showNotification();
    }

    @Override
    public void onStart(Intent intent,int startId){
        Log.i(TAG , "[Service] onStart...");
        super.onStart(intent,startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG , "[Service] onBind...");
        return mBinder;
    }

    @Override
    public void onDestroy(){
        Log.i(TAG , "[Service] onDestroy...");
        super.onDestroy();

    }


    private void showNotification() {
        CharSequence text = "Service Started";


        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(text)
                .build();

        notification.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;

        mNF.notify(0,notification);

    }



}

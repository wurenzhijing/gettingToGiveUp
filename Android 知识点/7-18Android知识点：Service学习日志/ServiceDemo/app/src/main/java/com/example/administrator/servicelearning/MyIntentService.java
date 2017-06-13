package com.example.administrator.servicelearning;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MyIntentService extends IntentService {
    static final String TAG = "MyIntentService" ;
    private NotificationManager mNF;

    public MyIntentService(){
        super(TAG);
    }

    public MyIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate(){
        super.onCreate();

        mNF = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        showNotification();

        Log.i(TAG , "onCreate()...");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG , "onHandleIntent()...");

        String str = intent.getStringExtra("name");

        switch (str){
            case "first":
                Log.i(TAG,"it is First Intent  "+intent.toString());
                break;
            case "second":
                Log.i(TAG,"it is seconf intent  "+intent.toString());
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG , "onDestroy()...");
    }

    private void showNotification() {
        CharSequence text = "IntentService Started";
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

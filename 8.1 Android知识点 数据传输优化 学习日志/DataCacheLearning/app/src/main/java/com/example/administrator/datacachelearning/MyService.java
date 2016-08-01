package com.example.administrator.datacachelearning;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends Service {

    NotificationManager nfm ;

    String imageurl ;

   static final String ACTION = "polling" ;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        nfm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification(null , 0);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        imageurl = intent.getStringExtra("url");
        new PollingThread().start();
    }

    public void showNotification(Bitmap bitmap , int count){
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
//        final ImageView  iv = new ImageView(this);
//        iv.setImageBitmap(bitmap);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(bitmap)
                .setContentTitle("hhhhhh       "+count)
                .build();

        notification.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        nfm.notify(0,notification);
    }


    int count = 0;
    class PollingThread extends Thread{
        @Override
        public void run() {
            super.run();
            count++ ;
            URL url = null;
            Bitmap bitmap = null ;
            try {
                url = new URL(imageurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream() ;
                bitmap = BitmapFactory.decodeStream(inputStream) ;
                Log.i("Myservice",bitmap+"");
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
//            if (count % 5 == 0) {
//                showNotification(bitmap , count);
//
//            }
            System.out.println("加载图标!");
            showNotification(bitmap , count);
        }
    }
}

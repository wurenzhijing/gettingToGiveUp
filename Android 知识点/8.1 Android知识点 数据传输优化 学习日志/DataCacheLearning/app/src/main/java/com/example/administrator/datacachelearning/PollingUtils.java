/**
 * User: wurenzhijing
 * Date: 2016-08-01
 * Time: 16:56
 * FIXME
 */
package com.example.administrator.datacachelearning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class PollingUtils {
    public static void startPolling(Context context , int seconds , Class<?> cls , String action){
        AlarmManager alarmManager =  (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context , cls);
        intent.setAction(action) ;
        intent.putExtra("url" , "http://ico.ooopic.com/ajax/iconpng/?id=61532.png");
        PendingIntent pendingIntent = PendingIntent.getService(context , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);

        long time = SystemClock.elapsedRealtime();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME , time , seconds*1000 , pendingIntent);
    }

    public static void stopPolling(Context context , Class<?> cls  , String action){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context , cls);
        intent.setAction(action);
        PendingIntent pendingIntent  = PendingIntent.getService(context , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}  


package com.example.administrator.broadcastreceiverlearning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class Ordered2BroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String str = intent.getStringExtra("message");

        Toast.makeText(context,"次级广播静态注册接受的信息： "+str,Toast.LENGTH_SHORT).show();
        Log.i("------","次级广播静态注册接受的信息： "+str);
    }
}

package com.example.administrator.messageserver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MessageService extends Service {

    static final int MSG = 11 ;

    Messenger mMessenger = new Messenger(new mHandler()) ;

    class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // message to client
            Bundle bundle = (Bundle)msg.obj ;
            Message msgBack = Message.obtain(msg) ;

            switch(msg.what){
                // client msg
                case MSG:
                    msgBack.what = MSG ;
                    try{
                        Thread.sleep(1000);
                        final Messenger callback = msg.replyTo ;
                        //msgBack.arg1 = msg.arg1 +100 ;
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("callback", "服务端：已收到信息  \n  返回信息："+bundle.get("name"));
                        //Message msg2server = Message.obtain(null, MSG);
                        msgBack.obj = bundle1;

                        //msgBack.obj = "服务端：已收到信息  \n  返回信息："+bundle.get("name");

                        String str = msgBack.obj.toString() ;
                        SharedPreferences.Editor editor = getSharedPreferences("message" , Context.MODE_PRIVATE).edit() ;
                        editor.putString("msg" , str) ;
                        editor.commit() ;

                        Log.d("Server",callback+"");
                        if(callback!=null){
                            callback.send(msgBack);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext() , "messenger" , Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder() ;
    }
}

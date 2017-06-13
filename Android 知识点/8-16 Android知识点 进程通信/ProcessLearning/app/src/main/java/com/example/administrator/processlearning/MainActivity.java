package com.example.administrator.processlearning;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.messageserver.IMyServiceAIDL;
import com.example.administrator.messageserver.Person;


public class MainActivity extends AppCompatActivity {

    static final int MSG = 11;

    Button btn_bind, btn_unbind, btn_aidl , btn_msg;
    EditText et_content;
    IMyService iMyService;
    IMyServiceAIDL aidlService ;
    ServiceConnection serviceConnection ;


    boolean serviceConState = false;
    boolean aidlConState = false ;
    boolean msgConState = false;

    Messenger msgService;

    Messenger messenger;


    ServiceConnection msgConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            msgService = new Messenger(iBinder);
            msgConState = true;
            Log.d("-----", "connect success");
            Toast.makeText(MainActivity.this, "connect success!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            msgService = null;
            msgConState = false;
            Log.d("-----", "connect failed");
            Toast.makeText(MainActivity.this, "connect fail!", Toast.LENGTH_SHORT).show();
        }
    };

    ServiceConnection aidlConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlService = IMyServiceAIDL.Stub.asInterface(iBinder);
            aidlConState = true ;
            Log.d("---", aidlService.toString() + " " + (aidlService != null));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            aidlConState = false ;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent("com.example.administrator.messageserver");
        intent.setPackage("com.example.administrator.messageserver");
        bindService(intent, msgConnection, Context.BIND_AUTO_CREATE);

        Log.d("MainActivity.onCreate()" , "messageService binded!");
        //Toast.makeText(MainActivity.this, "bind MessageService success! ", Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent("com.example.administrator.aidlService") ;
        intent1.setPackage("com.example.administrator.messageserver") ;
        bindService(intent1 , aidlConnection , Context.BIND_AUTO_CREATE) ;

        Log.d("MainActivity.onCreate()" , "aidlService binded!");



        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                iMyService = IMyService.Stub.asInterface(iBinder);
                serviceConState = true;
                Log.d("---", iMyService.toString() + " " + (iMyService != null));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceConState = false;
            }
        };






        btn_bind = (Button) findViewById(R.id.btn_bind);
        btn_unbind = (Button) findViewById(R.id.btn_unbind);
        btn_aidl = (Button)findViewById(R.id.btn_aidl);
        btn_msg = (Button) findViewById(R.id.btn_msg);

        btn_bind.setOnClickListener(bindOnClickListener);
        btn_unbind.setOnClickListener(unbindOnClickListener);
        btn_aidl.setOnClickListener(aidlOnCliclListener);
        btn_msg.setOnClickListener(msgOnClickLisener);

        et_content = (EditText) findViewById(R.id.et_content);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public View.OnClickListener bindOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // bind service

            Intent intent = new Intent(MainActivity.this, MyService.class);
            boolean b = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

            Toast.makeText(MainActivity.this, "bind success! " + b, Toast.LENGTH_SHORT).show();

            Person1 person = new Person1("hebin", "nan", 23);
            try {
                if (serviceConState) {
                    String str = iMyService.getPerson().toString();
                    String str1 = iMyService.getValue(person);
                    Log.d("__________", str +"  "+str1);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener unbindOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // unbind service
            if (serviceConState) {
                unbindService(serviceConnection);
                Toast.makeText(MainActivity.this, "unbind success! ", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener aidlOnCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            Person person = new Person("xiaoming", "nan", 22);

            try {
                 String str = aidlService.getValue(person);
                Log.d("----------" , str) ;
                Toast.makeText(MainActivity.this, "AIDLService信息： "+str, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    View.OnClickListener msgOnClickLisener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // message
            Log.d("msgConState", "   " + msgConState);
            String content = et_content.getText().toString();
            if (msgConState) {
//                Intent intent = new Intent();
//                intent.setAction("com.example.administrator.messageserver");
//                bindService(intent , msgConnection , Context.BIND_AUTO_CREATE) ;
//
//                Toast.makeText(MainActivity.this , "bind success! " ,Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("name", content);
                final Message msg2server = Message.obtain(null, MSG);
                msg2server.obj = bundle;
                //回调 Messenger
                msg2server.replyTo = new Messenger(new Handler() {
                    @Override
                    public void handleMessage(Message msgServer) {
                        switch (msgServer.what) {
                            case MSG:
                                Bundle bundle = (Bundle)msgServer.obj ;
                                String str = bundle.getString("callback");
                                Log.d("msgServer.obj  ", " " + str);
                                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        super.handleMessage(msgServer);
                    }
                });
                try {
                    msgService.send(msg2server);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (msgConState) {
            unbindService(msgConnection);

        }

        if (aidlConState){
            unbindService(aidlConnection);
        }
    }

}

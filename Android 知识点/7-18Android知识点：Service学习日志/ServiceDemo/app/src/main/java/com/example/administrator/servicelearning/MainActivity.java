package com.example.administrator.servicelearning;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button bt_start,bt_stop,bt_bind,bt_unbind,bt_startIntentService,bt_stopIntentService;

    ServiceConnection serviceConnection ;

    MyLocalService.LocalBinder myLocalService;

    static final String TAG = "MyLocalService";

    boolean isBind = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_start = (Button)findViewById(R.id.bt_start);
        bt_stop = (Button)findViewById(R.id.bt_stop);
        bt_bind = (Button)findViewById(R.id.bt_bind);
        bt_unbind = (Button)findViewById(R.id.bt_unbind);
        bt_startIntentService = (Button)findViewById(R.id.bt_startIntentService);
        bt_stopIntentService = (Button)findViewById(R.id.bt_stopIntentService);

        bt_start.setOnClickListener(myStartListener);
        bt_stop.setOnClickListener(myStopListener);
        bt_bind.setOnClickListener(myBindListener);
        bt_unbind.setOnClickListener(myUnbindListener);
        bt_startIntentService.setOnClickListener(myStartIntentListener);
        bt_stopIntentService.setOnClickListener(myStopIntentListener);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyLocalService.LocalBinder mBinder = (MyLocalService.LocalBinder)iBinder;

                Log.i(TAG , mBinder.getService().toString());
                myLocalService = (MyLocalService.LocalBinder)iBinder;

                String str = myLocalService.getState();

                Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();


                isBind = true ;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBind = false ;
            }
        };

    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(isBind){
            unbindService(serviceConnection);
            isBind = false ;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopService(new Intent(MainActivity.this,MyLocalService.class) );
        unbindService(serviceConnection);
    }



    private View.OnClickListener myStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startService(new Intent(MainActivity.this,MyLocalService.class));
            Toast.makeText(MainActivity.this,"service start",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener myStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            stopService(new Intent(MainActivity.this,MyLocalService.class));
            Toast.makeText(MainActivity.this,"service stop",Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener myBindListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent bindIntent  = new Intent(MainActivity.this,MyLocalService.class);

            bindService(bindIntent ,serviceConnection,0);


            isBind = true ;

            Toast.makeText(MainActivity.this,"service bind",Toast.LENGTH_SHORT).show();

            Log.i(TAG , isBind+"");

        }
    };

    private View.OnClickListener myUnbindListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isBind){
                unbindService(serviceConnection);
                isBind = false;
                Toast.makeText(MainActivity.this,"service unbind",Toast.LENGTH_SHORT).show();
            }

        }
    };

    private View.OnClickListener myStartIntentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent1 = new Intent(MainActivity.this,MyIntentService.class);
            intent1.putExtra("name","first");
            startService(intent1);

            Intent intent2 = new Intent(MainActivity.this,MyIntentService.class);
            intent2.putExtra("name","second");
            startService(intent2);

        }
    };

    private View.OnClickListener myStopIntentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,MyIntentService.class);
            stopService(intent);
        }
    };




}

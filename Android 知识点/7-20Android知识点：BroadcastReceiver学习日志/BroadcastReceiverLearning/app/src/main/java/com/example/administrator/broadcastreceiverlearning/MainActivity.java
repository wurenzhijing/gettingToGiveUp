package com.example.administrator.broadcastreceiverlearning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText et_message ;
    TextView tv_receive ,tv_sendInfo;
    Button bt_send;


    LBroadcastReceivere broadcastReceiver = new LBroadcastReceivere();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_message = (EditText)findViewById(R.id.et_message);
        tv_receive = (TextView)findViewById(R.id.tv_receive);
        tv_sendInfo = (TextView)findViewById(R.id.tv_sendInfo);

        bt_send = (Button)findViewById(R.id.bt_send);
        bt_send.setOnClickListener(sendOnClickListener);

    }

    public View.OnClickListener sendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction("com.android.aaaaaa");
            intent.putExtra("message",et_message.getText().toString());
            sendBroadcast(intent);

            Intent intent1 = new Intent();
            intent1.setAction("com.android.bbbbbb");
            intent1.putExtra("message",et_message.getText().toString());
            sendOrderedBroadcast(intent1,null);

            Log.i(this.toString(), intent1.toString());


            Intent intent2 = new Intent();
            intent2.setAction("com.android.cccccc");
            intent2.putExtra("message",et_message.getText().toString());
            sendOrderedBroadcast(intent2,null);

            Log.i(this.toString(), intent2.toString());

            tv_sendInfo.setText("第一个正常广播  "+intent.toString()+"\n第一个有序广播  "+intent1.toString()+"\n第二个有序广播  "+intent2.toString());
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter intentFilter= new IntentFilter("com.android.aaaaaa");
        registerReceiver(broadcastReceiver,intentFilter);
    }
    class  LBroadcastReceivere extends BroadcastReceiver  {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            tv_receive.setText(message);

            Log.i("--------",message);
        }
    }

    @Override
    protected void onPause(){
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}

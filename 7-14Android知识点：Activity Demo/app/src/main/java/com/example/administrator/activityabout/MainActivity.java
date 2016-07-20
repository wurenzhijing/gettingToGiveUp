package com.example.administrator.activityabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Button bt_standard,bt_singleTop,bt_SingleTask, bt_singleInstance;
    private TextView tv_main;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate()执行中。。。。");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_singleTop = (Button)findViewById(R.id.bt_singleTop);
        bt_standard = (Button)findViewById(R.id.bt_standard);
        bt_SingleTask = (Button)findViewById(R.id.bt_SingleTask);
        bt_singleInstance = (Button)findViewById(R.id.bt_SingleInstance) ;

        tv_main = (TextView)findViewById(R.id.tv_main) ;
        tv_main.setText(this.toString());


        bt_standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                tv_main.setText(this.toString());

            }
        });
        bt_singleTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SingleTopActivity.class);
                startActivity(intent);
            }
        });
        bt_SingleTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SingleTaskActivity.class);
                startActivity(intent);
            }
        });
        bt_singleInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SingleInstanceActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart(){
        Log.i(TAG,"onStart()执行中。。。。");
        super.onStart();
    }

    @Override
    protected void onRestart(){
        Log.i(TAG,"onRestart()执行中。。。。");
        super.onRestart();
    }

    @Override
    protected void onPause(){
        Log.i(TAG,"onPause()执行中。。。。");
        super.onPause();
    }

    @Override
    protected void onResume(){
        Log.i(TAG,"onResume()执行中。。。。");
        super.onResume();
    }

    @Override
    protected void onStop(){
        Log.i(TAG,"onStop()执行中。。。。");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        Log.i(TAG,"onDestroy()执行中。。。。");
        super.onDestroy();
    }

}

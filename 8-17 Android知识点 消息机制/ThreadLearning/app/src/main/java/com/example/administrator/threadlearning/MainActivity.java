package com.example.administrator.threadlearning;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btn_Thread , btn_loopThread , btn_Quene;
    TextView tv_time , tv_local ;

    People p1 = new People("hb" , "nan" , 22) ;
    People p2 = new People("xm" , "nan" , 23) ;


    ThreadLocal<People> threadLocal = new ThreadLocal<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_local = (TextView)findViewById(R.id.tv_loacal) ;
        tv_time = (TextView) findViewById(R.id.tv_time);

        btn_Thread = (Button) findViewById(R.id.btn_thread);
        btn_Thread.setOnClickListener(threadOnClickListener);

        btn_loopThread = (Button) findViewById(R.id.btn_loopThread);
        btn_loopThread.setOnClickListener(loopThreadOnClickListener);

        btn_Quene = (Button) findViewById(R.id.btn_messageQuene);
        btn_Quene.setOnClickListener(queneOnClickListener);


        new HandlerThread().start();    //启动子线程
    }

    View.OnClickListener threadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(timeRunnable).start();
        }
    };
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.obj = "timeThread";
            timeHandle.sendMessage(message);
            timeHandle.postDelayed(timeRunnable, 1000);
        }
    };

    Handler timeHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String time = format.format(date);

            tv_time.setText(msg.obj + "  " + time);
        }
    };


    View.OnClickListener loopThreadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocal.set(p1);
                    String threadName = Thread.currentThread().getName();
                    People people = threadLocal.get() ;
                    Log.d("  Thread   ",threadName +"  数据："+people.getName()+" "+people.getGender()+" "+people.getAge());
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocal.set(p2);
                    String threadName = Thread.currentThread().getName();
                    People people = threadLocal.get() ;
                    Log.d("  Thread   ",threadName +"  数据："+people.getName()+" "+people.getGender()+" "+people.getAge());
                    String str = threadName +"  数据："+people.getName()+" "+people.getGender()+" "+people.getAge() ;
                    Message msg = new Message() ;
                    msg.obj = str ;

                    localHandle.sendMessage(msg) ;
                }
            }).start();

        }
    };



    Handler localHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_local.setText(msg.obj.toString());
        }
    };

    Handler handler ;
    class HandlerThread extends Thread{
        @Override
        public void run() {
            // 开始建立消息循环
            Looper.prepare();  // 初始化Looper
            handler = new Handler(){   // 默认绑定本线程的Looper
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 0:
                            Toast.makeText(MainActivity.this, "子线程收到消息" ,Toast.LENGTH_SHORT).show();
                    }
                }
            };
            super.run();
            Looper.loop();  // 启动消息循环
        }
    }
    View.OnClickListener queneOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            handler.sendEmptyMessage(0) ;  // 向子线程发送消息
        }
    };


}

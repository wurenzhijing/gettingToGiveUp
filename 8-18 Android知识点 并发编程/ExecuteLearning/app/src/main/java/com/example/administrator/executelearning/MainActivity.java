package com.example.administrator.executelearning;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button btn_cachePool , btn_fixedPool , btn_schedulePool , btn_singlePool ;
    ListView listView ;
    ArrayAdapter arrayAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.lv) ;

        btn_cachePool = (Button)findViewById(R.id.btn_cachePool) ;
        btn_fixedPool = (Button)findViewById(R.id.btn_fixedPool) ;
        btn_schedulePool = (Button)findViewById(R.id.btn_scheduledPool) ;
        btn_singlePool = (Button)findViewById(R.id.btn_singlePool) ;

        btn_cachePool.setOnClickListener(cachePoolOnClickListener);
        btn_fixedPool.setOnClickListener(fixedPoolOnClickListener);
        btn_schedulePool.setOnClickListener(schedulePoolOnClickListenr);
        btn_singlePool.setOnClickListener(singlePoolOnClickListener);
    }

    View.OnClickListener cachePoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // newCachedThreadPool创建一个可缓存线程池，
            // 如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
            ExecutorService cacheExecutor = Executors.newCachedThreadPool();
            final ArrayList<String>  activeCache = new ArrayList<>() ;
            for (int i= 0 ;i < 30 ; i++){
                final int index = i ;
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                cacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newCachedThreadPool" ," activeCache count = "+ Thread.activeCount() + "  index = "+index);

                        activeCache.add(" activeCache count = "+ Thread.activeCount() + "  index = "+index );
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            cacheExecutor.shutdown();
            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , activeCache ) ;
            listView.setAdapter(arrayAdapter);
        }
    };

    View.OnClickListener fixedPoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
            final ArrayList<String>  activeFixed = new ArrayList<>() ;
            ExecutorService fixedExecutor = Executors.newFixedThreadPool(3) ;
            for ( int i = 0 ; i < 20 ; i++){
                final int index = i ;
                fixedExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newFixedTheadPool" , " activce count = "+Thread.activeCount()+"  index = "+index) ;
                        activeFixed.add(" activeFixed count = "+ Thread.activeCount() + "  index = "+index );
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            fixedExecutor.shutdown();
            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , activeFixed ) ;
            listView.setAdapter(arrayAdapter);
        }
    };

    View.OnClickListener schedulePoolOnClickListenr = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
            // 延时
            final ArrayList<String>  activeSchedule = new ArrayList<>() ;
            ScheduledExecutorService scheduleExecutor =   Executors.newScheduledThreadPool(5) ;
            scheduleExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    Log.d("newScheduleThreadPool" ,"delay 3s schedule activce count = "+Thread.activeCount() ) ;
                    activeSchedule.add("delay 3s schedule activce count = "+Thread.activeCount());
                }
            } , 3 , TimeUnit.SECONDS);
            // 定期   定时器

            ScheduledExecutorService scheduledExecutorFixed = Executors.newScheduledThreadPool(5) ;
            scheduledExecutorFixed.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Log.d("newScheduleThreadPool " ,"fixed delay 1s and execute every 3s  schedule activce count = "+Thread.activeCount() ) ;
                }
            } , 1 , 3 , TimeUnit.SECONDS);
            scheduleExecutor.shutdown();
            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , activeSchedule ) ;
            listView.setAdapter(arrayAdapter);
        }
    };


    View.OnClickListener singlePoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //newSingleThreadExecutor 创建一个单线程化的线程池，
            // 它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
            final ExecutorService singleExecutor = Executors.newSingleThreadExecutor() ;
            final ArrayList<String>  activeSingle = new ArrayList<>() ;
            for ( int i = 0 ; i < 15 ; i++){
                final int index = i ;
                singleExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newSingleThreadPool" , " active count = "+Thread.activeCount()+"  index = "+index) ;
                       // activeSingle.add(" active count = "+Thread.activeCount()+"  index = "+index);
                        Message msg = new Message() ;
                        msg.obj = " active count = "+Thread.activeCount()+"  index = "+index ;
                        handler.sendMessage(msg) ;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            singleExecutor.shutdown();

            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
        }
    };
    final ArrayList<String>  active = new ArrayList<>() ;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString() ;

            active.add(str);
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , active ) ;
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }
    };

}

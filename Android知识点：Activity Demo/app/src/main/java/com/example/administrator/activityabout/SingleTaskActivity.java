package com.example.administrator.activityabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/13.
 */
public class SingleTaskActivity extends Activity {
    private TextView tv_SingleTask;
    private Button bt_task,bt_MainActivity,bt_TaskActivity1,bt_TaskActivity2;

    @Override
    public void onCreate(Bundle savedOInstanceState){
        super.onCreate(savedOInstanceState);
        setContentView(R.layout.activity_singletask);

        tv_SingleTask = (TextView)findViewById(R.id.tv_singleTask);
        bt_MainActivity = (Button)findViewById(R.id.bt_MainActivity);
        bt_task = (Button)findViewById(R.id.bt_task);
        bt_TaskActivity1 = (Button)findViewById(R.id.bt_TaskActivity1);
        bt_TaskActivity2 = (Button)findViewById(R.id.bt_TaskActivity2);

        tv_SingleTask.setText(this.toString());

        bt_MainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTaskActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        bt_TaskActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTaskActivity.this,TaskActivity1.class);
                startActivity(intent);

            }
        });
        bt_TaskActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTaskActivity.this,TaskActivity2.class);
                startActivity(intent);

            }
        });

        bt_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTaskActivity.this,SingleTaskActivity.class);
                startActivity(intent);
                tv_SingleTask.setText(this.toString());
            }
        });
    }
}

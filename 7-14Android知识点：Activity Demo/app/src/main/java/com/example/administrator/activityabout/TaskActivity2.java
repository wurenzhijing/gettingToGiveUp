package com.example.administrator.activityabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/14.
 */
public class TaskActivity2 extends Activity {

    private Button bt_task1,bt_task2,bt_SingleTaskActivity;
    private TextView tv_task;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        bt_SingleTaskActivity = (Button)findViewById(R.id.bt_singleTaskActivity);
        bt_task1 = (Button)findViewById(R.id.bt_task1);
        bt_task2 = (Button)findViewById(R.id.bt_task2);

        tv_task = (TextView) findViewById(R.id.tv_Task);
        tv_task.setText(this.toString());

        bt_task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity2.this,TaskActivity1.class);
                startActivity(intent);
            }
        });

        bt_SingleTaskActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity2.this,SingleTaskActivity.class);
                startActivity(intent);
            }
        });

        bt_task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity2.this,TaskActivity2.class);
                startActivity(intent);
                tv_task.setText(TaskActivity2.this.toString());
            }
        });
    }
}

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
public class InstanceActivity1 extends Activity {

    private Button bt_Instance1,bt_Instance2,bt_SingleInstanceActivity;
    private TextView tv_Instance;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance);

        bt_SingleInstanceActivity = (Button)findViewById(R.id.bt_singleInstanceActivity);
        bt_Instance1 = (Button)findViewById(R.id.bt_Instance1);
        bt_Instance2 = (Button)findViewById(R.id.bt_Instance2);

        tv_Instance = (TextView) findViewById(R.id.tv_Instance);
        tv_Instance.setText(this.toString());

        bt_Instance1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstanceActivity1.this,InstanceActivity1.class);
                startActivity(intent);
                tv_Instance.setText(InstanceActivity1.this.toString());
            }
        });

        bt_SingleInstanceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstanceActivity1.this,SingleInstanceActivity.class);
                startActivity(intent);

            }
        });

        bt_Instance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstanceActivity1.this,InstanceActivity2.class);
                startActivity(intent);
            }
        });
    }
}

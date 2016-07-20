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
public class SingleInstanceActivity extends Activity {

    private TextView tv_SingleInstance;
    private Button bt_Instance,bt_MainActivity,bt_InstanceActivity1,bt_InstanceActivity2;

    @Override
    public void onCreate(Bundle savedOInstanceState){
        super.onCreate(savedOInstanceState);
        setContentView(R.layout.activity_singleinstance);

        tv_SingleInstance = (TextView)findViewById(R.id.tv_SingleInstance);
        bt_Instance = (Button)findViewById(R.id.bt_Instance);
        bt_MainActivity = (Button)findViewById(R.id.bt_MainActivity);
        bt_InstanceActivity1 = (Button)findViewById(R.id.bt_InstanceActivity1);
        bt_InstanceActivity2 = (Button)findViewById(R.id.bt_InstanceActivity2);

        tv_SingleInstance.setText(this.toString());

        bt_MainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstanceActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        bt_InstanceActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstanceActivity.this,InstanceActivity1.class);
                startActivity(intent);

            }
        });
        bt_InstanceActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstanceActivity.this,InstanceActivity2.class);
                startActivity(intent);

            }
        });

        bt_Instance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstanceActivity.this,SingleInstanceActivity.class);
                startActivity(intent);
                tv_SingleInstance.setText(this.toString());
            }
        });
    }
}

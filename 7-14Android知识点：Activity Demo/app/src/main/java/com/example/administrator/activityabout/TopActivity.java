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
public class TopActivity extends Activity {
    private TextView tv_Top;
    private Button bt_SingleTop,bt_Top;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        tv_Top = (TextView)findViewById(R.id.tv_Top);
        bt_SingleTop = (Button)findViewById(R.id.bt_SingleTop);
        bt_Top = (Button)findViewById(R.id.bt_Top);

        tv_Top.setText(this.toString());

        bt_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopActivity.this,TopActivity.class);
                startActivity(intent);
                tv_Top.setText(TopActivity.this.toString());
            }
        });

        bt_SingleTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopActivity.this,SingleTopActivity.class);
                startActivity(intent);
            }
        });
    }

}

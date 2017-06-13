package com.example.administrator.messageserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MSGActivity extends AppCompatActivity {
   TextView tv_message ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_message = (TextView)findViewById(R.id.tv_message) ;

        SharedPreferences sharedPreferences = getSharedPreferences("message" , Context.MODE_PRIVATE) ;
        String str = sharedPreferences.getString("msg" ,"") ;
        tv_message.setText(str);
    }
}

package com.example.administrator.testLearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class OtherActivity extends Activity {

    TextView tv_activityReceive ;



    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_other);

        tv_activityReceive = (TextView)findViewById(R.id.tv_activityReceive);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ParcelableBook book = (ParcelableBook) bundle.getParcelable("book");
        String str = bundle.getString("message");
        tv_activityReceive.setText("intent传递的数据： "+str+"\nintent传递的对象:  "+ book.getName().toString()+" "+book.getAuthor()+" "+book.getISBN()+" "+book.getPrice());
    }

}

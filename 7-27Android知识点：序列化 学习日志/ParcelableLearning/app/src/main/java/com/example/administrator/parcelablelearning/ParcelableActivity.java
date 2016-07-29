package com.example.administrator.parcelablelearning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ParcelableActivity extends Activity {

    TextView tv_name ,tv_gender , tv_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelable);

        tv_name = (TextView)findViewById(R.id.tv_name) ;
        tv_gender = (TextView)findViewById(R.id.tv_gender);
        tv_age = (TextView)findViewById(R.id.tv_age) ;

    }

    @Override
    public void onStart(){
        super.onStart();

        Intent intent = getIntent();
        ParcelablePeople parcelablePeople = (ParcelablePeople)intent.getParcelableExtra("parcelablePeople");
        String name = parcelablePeople.getName();
        String gender = parcelablePeople.getGender() ;
        int age = parcelablePeople.getAge() ;

        tv_name.setText(name);
        tv_gender.setText(gender);
        tv_age.setText(age+"岁");
    }

}

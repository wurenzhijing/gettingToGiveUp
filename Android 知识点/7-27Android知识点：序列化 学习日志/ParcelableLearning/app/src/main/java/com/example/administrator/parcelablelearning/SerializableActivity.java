package com.example.administrator.parcelablelearning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SerializableActivity extends AppCompatActivity {

    TextView tv_name ,tv_gender , tv_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serializable);

        tv_name = (TextView)findViewById(R.id.tv_Sname) ;
        tv_gender = (TextView)findViewById(R.id.tv_Sgender);
        tv_age = (TextView)findViewById(R.id.tv_Sage) ;

    }

    @Override
    public void onStart(){
        super.onStart();

        Intent intent = getIntent();
        SerializablePeople serializablePeople = (SerializablePeople)intent.getSerializableExtra("serializablePeople");
        String name = serializablePeople.getName();
        String gender = serializablePeople.getGender() ;
        int age = serializablePeople.getAge() ;

        tv_name.setText(name);
        tv_gender.setText(gender);
        tv_age.setText(age+"岁");
    }
}

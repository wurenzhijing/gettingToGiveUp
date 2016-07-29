package com.example.administrator.parcelablelearning;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText et_name , et_gender , et_age;
    Button bt_send1 ,bt_send2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = (EditText)findViewById(R.id.et_name) ;
        et_gender = (EditText)findViewById(R.id.et_gender) ;
        et_age = (EditText)findViewById(R.id.et_age) ;

        bt_send1 = (Button)findViewById(R.id.bt_send1) ;
        bt_send1.setOnClickListener(myParcelableOnClickListener);

        bt_send2 = (Button)findViewById(R.id.bt_send2);
        bt_send2.setOnClickListener(mySerializableOnCliclListener);


    }

    public void getData(){
        String name = et_name.getText().toString() ;
        String gender = et_gender.getText().toString() ;
        int age = Integer.parseInt(et_age.getText().toString());
    }

    public View.OnClickListener myParcelableOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String name = et_name.getText().toString() ;
            String gender = et_gender.getText().toString() ;
            String age =  et_age.getText().toString() ;

            if( name.equals("") || gender.equals("") || age.equals("")){
                Toast.makeText(MainActivity.this,"请输入信息",Toast.LENGTH_LONG).show();
            }else {
                ParcelablePeople parcelablePeople = new ParcelablePeople(name , gender , Integer.parseInt(age) );
                parcelablePeople.setName(name);
                parcelablePeople.setGender(gender);
                parcelablePeople.setAge(Integer.parseInt(age));

                Intent intent = new Intent(MainActivity.this,ParcelableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("parcelablePeople",parcelablePeople);
                intent.putExtras(bundle) ;
                startActivity(intent);
                Log.i("________",parcelablePeople.toString());
            }
        }
    };

    public View.OnClickListener mySerializableOnCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = et_name.getText().toString() ;
            String gender = et_gender.getText().toString() ;
            String age =  et_age.getText().toString() ;

            if( name.equals("") || gender.equals("") || et_age.getText().toString().equals("")){
                Toast.makeText(MainActivity.this,"请输入信息",Toast.LENGTH_LONG).show();
            }else {
                SerializablePeople serializablePeople = new SerializablePeople(name , gender , Integer.parseInt(age));
                Intent intent = new Intent(MainActivity.this, SerializableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("serializablePeople",serializablePeople);
                intent.putExtras(bundle);

                startActivity(intent);
                Log.i("________",serializablePeople.toString());
            }
        }
    };
}

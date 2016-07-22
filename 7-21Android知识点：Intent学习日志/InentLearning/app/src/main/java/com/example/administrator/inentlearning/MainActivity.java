package com.example.administrator.inentlearning;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_message;
    Button bt_activity, bt_service, bt_broadcast, bt_system;
    SerializableBook mBook;
    ParcelableBook pBook;
    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceiver = new MyReceiver();

        mBook = new SerializableBook("java编程思想", "Amindihd", "4948-8844", "56");

        pBook = new ParcelableBook();
        pBook.setName("android编程");
        pBook.setAuthor("Sedethd");
        pBook.setISBN("4565-4846");
        pBook.setPrice("46");


        et_message = (EditText) findViewById(R.id.et_message);

        bt_activity = (Button) findViewById(R.id.bt_activity);
        bt_activity.setOnClickListener(activityOnclickListener);

        bt_broadcast = (Button) findViewById(R.id.bt_broadcast);
        bt_broadcast.setOnClickListener(broadcastOnclickListener);

        bt_service = (Button) findViewById(R.id.bt_service);
        bt_service.setOnClickListener(serviceOnClickListener);

        bt_system = (Button) findViewById(R.id.bt_system);
        bt_system.setOnClickListener(systemOnClickListener);

    }

    View.OnClickListener activityOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, OtherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("message", et_message.getText().toString());
            bundle.putParcelable("book", pBook);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    View.OnClickListener broadcastOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("message", et_message.getText().toString());
            bundle.putSerializable("book", mBook);
            intent.putExtras(bundle);
            intent.setAction("aaaaaa");
            sendBroadcast(intent);
        }
    };

    View.OnClickListener serviceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MyService.class);
            intent.putExtra("message", et_message.getText().toString());
            startService(intent);
            Log.i("----",intent.toString());
        }
    };

    View.OnClickListener systemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse("tel:10086");
            Intent intent1 = new Intent(Intent.ACTION_CALL, uri);
            try{
                startActivity(intent1);
                Log.i("--------",intent1.toString());
            }catch (SecurityException e){
                e.printStackTrace();
            }


        }
    };

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SerializableBook book = (SerializableBook)bundle.getSerializable("book");
            String str = bundle.getString("message");
            Toast.makeText(MainActivity.this, "intent通过广播传递的数据： "+str+"\nintent通过广播传递的对象 "+ book.getName().toString()+" "+book.getAuthor()+" "+book.getISBN()+" "+book.getPrice()
                    , Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onResume(){
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("aaaaaa");
        registerReceiver(myReceiver,intentFilter);
    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(myReceiver);
    }
}

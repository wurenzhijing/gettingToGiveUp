package com.example.administrator.testLearning;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.testLearning.sqlite.Book;
import com.example.administrator.testLearning.sqlite.BookDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv_life ;
    EditText et_message;
    Button bt_activity, bt_service, bt_broadcast, bt_system;
    static SerializableBook mBook;
    static ParcelableBook pBook;
    MyReceiver myReceiver;


    static{
        pBook = new ParcelableBook();
        pBook.setName("android编程");
        pBook.setAuthor("Sedethd");
        pBook.setISBN("4565-4846");
        pBook.setPrice("46");

        mBook = new SerializableBook("java编程思想", "Amindihd", "4948-8844", "56");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        myReceiver = new MyReceiver();


        tv_life = (TextView)findViewById(R.id.tv_life) ;
        tv_life.setText("onCreate");
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


//            Book book = new Book();
//            book.setName("androidstudio");
//            book.setAuthor("heibin");
//            book.setIsbn("8054d3_35");
//            book.setPrice(748.00);
//            new BookDao(MainActivity.this).save(book);
//
//            Book book1 = new Book();
//            book1.setName("androidstudio");
//            book1.setAuthor("heibin");
//            book1.setIsbn("8054d3_35");
//            book1.setPrice(999.00);
//            int  result = new BookDao(MainActivity.this).update(book1);
//            Book newbook = new BookDao(MainActivity.this).query("androidstudio");

            Book book = new Book();
            book.setName("androidstudio");
            book.setAuthor("heibin");
            book.setIsbn("8054d3_35");
            book.setPrice(748.00);
            new BookDao(MainActivity.this).save(book);

            Book book1 = new Book();
            book1.setName("androidstudio");
            book1.setAuthor("heibin");
            book1.setIsbn("8054d3_35");
            book1.setPrice(999.00);
            new BookDao(MainActivity.this).save(book1);

            Book book2 = new Book();
            book2.setName("androidstudio1.0");
            book2.setAuthor("heijbin");
            book2.setIsbn("805e454d3_35");
            book2.setPrice(666.00);
            new BookDao(MainActivity.this).save(book2);

            List<Book> list = new BookDao(MainActivity.this).getAll() ;
            Log.i("____fff_____",list.size()+" ");

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



    @Override
    public void onResume(){
        super.onResume();
        tv_life.setText("onResume");


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("aaaaaa");
        registerReceiver(myReceiver,intentFilter);

        MyFragment myFragment = (MyFragment)getSupportFragmentManager().findFragmentById(R.id.fragment) ;
        myFragment.setView(et_message.getText().toString());

    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_life.setText("onDestroy");
    }
}

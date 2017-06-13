/**
 * User: wurenzhijing
 * Date: 2016-08-03
 * Time: 16:43
 * FIXME
 */
package com.example.administrator.testLearning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SerializableBook book = (SerializableBook)bundle.getSerializable("book");
            String str = bundle.getString("message");
            Toast.makeText(context, "intent通过广播传递的数据： "+str+"\nintent通过广播传递的对象: "+ book.getName().toString()+" "+book.getAuthor()+" "+book.getISBN()+" "+book.getPrice()
                    , Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = context.getSharedPreferences("book" , Context.MODE_PRIVATE).edit() ;
            editor.putString("book_name" , book.getName());
            editor.putString("book_author",book.getAuthor());
            editor.putString("book_ISBN",book.getISBN());
            editor.putString("book_price",book.getPrice());
            editor.commit() ;
        }
}  


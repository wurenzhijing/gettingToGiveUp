package com.example.administrator.contentprovicer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    EditText et_bookName,et_bookAuthor,et_bookISBN,et_bookPrice;
    Button bt_save ,bt_delete;
    ListView listView_book;

    String bookName;
    String bookAuthor;
    String bookISBN;
    String bookPrice;

    MyDBHelper myDBHelper;
    SQLiteDatabase db ;
    ListViewAdapter listViewAdapter;
    List<Book> bookList = new ArrayList<Book>();

    private ContentResolver contentResolver;

    static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_bookName = (EditText)findViewById(R.id.et_bookName);
        et_bookAuthor = (EditText)findViewById(R.id.et_bookAuthor);
        et_bookISBN = (EditText)findViewById(R.id.et_bookISBN);
        et_bookPrice = (EditText)findViewById(R.id.et_bookPrice);




        bt_save = (Button)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(mySaveOnClickListener);

        bt_delete = (Button)findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(myDeleteCliclListener);

        listView_book = (ListView)findViewById(R.id.listView_book);
        listViewAdapter = new ListViewAdapter(this);
        listView_book.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        myDBHelper = new MyDBHelper(this,"book.db",null,1);

        contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(BookU.CONTENT_URI,new String[]{BookU.NAME,BookU.AUTHOR,BookU.ISBN, BookU.PRICE},null,null,null);
        while(cursor.moveToNext()){
//            Toast.makeText(this,
//                    cursor.getString(cursor.getColumnIndex(BookU.NAME))+" "+
//                    cursor.getString(cursor.getColumnIndex(BookU.AUTHOR))+" "
//                            +cursor.getString(cursor.getColumnIndex(BookU.ISBN))+" "
//                            +cursor.getString(cursor.getColumnIndex(BookU.PRICE)),Toast.LENGTH_SHORT).show();

            bookList.add(new Book(cursor.getString(cursor.getColumnIndex(BookU.NAME)),
                    cursor.getString(cursor.getColumnIndex(BookU.AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BookU.ISBN)),
                    cursor.getString(cursor.getColumnIndex(BookU.PRICE))));
            listViewAdapter.notifyDataSetChanged();

        }
        cursor.close();

    }

    public void onResume(){
        super.onResume();
        contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(BookU.CONTENT_URI,new String[]{BookU.NAME,BookU.AUTHOR,BookU.ISBN, BookU.PRICE},null,null,null);
        while(cursor.moveToNext()){
            Toast.makeText(this,
                    cursor.getString(cursor.getColumnIndex(BookU.NAME))+" "+
                            cursor.getString(cursor.getColumnIndex(BookU.AUTHOR))+" "
                            +cursor.getString(cursor.getColumnIndex(BookU.ISBN))+" "
                            +cursor.getString(cursor.getColumnIndex(BookU.PRICE)),Toast.LENGTH_SHORT).show();

        }
        cursor.close();
    }

    public View.OnClickListener mySaveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            bookName = et_bookName.getText().toString();
            bookAuthor = et_bookAuthor.getText().toString();
            bookISBN = et_bookISBN.getText().toString();
            bookPrice = et_bookPrice.getText().toString();

            Log.i("------",bookName+"--"+bookAuthor+"---"+bookISBN+"---"+bookPrice);

            if (bookName != null && bookAuthor != null && bookISBN != null && bookPrice != null){
                db = myDBHelper.getWritableDatabase();

                Book book = new Book(bookName,bookAuthor,bookISBN,bookPrice);

                bookList.add(book);

                ContentValues cv = new ContentValues();
                cv.put("name",book.name);
                cv.put("author",book.author);
                cv.put("isbn",book.ISBN);
                cv.put("price",book.price);

                Log.i("------",cv.toString());

                db.insert("book","",cv);

                cv.clear();
            }else{
                Toast.makeText(MainActivity.this,"请填写信息",Toast.LENGTH_SHORT).show();
            }

            et_bookAuthor.setText("");
            et_bookISBN.setText("");
            et_bookName.setText("");
            et_bookPrice.setText("");

            listViewAdapter.notifyDataSetChanged();
        }
    };

    public View.OnClickListener myDeleteCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String bookname = bookList.get(2).getName();

            Log.i("------",bookname);

            ContentResolver contentResolver = getContentResolver();
            Uri uri = Uri.parse("content://com.example.administrator.contentprovice/book");
            String where = "name=?" ;
            String[] selectionArgs = { bookname };
            contentResolver.delete(uri,where,selectionArgs);

            Toast.makeText(MainActivity.this,"成功删除   "+bookname,Toast.LENGTH_LONG).show();

            listViewAdapter.notifyDataSetChanged();
        }
    };

    public class ListViewAdapter extends BaseAdapter{
        LayoutInflater layoutInflater = null;
        private ListViewAdapter(Context context){
            this.layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return bookList.size();
        }

        @Override
        public Book getItem(int i) {
            return bookList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null){
                view = layoutInflater.inflate(R.layout.listview_item,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_bookname = (TextView)view.findViewById(R.id.tv_bookname);
                viewHolder.tv_bookauthor = (TextView)view.findViewById(R.id.tv_bookauthor);
                viewHolder.tv_bookisbn = (TextView)view.findViewById(R.id.tv_bookisbn);
                viewHolder.tv_bookprice = (TextView)view.findViewById(R.id.tv_bookprice);

                view.setTag(viewHolder);
            }else {
                viewHolder =(ViewHolder)view.getTag();
            }

            viewHolder.tv_bookname.setText(bookList.get(i).getName());
            viewHolder.tv_bookauthor.setText(bookList.get(i).getAuthor());
            viewHolder.tv_bookisbn.setText(bookList.get(i).getISBN());
            viewHolder.tv_bookprice.setText(bookList.get(i).getPrice());
            return view;
        }
    }

    public class ViewHolder {
        TextView tv_bookname;
        TextView tv_bookauthor;
        TextView tv_bookisbn;
        TextView tv_bookprice;
    }
}

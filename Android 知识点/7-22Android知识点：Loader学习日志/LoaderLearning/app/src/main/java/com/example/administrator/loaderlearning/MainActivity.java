package com.example.administrator.loaderlearning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity  {

    EditText et_name,et_gender,et_age,et_height,et_weight,et_number;
    Button bt_save;
    ListView listView;

    ListViewAdapter listViewAdapter;

    MySQLiteHelper mySQLiteHelper ;
    SQLiteDatabase db;
    List<People> peopleList = new ArrayList<>();

    SimpleCursorAdapter simpleCursorAdapter ;
    LoaderManager loaderManager ;

    String mCurFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySQLiteHelper = new MySQLiteHelper(this,MySQLiteHelper.DB_NAME,null,MySQLiteHelper.VERSION);

        et_name = (EditText) findViewById(R.id.et_name);
        et_gender = (EditText)findViewById(R.id.et_gender);
        et_age = (EditText)findViewById(R.id.et_age);
        et_height = (EditText)findViewById(R.id.et_height);
        et_weight = (EditText)findViewById(R.id.et_weight);
        et_number = (EditText)findViewById(R.id.et_number);

        bt_save = (Button)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(saveOnClickListener);

        listView = (ListView)findViewById(R.id.listView);
        //listViewAdapter = new ListViewAdapter(this);
        //listView.setAdapter(listViewAdapter);

        String[] str= new String[]{"name","gender","age","height","weight","number"};
        int[] id= new int[]{ R.id.tv_name,R.id.tv_gender,R.id.tv_age,R.id.tv_height,R.id.tv_weight,R.id.tv_number};

        simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.list_item,null, str,id,0);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,callbacks);

        loaderManager.restartLoader(0,null,callbacks);
        Log.i(this.toString(),"--------"+loaderManager.toString());

    }

    public View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            db = mySQLiteHelper.getWritableDatabase();

            String name = et_name.getText().toString();
            String gender = et_gender.getText().toString();
            int age = Integer.parseInt(et_age.getText().toString());
            double height = Double.parseDouble(et_height.getText().toString());
            double weight = Double.parseDouble(et_weight.getText().toString());
            String number = et_number.getText().toString();
            if (name!=null && gender!=null && age!=0 &&height!=0 && weight!=0 && number!=null){
                ContentValues cv = new ContentValues();
                cv.put("name",name);
                cv.put("gender",gender);
                cv.put("age",age);
                cv.put("height",height);
                cv.put("weight",weight);
                cv.put("number",number);

                Log.i(this.toString(),cv.toString()+"-----" );
//                People peopleBean = new People(name,gender,age,height,weight,number);
//                peopleList.add(peopleBean);

                db.insert(MySQLiteHelper.TABLE_NAME,"",cv);
                cv.clear();
                db.close();
            }else {
                Toast.makeText(MainActivity.this,"请输入信息",Toast.LENGTH_SHORT).show();
            }
            et_name.setText("");
            et_gender.setText("");
            et_age.setText("");
            et_height.setText("");
            et_weight.setText("");
            et_number.setText("");

            loaderManager.restartLoader(0,null,callbacks);

        }
    };

    @Override
    public void onStart(){
        super.onStart();

        db = mySQLiteHelper.getWritableDatabase();

        Log.i("DB_NAME",db.toString()+"     "+db.getSyncedTables() );

        Cursor cursor =  db.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME,null);

        if (cursor!=null){
            cursor.moveToFirst();
            while(cursor.isAfterLast()==false){
                String name = cursor.getString(cursor.getColumnIndex("name")) ;
                String gender = cursor.getString(cursor.getColumnIndex("gender") );
                int age =   cursor.getInt(cursor.getColumnIndex("age"))  ;
                double height =  cursor.getDouble(cursor.getColumnIndex("height")   );
                double weight =  cursor.getDouble(cursor.getColumnIndex("weight"))   ;
                String   number=  cursor.getString(cursor.getColumnIndex("number") );

                People peopleBean = new People(name,gender,age,height,weight,number);
                peopleList.add(peopleBean);
                listViewAdapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
    }

    private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Uri uri = Uri.parse("content://com.example.administrator.loaderlearning.PeopleContentProvider/people");

            CursorLoader cursorloader =  new CursorLoader(MainActivity.this,uri,null,null,null,null);

            return cursorloader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            simpleCursorAdapter.swapCursor(cursor);
            listView.setAdapter(simpleCursorAdapter);
            Log.i("____________","onLoadFinashed()---------"+loader.toString());
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            simpleCursorAdapter.swapCursor(null);
            Log.i("_____-----","oneLoaderReset()");
        }
    };




    public class ListViewAdapter extends BaseAdapter{
        LayoutInflater layoutInflater = null;
        private ListViewAdapter(Context context){
            this.layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return peopleList.size();
        }

        @Override
        public People getItem(int i) {
            return peopleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;
            if (view == null){
                view = layoutInflater.inflate(R.layout.list_item,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView)view.findViewById(R.id.tv_name);
                viewHolder.tv_gender = (TextView)view.findViewById(R.id.tv_gender);
                viewHolder.tv_age = (TextView)view.findViewById(R.id.tv_age);
                viewHolder.tv_height = (TextView)view.findViewById(R.id.tv_height);
                viewHolder.tv_weight = (TextView)view.findViewById(R.id.tv_weight);
                viewHolder.tv_number = (TextView)view.findViewById(R.id.tv_number);

                view.setTag(viewHolder);
            }else {
                viewHolder =(ViewHolder)view.getTag();
            }
            viewHolder.tv_name.setText(peopleList.get(i).getName());
            viewHolder.tv_gender.setText(peopleList.get(i).getGender());
            viewHolder.tv_age.setText(String.valueOf(peopleList.get(i).getAge()));
            viewHolder.tv_height.setText(String.valueOf(peopleList.get(i).getHeight()));
            viewHolder.tv_weight.setText(String.valueOf(peopleList.get(i).getWeight())) ;
            viewHolder.tv_number.setText(peopleList.get(i).getNumber());

            return view;
        }
    }

    public class ViewHolder{
        TextView tv_name;
        TextView tv_gender;
        TextView tv_age;
        TextView tv_height;
        TextView tv_weight;
        TextView tv_number;

    }

}

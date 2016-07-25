package com.example.administrator.sqlitelearning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    EditText et_name , et_gender , et_class , et_score ;
    Button bt_add , bt_delete , bt_query , bt_modify ;
    ListView listView ;

    StudentListAdapter studentListAdapter ;

    List<Student> studentList = new ArrayList<Student>();

    MySQLiteHelper mySQLiteHelper ;

    static final String TAG  = "MainActivty" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySQLiteHelper = new MySQLiteHelper(this);

        et_name = (EditText)findViewById(R.id.et_name);
        et_gender = (EditText)findViewById(R.id.et_gender);
        et_class = (EditText)findViewById(R.id.et_class) ;
        et_score = (EditText)findViewById(R.id.et_score) ;

        bt_add = (Button)findViewById(R.id.bt_add) ;
        bt_add.setOnClickListener(addOnClickListener);

        bt_delete = (Button)findViewById(R.id.bt_delete) ;
        bt_delete.setOnClickListener(deleteOnClickListener);

        bt_query = (Button)findViewById(R.id.bt_query) ;
        bt_query.setOnClickListener(queryOnClickListener);

        bt_modify = (Button)findViewById(R.id.bt_modify) ;
        bt_modify.setOnClickListener(modifyOnClickListener);

        listView = (ListView)findViewById(R.id.listView) ;
        studentListAdapter = new StudentListAdapter(this);
        listView.setAdapter(studentListAdapter);
        studentListAdapter.notifyDataSetChanged();

    }

    public View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();

            String name = et_name.getText().toString();
            String gender = et_gender.getText().toString();
            String classes = et_class.getText().toString();
            String score = et_score.getText().toString();
            Log.i(TAG , "--"+name+"-"+gender+"-"+classes+"-"+score);

            if(name.equals("")||gender.equals("")||classes.equals("")||score.equals("")){
                Toast.makeText(MainActivity.this,"输入信息",Toast.LENGTH_SHORT).show();
            }else{
                ContentValues cv = new ContentValues() ;
                cv.put("name",name);
                cv.put("gender",gender);
                cv.put("class",classes);
                cv.put("score",Integer.parseInt(score));
                db.insert(MySQLiteHelper.TABLE_NAME,null,cv);

                Log.i(TAG , "--"+cv);

                cv.clear();
                db.close();
            }
            et_name.setText("");
            et_gender.setText("");
            et_class.setText("");
            et_score.setText("");
            updateList();

        }
    };

    public View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final EditText et = new EditText(MainActivity.this);
            et.setHint("输入学生姓名");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("删除")
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (et.getText().toString().equals("")){
                                Toast.makeText(MainActivity.this,"输入信息",Toast.LENGTH_SHORT).show();
                            }else{
                                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
                                db.delete(MySQLiteHelper.TABLE_NAME,"name = ?",new String[]{et.getText().toString()});
                                db.close();
                                updateList();
                            }

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.create().show();

        }
    };

    public View.OnClickListener queryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final EditText et = new EditText(MainActivity.this);

            et.setHint("输入学生姓名");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("查询")
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (et.getText().toString().equals("")){
                                Toast.makeText(MainActivity.this,"输入信息",Toast.LENGTH_SHORT).show();
                            }else {
                                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
                                Cursor cursor  = db.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME + " where name = ?", new String[]{et.getText().toString()} );
                                Log.i(TAG,"--"+cursor.toString());
                                if (cursor.moveToFirst()){
                                    Toast.makeText(MainActivity.this,cursor.getString(cursor.getColumnIndex("name")) +" "+cursor.getString(cursor.getColumnIndex("gender"))+" "
                                            +cursor.getString(cursor.getColumnIndex("class"))+" "+String.valueOf(cursor.getString(cursor.getColumnIndex("score"))),Toast.LENGTH_SHORT).show();
                                }
                                cursor.close();
                                db.close();
                            }
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.create().show();
        }
    };

    public View.OnClickListener modifyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null);
            final EditText et_stu = (EditText)contentView.findViewById(R.id.et_stu);
            final EditText et_message = (EditText)contentView.findViewById(R.id.et_message);
            final String[] items = new String[]{"性别","年级","分数"};
            final String[] item  = new String[]{"gender","class","score"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("修改")
                    .setView(contentView)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (et_message.getText().toString().equals("")|| et_stu.getText().toString().equals("")){
                                Toast.makeText(MainActivity.this,"输入信息",Toast.LENGTH_SHORT).show();
                            }else {
                                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase() ;
                                ContentValues cv = new ContentValues() ;
                                cv.put(item[i], et_message.getText().toString());
                                String where = "name = ?";
                                String[] whereArgs = new String[]{et_stu.getText().toString()};

                                db.update(MySQLiteHelper.TABLE_NAME,cv,where,whereArgs);
                                Log.i(TAG,"--"+cv.toString());
                                cv.clear();
                                db.close();
                                updateList();
                            }
                        }
                    });
            builder.create().show();
        }
    };

    @Override
    public void onResume(){
        super.onResume();

        updateList();

    }

    private void updateList() {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME,null);
        List<Student> list = new ArrayList<Student>();
        if (cursor != null){
            cursor.moveToFirst();
            while(cursor.isAfterLast()==false){

                String name = cursor.getString(cursor.getColumnIndex("name"));
                String gender = cursor.getString(cursor.getColumnIndex("gender"));
                String classes = cursor.getString(cursor.getColumnIndex("class"));
                int score =  cursor.getInt(cursor.getColumnIndex("score")) ;

                Student stu = new Student(name , gender , classes , score);
                list.add(stu);
                cursor.moveToNext();
                Log.i(TAG,"--"+stu.toString()+"   "+cursor.toString());
            }
        }
        cursor.close();
        db.close();
        if (list.equals(studentList)==false){
            studentList.clear();
            studentList.addAll(list);
            studentListAdapter.notifyDataSetChanged();
        }else {
            studentListAdapter.notifyDataSetChanged();
        }

    }


    public class StudentListAdapter extends BaseAdapter{

        LayoutInflater layoutInflater ;

        private  StudentListAdapter(Context context){
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return studentList.size();
        }

        @Override
        public Student getItem(int i) {
            return studentList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder ;
            if (view == null){
                viewHolder = new ViewHolder() ;
                view = layoutInflater.inflate(R.layout.list_item,null);
                viewHolder.tv_name = (TextView)view.findViewById(R.id.tv_name);
                viewHolder.tv_gender = (TextView)view.findViewById(R.id.tv_gender);
                viewHolder.tv_class = (TextView)view.findViewById(R.id.tv_class) ;
                viewHolder.tv_score = (TextView)view.findViewById(R.id.tv_score) ;

                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)view.getTag();
            }

            viewHolder.tv_name.setText(studentList.get(i).getName());
            viewHolder.tv_gender.setText(studentList.get(i).getGender());
            viewHolder.tv_class.setText(studentList.get(i).getClasses());
            viewHolder.tv_score.setText(String.valueOf(studentList.get(i).getScore())) ;

            return view;
        }
    }

    public class ViewHolder{
        TextView tv_name ;
        TextView tv_gender ;
        TextView tv_class ;
        TextView tv_score ;
    }
}

package com.example.administrator.sharedpreferencelearning;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {

    EditText et_value ,et_key ;
    TextView tv_key , tv_value;
    Button bt_add ;

    ListView listView ;
    MyAdapter adapter ;

    List<Map<String,String>> nameArray = new ArrayList<Map<String,String>>();
    Set<String> set = new HashSet<String>();
    List<String> keyList = new ArrayList<>() ;
    List<String> valueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_value = (EditText)findViewById(R.id.et_value);
        et_key = (EditText)findViewById(R.id.et_key);

        tv_key = (TextView)findViewById(R.id.tv_key);
        tv_value = (TextView)findViewById(R.id.tv_value);

        bt_add = (Button)findViewById(R.id.bt_add);
        bt_add.setOnClickListener(addOnClickListener);

        listView = (ListView)findViewById(R.id.listView);
        getData();
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String key = et_key.getText().toString();
            String value = et_value.getText().toString() ;

            if ( value.equals("")){
                Toast.makeText(MainActivity.this,"请输入信息",Toast.LENGTH_LONG).show();
            }else{
                SharedPreferences sharedPreferences = getSharedPreferences("name",0);
                SharedPreferences.Editor editor = sharedPreferences.edit() ;
                editor.putString(key,value) ;
                editor.commit();

                Log.i("___",value);

                SharedPreferences sharedPreferences1 = getSharedPreferences("name",0);
                String name = sharedPreferences1.getString(key,null);
                tv_key.setText(key);
                tv_value.setText(name);
                et_key.setText("");
                et_value.setText("");

                Map<String,?> map = sharedPreferences1.getAll();
                Log.i("____","map  "+map.toString());
                Toast.makeText(MainActivity.this,"存储的键值对为："+map.toString(),Toast.LENGTH_LONG).show();

                Iterator iterator = map.entrySet().iterator() ;
                keyList.clear();
                valueList.clear();
                while(iterator.hasNext()){
                    Map.Entry entry = (Map.Entry)iterator.next();

                    keyList.add(entry.getKey().toString());

                    valueList.add(entry.getValue().toString());

                    adapter.notifyDataSetChanged();
                }
                Log.i("___Map__",keyList.size()+"　"+keyList.toString()+"  "+valueList.size()+" "+valueList.toString());
            }
          }
    };




    public void getData(){
        SharedPreferences sharedPreferences1 = getSharedPreferences("name",0);
        Map<String,?> map = sharedPreferences1.getAll();
        Log.i("____","map  "+map.toString());
        Iterator iterator = map.entrySet().iterator() ;
        keyList.clear();
        valueList.clear();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            keyList.add(entry.getKey().toString());
            valueList.add(entry.getValue().toString());
        }
    }

    public class MyAdapter extends BaseAdapter{
        LayoutInflater layoutInflater ;
        public MyAdapter(Context context){
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return keyList.size();
        }

        @Override
        public String getItem(int i) {
            return keyList.get(i);
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
                view = layoutInflater.inflate(R.layout.item,null);
                viewHolder.tv_itemkey = (TextView)view.findViewById(R.id.tv_itemkey);
                viewHolder.tv_itemvalue = (TextView)view.findViewById(R.id.tv_itemvalue);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)view.getTag();
            }

            viewHolder.tv_itemkey.setText(keyList.get(i));
            viewHolder.tv_itemvalue.setText(valueList.get(i));

            return view;
        }
    }

    public class ViewHolder{
        TextView tv_itemkey , tv_itemvalue ;
    }

}

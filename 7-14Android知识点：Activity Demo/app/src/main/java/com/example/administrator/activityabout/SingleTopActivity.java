package com.example.administrator.activityabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/13.
 */
public class SingleTopActivity extends Activity {

    private Button bt_SingleTop,bt_Top ,bt_MainActivity ;
    private ListView listView_SingleTop;
    private TextView tv_singleTop;
//
//    private MyAdapter myAdapter;
//
//    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletop);

        bt_Top = (Button)findViewById(R.id.bt_Top);
        bt_SingleTop = (Button)findViewById(R.id.bt_singleTop);
        bt_MainActivity = (Button)findViewById(R.id.bt_MainActivity) ;

        tv_singleTop = (TextView)findViewById(R.id.tv_singleTop);
        tv_singleTop.setText(this.toString());

//        listView_SingleTop = (ListView)findViewById(R.id.listView_singleTop);
//        myAdapter = new MyAdapter();
//        listView_SingleTop.setAdapter(myAdapter);

        bt_SingleTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTopActivity.this,SingleTopActivity.class);
                startActivity(intent);

                tv_singleTop.setText(this.toString());

//                list.add(this.toString());
            }
        });
        bt_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTopActivity.this,TopActivity.class);
                startActivity(intent);

            }
        });

        bt_MainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTopActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });



    }



//    public class MyAdapter extends BaseAdapter {
//
//        private LayoutInflater layoutInflater;
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            ViewHolder viewHolder =null;
//            if ( view == null){
//                viewHolder = new ViewHolder()    ;
//                view = layoutInflater.inflate(R.layout.list_item,null);
//
//                viewHolder.tv_list = (TextView)view.findViewById(R.id.tv_list);
//
//            }else{
//                viewHolder = (ViewHolder)view.getTag();
//            }
//
//            viewHolder.tv_list.setText(list.get(i));
//
//            return null;
//        }
//    }
//    public final class ViewHolder{
//        public TextView tv_list;
//    }
}


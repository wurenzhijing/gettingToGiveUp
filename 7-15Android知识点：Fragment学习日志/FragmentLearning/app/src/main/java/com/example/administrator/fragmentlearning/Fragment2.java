package com.example.administrator.fragmentlearning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Fragment2 extends Fragment{
    private TextView tv_content,tv_message;
    public static final String TAG = "Fragmgent2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG , "onCreateView().....开始执行");
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment2,null);
        if(rootView != null){
            tv_content = (TextView)rootView.findViewById(R.id.tv_content2);
            tv_message = (TextView)rootView.findViewById(R.id.tv_message2);

            tv_content.setText("Fragment2");

        }
        return rootView;
    }

    public   void setData(String str){
        tv_message.setText("Fragment1传递来的值: "+str);
    }




}

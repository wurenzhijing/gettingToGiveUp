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
public class Fragment3 extends Fragment {
    private TextView tv_content;
    public static final String TAG = "Fragmgent3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG , "onCreateView().....开始执行");
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment,null);
        if(rootView != null){
            tv_content = (TextView)rootView.findViewById(R.id.tv_content);
            tv_content.setText("Fragment3");
        }
        return rootView;
    }
}



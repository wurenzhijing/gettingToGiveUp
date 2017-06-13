/**
 * User: wurenzhijing
 * Date: 2016-08-04
 * Time: 13:56
 * FIXME
 */
package com.example.administrator.testLearning;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {
    View rootView ;
    TextView tv_content ;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment , null ) ;
        if (rootView != null){
            tv_content = (TextView)rootView.findViewById(R.id.tv_fragmgent) ;


        }

        return rootView ;

    }

    public void setView(String str){
        tv_content.setText("Fragment\n "+str);
    }
}


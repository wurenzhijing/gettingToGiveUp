package com.example.administrator.fragmentlearning;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Fragment1 extends Fragment {

    public static final String TAG = "Fragmgent1";

    private TextView tv_content;
    private EditText et_content;
    private Button bt_send,bt_sendTOFragment;
    private onMyListener mCallback;


    @Override
    public void onAttach(Activity activity){
        Log.i(TAG , "onAttach().....开始执行");
        super.onAttach(activity);
        try{
             mCallback = (onMyListener)activity ;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnMyListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.i(TAG , "onCreate().....开始执行");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(TAG , "onCreateView().....开始执行");
        super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment1,null);
        if(rootView != null){
            tv_content = (TextView)rootView.findViewById(R.id.tv_content);
            et_content = (EditText)rootView.findViewById(R.id.et_content);

            bt_sendTOFragment = (Button)rootView.findViewById(R.id.bt_sendToFragment);
            bt_send = (Button)rootView.findViewById(R.id.bt_send);

            tv_content.setText("Fragment1");
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.i(TAG , "onActivityCreated().....开始执行");
        super.onActivityCreated(savedInstanceState);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.test(et_content.getText().toString());
                et_content.setText("");
            }
        });

        bt_sendTOFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.test(et_content.getText().toString());
                et_content.setText("");
            }
        });

    }

    @Override
    public void onStart(){
        Log.i(TAG , "onStart().....开始执行");
        super.onStart();
    }

    @Override
    public void onResume(){
        Log.i(TAG , "onResume().....开始执行");
        super.onResume();
    }

    @Override
    public void onPause(){
        Log.i(TAG , "onPause().....开始执行");
        super.onPause();
    }

    @Override
    public void onStop(){
        Log.i(TAG , "onStop().....开始执行");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView().....开始执行");
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG , "onDestroy().....开始执行");
        super.onDestroy();
    }


    public interface onMyListener {
        public void test(String str);
    }

}

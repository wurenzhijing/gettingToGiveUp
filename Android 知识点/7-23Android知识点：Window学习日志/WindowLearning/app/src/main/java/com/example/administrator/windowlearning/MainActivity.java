package com.example.administrator.windowlearning;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    Button bt_window ;
    WindowManager wm ;
    MyImageView iv ;
    WindowManager.LayoutParams params ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_window = (Button)findViewById(R.id.bt_window);
        bt_window.setOnClickListener(myOnClickListener);

        wm = (WindowManager)getSystemService("window");


    }

    public void init(){
        iv = new MyImageView(this);
        iv.setImageResource(R.mipmap.image);
        iv.setMyOnTouchListener(new MyTouchListener());

        params = new WindowManager.LayoutParams();
        //params.type = WindowManager.LayoutParams.TYPE_PHONE ;
        params.format = PixelFormat.RGBA_8888 ;
        params.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT|Gravity.TOP ;
        params.x = 100 ;
        params.y = 100 ;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT ;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT ;

        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


        wm.addView(iv ,params);


    }


    public View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            init();
        }
    };

    public void onDestroy(){
        super.onDestroy();
        wm.removeViewImmediate(iv);
    }

    private class MyTouchListener implements MyImageView.MyOnTouchListener {
        @Override
        public int getTitleHeight() {

            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            int titleBarHeight = contentTop - statusBarHeight;

            return titleBarHeight;
        }

        @Override
        public android.view.WindowManager.LayoutParams getLayoutParams() {
            return  params;
        }
    }
}

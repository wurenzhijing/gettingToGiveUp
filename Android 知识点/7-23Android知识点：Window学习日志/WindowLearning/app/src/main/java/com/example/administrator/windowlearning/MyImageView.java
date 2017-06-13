package com.example.administrator.windowlearning;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/7/24.
 */
public class MyImageView extends ImageView {
    WindowManager mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService("window");
    float mRawX;
    float mRawY;
    float mStartX;
    float mStartY;
    MyOnTouchListener myOnTouchListenr ;
    public MyImageView(Context context) {
        super(context);
    }

    public void setMyOnTouchListener(MyOnTouchListener listenr){
         myOnTouchListenr = listenr ;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int titleHeight = 0;
        if (myOnTouchListenr != null) {
            titleHeight = myOnTouchListenr.getTitleHeight();
        }
        mRawX = event.getRawX();
        mRawY = event.getRawY() - titleHeight;

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateWindowPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateWindowPosition();
                break;
        }
        return true;
    }

    private void updateWindowPosition() {
        if (myOnTouchListenr != null) {
            WindowManager.LayoutParams layoutParams = myOnTouchListenr.getLayoutParams();
            layoutParams.x = (int)(mRawX - mStartX);
            layoutParams.y = (int)(mRawY - mStartY);
            mWindowManager.updateViewLayout(this, layoutParams);
        }
    }


    public interface MyOnTouchListener {
        public int getTitleHeight() ;
        public WindowManager.LayoutParams getLayoutParams();
    }

}

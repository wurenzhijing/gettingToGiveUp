#Window#
Window是Android应用程序的窗口，每个Activity组件都关联一个Window对象，Activity和Window的关系如下所示：
![](http://files.jb51.net/file_images/article/201212/201212030902163.jpg)

##WindowManager##

WindowManager是Android中的窗口机制，是全局唯一的，可以添加view到屏幕，也可以从屏幕删除view。它的对象一端是屏幕，另一端是View。WindowManager继承自ViewManager。WindowManager主要用来管理窗口的一些状态、属性、view增加、删除、更新、窗口顺序、消息收集和处理等。
 
 
	//获取WindowManager
    WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
    //添加View
    wm.addView(iv ,params);
    //移除View
    wm.removeViewImmediate(iv);
    //更新window
    wm.updateViewLayout(this, layoutParams);
##悬浮窗口的实现##
 - 获取WindowManager服务

		windowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
    
 - 设置WindowManager.LayoutParams参数

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
 - 添加view到屏幕
 		wm.addView(iv ,params);

 - 更新View位置
 		
        
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
                WindowManager.LayoutParams layoutParams= myOnTouchListenr.getLayoutParams();
                layoutParams.x = (int)(mRawX - mStartX);
                layoutParams.y = (int)(mRawY - mStartY);
                mWindowManager.updateViewLayout(this, layoutParams);
            }
        }

 - 从屏幕删除View
 		public void onDestroy(){
            super.onDestroy();
            wm.removeViewImmediate(iv);
        }
        





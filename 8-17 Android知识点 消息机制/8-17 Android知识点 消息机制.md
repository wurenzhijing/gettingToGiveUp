#进程与线程——消息机制

##简介

Android中的消息机制主要是指Handler、Message、MessageQuene和Looper的工作机制，用于线程内部和线程之间进行信息交互。

##用途

当在主线程即UI线程进行一些耗时操作，比如联网操作，图片处理、下载，为了不引起ANR异常，我们经常会使用消息机制。

##消息机制
 
 - Message，消息对象，重要属性有arg1和arg2，obj，what，在使用时可new Message()实例化一个Message实例，或者通过Message.obtain()或者Handler.handleMessage()获取Message对象。

		Runnable timeRunnable = new Runnable() {
	        @Override
	        public void run() {
	            Message message = new Message();
	            message.obj = "timeThread";
	            timeHandle.sendMessage(message);
	            timeHandle.postDelayed(timeRunnable, 1000);
	        }
	    };

 - Handler，消息的处理者，主要作用是发送消息，获取和处理消息，在Handler中的handleMessage方法中处理消息。

		Handler timeHandle = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	
	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
	            Date date = new Date(System.currentTimeMillis());
	            String time = format.format(date);
	
	            tv_time.setText(msg.obj + "  " + time);
	        }
	    };

 - MessageQuene，消息队列，被封装在Looper中。

 - Looper 循环，每个线程最多只有一个Looper对象，，Looper内部都有一个被封装的Message Quene，通过Looper.prepare方法和Looper.loop方法可以将当前线程变为Looper线程，Looper线程可以循环工作。

		class HandlerThread extends Thread{
	        @Override
	        public void run() {
	            // 开始建立消息循环
	            Looper.prepare();  // 初始化Looper
	            handler = new Handler(){   // 默认绑定本线程的Looper
	                @Override
	                public void handleMessage(Message msg) {
	                    super.handleMessage(msg);
	                    switch (msg.what){
	                        case 0:
	                            Toast.makeText(MainActivity.this, "子线程收到消息" ,Toast.LENGTH_SHORT).show();
	                    }
	                }
	            };
	            super.run();
	            Looper.loop();  // 启动消息循环
	        }
	

##ThreadLocal

使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本，完美的解决了多线程的并发问题。有四个方法：

 - set(Object value)，设置当前线程的线程局部变量的值。
 - get()，获取当前线程的线程局部变量的值。
 - remove()，删除当前线程的线程局部变量的值。
 - initialValue()，返回当前局部变量的初始值。


	
		ThreadLocal<People> threadLocal = new ThreadLocal<>();
	
	 	 
		new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    threadLocal.set(p1);
	                    String threadName = Thread.currentThread().getName();
	                    People people = threadLocal.get() ;
	                    Log.d("  Thread   ",threadName +"  数据："+people.getName()+" "+people.getGender()+" "+people.getAge());
	                }
	            }).start();
	
	            new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    threadLocal.set(p2);
	                    String threadName = Thread.currentThread().getName();
	                    People people = threadLocal.get() ;
	                    Log.d("  Thread   ",threadName +"  数据："+people.getName()+" "+people.getGender()+" "+people.getAge());
	                    String str = threadName +"  数据："+people.getName()+" "+people.getGender()+" "+people.getAge() ;
	                    Message msg = new Message() ;
	                    msg.obj = str ;
	
	                    localHandle.sendMessage(msg) ;
	                }
	            }).start();
	

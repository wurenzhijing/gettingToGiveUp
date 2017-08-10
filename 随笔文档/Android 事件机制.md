# Android 事件机制

## 概述

 Android 通信机制

 - Binder ， 进程间

 - 事件机制， 进程内部

## 事件机制

 - 消息发送者

 - 消息队列

 - 消息处理循环

![](http://static.codeceo.com/images/2015/08/6bfd037e4716fec12bf1c1bd9010c45c.png)

 - Handle , 消息发送者

### Looper

 消息队列， 和消息处理循环 ， 每个线程只能有一个 Looper

```java
   private static void prepare(boolean quitAllowed) {
   		// 这里限制了 只能是一个 Looper
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }
```

#####  Looper.prepare()

 - 在线程中， <code>Looper.prepare()</code> 多次调用

	```java
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            	// 多次调用 Looper.prepare()
                Looper.prepare();
                Looper.prepare();
                Looper.prepare();
                Log.i(TAG, "run: " + Looper.myLooper().toString());
                Looper.loop();
            }
        });
        thread.start();
    }
```
 运行时会报错， <code>java.lang.RuntimeException: Only one Looper may be created per thread</code>

##### Looper.Looper()

 - 如果只调用 <code>Looper.Looper()</code> ， 而不调用 <code>Looper.prepare()</code>， 运行时会报错 <code>java.lang.RuntimeException: No Looper; Looper.prepare() wasn't called on this thread</code>

 - 如果将 目标代码写在 <code>Looper.Looper()</code> 之后， 那么将不会执行

	```java
    Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: out begin");
                Looper.prepare();
                Log.i(TAG, "run: ");
                Looper.loop();
                Log.i(TAG, "run: out end");

            }
        });
        thread.start();
    ```
    只会出现 run：out begin 和 run： ， 不会出现 run：begin end

 - 看一下 <code>Looper.loop()</code> 的源码
```java
public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        // Make sure the identity of this thread is that of the local process,
        // and keep track of what that identity token actually is.
        Binder.clearCallingIdentity();
        final long ident = Binder.clearCallingIdentity();
		// 这里会无限循环消息队列
        for (;;) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            // This must be in a local variable, in case a UI event sets the logger
            final Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(">>>>> Dispatching to " + msg.target + " " +
                        msg.callback + ": " + msg.what);
            }

            final long traceTag = me.mTraceTag;
            if (traceTag != 0 && Trace.isTagEnabled(traceTag)) {
                Trace.traceBegin(traceTag, msg.target.getTraceName(msg));
            }
            try {
                msg.target.dispatchMessage(msg);
            } finally {
                if (traceTag != 0) {
                    Trace.traceEnd(traceTag);
                }
            }

            if (logging != null) {
                logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
            }

            // Make sure that during the course of dispatching the
            // identity of the thread wasn't corrupted.
            final long newIdent = Binder.clearCallingIdentity();
            if (ident != newIdent) {
                Log.wtf(TAG, "Thread identity changed from 0x"
                        + Long.toHexString(ident) + " to 0x"
                        + Long.toHexString(newIdent) + " while dispatching to "
                        + msg.target.getClass().getName() + " "
                        + msg.callback + " what=" + msg.what);
            }

            msg.recycleUnchecked();
        }
    }
```

这里 Looper.loop() 会无限循环 messageQuene ， 影响性能 ， 线程使用完就要关闭

##### Looper.quit()

源码

```java
// quit()
public void quit() {
    mQueue.quit(false);			// mQuene  -->  MessageQueue
}
// quitSafely
public void quitSafely() {
    mQueue.quit(true); 		// mQuene  -->  MessageQueue
}

	// MessageQueue
	void quit(boolean safe) {
        if (!mQuitAllowed) {
            throw new IllegalStateException("Main thread not allowed to quit.");
        }

        synchronized (this) {
            if (mQuitting) {
                return;
            }
            mQuitting = true;

            if (safe) {
                removeAllFutureMessagesLocked();
            } else {
                removeAllMessagesLocked();
            }

            // We can assume mPtr != 0 because mQuitting was previously false.
            nativeWake(mPtr);
        }
	}
```

 - Looper.quit() ， 使用条件 ， 有Loop ， 有 Handler

	```java
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: out begin");
                Looper.prepare();
                mLooper = Looper.myLooper();
                Log.i(TAG, "run: " + mLooper);
                Message msg  = new Message();
                msg.obj = "s";
                handler.sendMessage(msg);
                Looper.loop();
                Log.i(TAG, "run: out end");

            }
        });
        thread.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: " + msg.obj);
            mLooper.quit();
            Log.i(TAG, "handleMessage: looper quit");
        }
    };
```

 - quit 之后，会执行 Looper.loop() 之后的代码 ， 没有 quit 是永远不会执行的


## Handler

#### 构造器

```java
    public Handler(Callback callback, boolean async) {
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends Handler> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                    klass.getCanonicalName());
            }
        }
		//  获取Looper ， sThreadLocal.get();
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }

    public Handler(Looper looper, Callback callback, boolean async) {
        mLooper = looper;
        mQueue = looper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }
```

初始化四个值

 - mLooper ， 不能为 null ， Looper.myLooper()

 - mQuene ， 消息队列 ， mLooper.mQuene

 - mCallBack ， 处理消息 ， 可以为 null
 ```java
     public interface Callback {
        public boolean handleMessage(Message msg);
    }
    ```

 - mAsynchronous ， 是否异步 ， 默认 false


#### 发送消息

 - 将消息入队列
```java
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            Log.w("Looper", e.getMessage(), e);
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }
```

 - 设置 msg.target
```java
    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;			// message 的 target是 这个handler
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }
```

#### 处理消息

  上面讲消息放进 MessageQuene ，然后 Looper.loop() 会去循环消息队列，由Handler处理消息 ， Looper.looper() 中 <code>msg.target.dispatchMessage(msg);</code> 处理消息 ， 上面发送消息时设置了 <code>msg.target = =this</code>

 - dispatchMessage , 调度消息
 ```java
  public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {   // Handler 的 callback  ， 用户实现
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);		// Handler handlemessage ，用户实现
        }
    }

    // Message 的 callback
    private static void handleCallback(Message message) {
        message.callback.run();
    }
    ```

## Thread

#### Thread 的 <code>start()</code> 和 <code>run()</code>

看一下例子

```java
		// start
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: out begin");
                Looper.prepare();
                Log.i(TAG, "run: ");
                Looper.loop();
                Log.i(TAG, "run: out end");
                Looper.myLooper().quit();

            }
        });
        thread.start();

		// run
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: ");

            }
        });
        thread1.run();
```


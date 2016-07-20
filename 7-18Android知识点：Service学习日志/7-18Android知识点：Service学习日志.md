Service
======

  Service是Android四大组件之一，是在一段不定的时间运行在后台，不和用户交互应用组件。每个Service必须在manifest中来声明，可以通过context.startservice和context.bindserverice来启动。

##Service的生命周期##

- 被启动的服务的生命周期：如果一个service被某个Activity调用startService方法启动，不管activity是否绑定到该service，该service都在后台运行，直到activity调用stopService，或自身的stopSelf方法。
  >onCreate() --> onStartCommand(可多次调用) --> onDestroy()
- 被绑定的服务的生命周期：如果一个service被activity调用bindService绑定启动，不管bindService调用几次，onCreate()执行一次，onStartCommand()不会被调用，当unbindService被调用，或者Context不存在，Service停止。
  >>onCreate() --> onBind() --> onUnbind() --> onDestroy()
- 被启动又被绑定的服务的生命周期：如果一个service被启动又被绑定，它会一直在后台运行，onCreate只会调用一次，onStartCommand()调用多次，只有先调用onUnbind()，再调用onStop()，服务才能停止，或者调用service的stopSelf来停止服务。
>onCreate() --> onStartCommand() --> onUnbind() --> onDestroy()

##Service与Activity的通信##
 #####继承Binder类#####
 通过继承Binder类来实现对Activity的通信。如下代码在Service中定义内部类继承了Binder，在LocalBinder这个内部类中添加getState()方法。

	public class MyLocalService extends Service {

    private NotificationManager mNF;

    public static String NOTIFICATION ="Service Start";
    public static final String TAG = "MyLocalService";

    private final IBinder mBinder = new LocalBinder() ;

    public class LocalBinder extends Binder{
        MyLocalService getService(){
            return MyLocalService.this;
        }

        public String getState(){
            String str = "Service has connencted";
            return str;
        }
    }
在MainActivity中，通过IBinder来获取Service对象，再去调用getState()方法。

	serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyLocalService.LocalBinder mBinder = (MyLocalService.LocalBinder)iBinder;

                Log.i(TAG , mBinder.getService().toString());
                myLocalService = (MyLocalService.LocalBinder)iBinder;

                String str = myLocalService.getState();

                Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();

                isBind = true ;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBind = false ;
            }
        };

##IntentService##

IntentService是异步处理服务，一般用来处理异步请求，activity可以通过startService(intent)来启动service，IntentService在onCreate中通过HanlderThread开启线程来处理所有的请求任务。可以防止线程阻塞。

在MainActivity中定义两个Intent用来启动IntentService，在IntentService中的onHandleIntent方法中会按顺序来执行Intent，即它有一个工作队列，每次只执行一个工作线程，按顺序来执行。

    						MainActivity中的启动IntentService

    private View.OnClickListener myStartIntentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent1 = new Intent(MainActivity.this,MyIntentService.class);
            intent1.putExtra("name","first");
            startService(intent1);

            Intent intent2 = new Intent(MainActivity.this,MyIntentService.class);
            intent2.putExtra("name","second");
            startService(intent2);

        }
    };

    					IntentService中的 onHandleIntent方法

    protected void onHandleIntent(Intent intent) {

        Log.i(TAG , "onHandleIntent()...");

        String str = intent.getStringExtra("name");

        switch (str){
            case "first":
                Log.i(TAG,"it is First Intent  "+intent.toString());
                break;
            case "second":
                Log.i(TAG,"it is seconf intent  "+intent.toString());
                break;
        }
    }
在Logcat中可以看出其执行顺序，按intent的顺序来执行，而且每个线程只有一个工作任务。

	07-18 17:48:55.220 6018-6018/com.example.administrator.servicelearning I/MyIntentService: onCreate()...
	07-18 17:48:55.220 6018-6166/com.example.administrator.servicelearning I/MyIntentService: onHandleIntent()...
	07-18 17:48:55.221 6018-6166/com.example.administrator.servicelearning I/MyIntentService: it is First Intent  Intent { cmp=com.example.administrator.servicelearning/.MyIntentService (has extras) }
	07-18 17:48:55.222 6018-6166/com.example.administrator.servicelearning I/MyIntentService: onHandleIntent()...
	07-18 17:48:55.222 6018-6166/com.example.administrator.servicelearning I/MyIntentService: it is seconf intent  Intent { cmp=com.example.administrator.servicelearning/.MyIntentService (has extras) }
	07-18 17:48:55.226 6018-6018/com.example.administrator.servicelearning I/MyIntentService: onDestroy()...







#进程与线程——进程

##进程通信的障碍

由于Android系统应用程序之间不能共享内存，因此在不同应用程序之间无法互相访问，各个应用的数据都在自己的内存中，这也是为了安全。

##进程通信的方法

为了解决以上问题，Android提供了四种方式来跨进程通信，分别是：

  - 访问其他应用的Activity。
  - Broadcast，一种被动跨进程通讯的方式。当某个程序向系统发送广播时，其他的应用程序只能被动地接收广播数据。
  - ContentProvider，提供了一种在多个应用程序之间数据共享的方式（跨进程共享数据）。
  - AIDL服务，服务（Service）是android系统中非常重要的组件。Service可以脱离应用程序运行。AIDL服务实现进程通信是根据Binder机制。
  
##Binder

![](http://hi.csdn.net/attachment/201107/19/0_13110996490rZN.gif)

Binder通信是一种client-server的通信结构，B定义了四个角色：Server，Client，ServiceManager以及Binder驱动。其中Server，Client，ServiceManager运行于用户空间，Binder驱动运行于内核空间，Service Manager是一个守护进程，用来管理Server，并向Client提供查询Server接口的能力，Client和Server之间的进程间通信通过Binder驱动程序间接实现。

##AIDL

AIDL(Android Interface Definition Language)是一种IDL语言，用于生成可以在Android设备上两个进程之间进行进程间通信(interprocess communication, IPC)的代码，AIDL IPC机制是面向接口的，像COM或Corba一样，但是更加轻量级。
 
 - 服务端
 
		//AIDL
		interface IMyService {
		    /**
		     * Demonstrates some basic types that you can use as parameters
		     * and return values in AIDL.
		     */
		      String getValue(in Person person);
		      Person getPerson();
		
		}
	
	    // 
		public class MyService extends Service {
		
		    class MyBinder extends IMyService.Stub{
		
		        @Override
		        public String getValue(Person person) throws RemoteException {
		            person.setName(person.getName()+"添加的名字 hhh");
		            return person.toString();
		        }
		
		        @Override
		        public Person getPerson() throws RemoteException {
		            Person person = new Person();
		            person.setName("hebin");
		            person.setGender("nan");
		            person.setAge(22);
		            return person;
		        }
		    }
		    @Override
		    public IBinder onBind(Intent intent) {
		        // TODO: Return the communication channel to the service.
		        return new MyBinder();
		    }
 
 - 客户端

		// 客户端连接服务
		aidlConnection = new ServiceConnection() {
		            @Override
		            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
		                iMyService = IMyService.Stub.asInterface(iBinder);
		                aidlConState = true;
		                Log.d("---", iMyService.toString() + " " + (iMyService != null));
		            }
		
		            @Override
		            public void onServiceDisconnected(ComponentName componentName) {
		                aidlConState = false;
		            }
		        };


##Messager

Messager实现IPC通信，底层也是使用了AIDL方式，Messager方式是利用Handler形式处理，因此，它是线程安全的。

 - 服务端

		// 服务端
		Messenger mMessenger = new Messenger(new mHandler()) ;
		
		    class mHandler extends Handler {
		        @Override
		        public void handleMessage(Message msg) {
		            // message to client
		            Bundle bundle = (Bundle)msg.obj ;
		            Message msgBack = Message.obtain(msg) ;
		
		            switch(msg.what){
		                // client msg
		                case MSG:
		                    msgBack.what = MSG ;
		                    try{
		                        Thread.sleep(1000);
		                        final Messenger callback = msg.replyTo ;
		                        //msgBack.arg1 = msg.arg1 +100 ;
		                        Bundle bundle1 = new Bundle();
		                        bundle1.putString("callback", "服务端：已收到信息  \n  返回信息："+bundle.get("name"));
		                        //Message msg2server = Message.obtain(null, MSG);
		                        msgBack.obj = bundle1;
		
		                        //msgBack.obj = "服务端：已收到信息  \n  返回信息："+bundle.get("name");
		
		                        String str = msgBack.obj.toString() ;
		                        SharedPreferences.Editor editor = getSharedPreferences("message" , Context.MODE_PRIVATE).edit() ;
		                        editor.putString("msg" , str) ;
		                        editor.commit() ;
		
		                        Log.d("Server",callback+"");
		                        if(callback!=null){
		                            callback.send(msgBack);
		                        }
		
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    } catch (RemoteException e) {
		                        e.printStackTrace();
		                    }
		                    Toast.makeText(getApplicationContext() , "messenger" , Toast.LENGTH_SHORT).show();
		                    break;
		            }
		            super.handleMessage(msg);
		        }
		    }
		
		    public MessageService() {
		    }
		
		    @Override
		    public IBinder onBind(Intent intent) {
		        return mMessenger.getBinder() ;
		    }
		}


 - 客户端

		// 客户端 
		ServiceConnection msgConnection = new ServiceConnection() {
	        @Override
	        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
	            msgService = new Messenger(iBinder);
	            msgConState = true;
	            Log.d("-----", "connect success");
	            Toast.makeText(MainActivity.this, "connect success!", Toast.LENGTH_SHORT).show();
	        }
	
	        @Override
	        public void onServiceDisconnected(ComponentName componentName) {
	            msgService = null;
	            msgConState = false;
	            Log.d("-----", "connect failed");
	            Toast.makeText(MainActivity.this, "connect fail!", Toast.LENGTH_SHORT).show();
	        }
	    };

		// 绑定服务
		Intent intent = new Intent("com.example.administrator.messageserver");
        intent.setPackage("com.example.administrator.messageserver");
        bindService(intent, msgConnection, Context.BIND_AUTO_CREATE);
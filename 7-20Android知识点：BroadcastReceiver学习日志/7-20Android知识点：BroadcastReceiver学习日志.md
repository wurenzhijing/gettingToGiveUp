#BroadcastReceiver#

Android四大组件之一，本质上是一种全局的监听器，用于监听系统全局的广播消息，可以接受来自系统和应用的广播Intent，实现不同组件之间的通信。
##生命周期##
   调用对象 -->  实现onReceive() -->  结束                             
BroadcastReceiver的生命周期最多只有10s，即从对象开始它开始，到onReceive执行结束，BroadcastReceiver就销毁，超过10s，系统会弹出ANR的错误。
##注册##
###静态注册###
这种注册方法实在AndroidManifest.xml文件中注册。例如：

	<!-- 在配置文件中注册BroadcastReceiver能够匹配的Intent -->
	<receiver android:name=".MyBroadcastReceiver">
            <intent-filter>
                <action android:name="com.android.aaaaaa" />
            </intent-filter>
    </receiver>
	
###动态注册###
这种方式是通过在Java代码中注册，这种方式需要手动关闭，在activity结束之前使用unregisterReceivr(mReceiver)移除广播。

	//通过代码的方式动态注册BroadcastReceiver	
	@Override
    protected void onResume(){
        super.onResume();
        IntentFilter intentFilter= new IntentFilter("com.android.aaaaaa");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    //取消注册BroadcastReceiver
	 protected void onPause(){
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
创建BroadcastReceiver的子类，重写onReceive方法。
	
	class  LBroadcastReceivere extends BroadcastReceiver  {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            tv_receive.setText(message);

            Log.i("--------",message);
        }
    }

	
##广播的类型##
Broadcast有两种类型，即普通广播和有序广播。

 - Normal broadcasts（普通广播）：Normal broadcasts是完全异步的可以同一时间被所有的接收者接收到。
 - Ordered broadcasts（有序广播）：Ordered broadcasts的接收者按照一定的优先级进行消息的接收。如：A,B,C的优先级依次降低，那么消息先传递给A，在传递给B，最后传递给C。

	
		<receiver android:name=".Ordered1BroadcastReceiver">
            <intent-filter android:priority="90">
                <action android:name="com.android.bbbbbb"/>
            </intent-filter>
        </receiver>

        <receiver android:name=".Ordered2BroadcastReceiver">
            <intent-filter android:priority="40">
                <action android:name="com.android.cccccc"/>
            </intent-filter>
        </receiver>
如上，静态注册了两个BroadcastReceiver，其优先级分别为90和40。
	
	
		public class Ordered1BroadcastReceiver extends BroadcastReceiver {
    	   @Override
   		   public void onReceive(Context context, Intent intent) {
               String str = intent.getStringExtra("message");
        
               Toast.makeText(context,"主级广播静态注册接受的信息： "+str,Toast.LENGTH_LONG).show();
    	   }
		}
	
	
	

        public class Ordered2BroadcastReceiver extends BroadcastReceiver {
           @Override
           public void onReceive(Context context, Intent intent) {
                String str = intent.getStringExtra("message");

                Toast.makeText(context,"次级广播静态注册接受的信息： "+str,Toast.LENGTH_SHORT).show();
         
           }
        }

这是两个BroadcastReceiver的实现,重写了onReceive方法。
	 		
            Intent intent1 = new Intent();
            intent1.setAction("com.android.bbbbbb");
            intent1.putExtra("message",et_message.getText().toString());
            sendOrderedBroadcast(intent1,null);

            Log.i(this.toString(), intent1.toString());


            Intent intent2 = new Intent();
            intent2.setAction("com.android.cccccc");
            intent2.putExtra("message",et_message.getText().toString());
            sendOrderedBroadcast(intent2,null);

            Log.i(this.toString(), intent2.toString());

这是发送有序广播的代码。其执行结果如下：
	
	
	
	07-20 14:33:39.329 22506-22506/com.example.administrator.broadcastreceiverlearning I/------: 主级广播静态注册接受的信息： 464646
	07-20 14:33:39.349 22506-22506/com.example.administrator.broadcastreceiverlearning I/------: 次级广播静态注册接受的信息： 464646
可以看出它是按照优先级的顺序来接受广播。
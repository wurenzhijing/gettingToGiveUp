#Intent#

##Intent的介绍##
Android中提供了Intent机制实现应用间的交互与通讯，可以在activity、broadcastReceiver和service之间进行通信。Intent是一种运行绑定的机制，它能在程序运行过程中连接两个不同的组件。
![](http://images.cnitblog.com/blog/25064/201412/051436115768172.png)
##Intent的属性##
1. Action(动作)：用来变现意图的行动
Action是用户定义的字符串，用于描述一个Android应用程序组件。 在AndroidManifest中定义Activity时，可以在其<intent-filter>节点指定一个Action列表用于辨识Activity所能接受的“动作”。 
2. Component(组件)：目的组件
Component属性明确指定Intent的目标组件的类名称。当指定了这个组件以后，其他属性都是可选的。
3. Category(类别)：用来表现动作的类别，Category属性也是在<intent-filter>中声明。
4. Data(数据)：表示动作要操纵的数据，在<intent-filter>中声明。
5. Type(数据类型)：Intent的数据类型。
6. Extras(扩展信息)：Intent的附加数据。使用extras可以为组件提供扩展信息。
7. Flags(标志位)：期望这个Intent运行的模式。
##显式Intent和隐式Intent##
 - 显式Intent，明确指出此Intent启动的是哪个Activity。
 	
		Intent intent = new Intent();  
		intent.setClass(MainActivity.this, OtherActivity.class);  
		startActivity(intent);
 - 隐式Intent，并没有指出要启动哪个Activity，需要系统自动判断并启动匹配的Activity。
 		
		Intent intent = new Intent();  
		intent.setAction(Intent.ACTION_CALL);  
		startActivity(intent); 
##Intent启动组件的方法##
 - 启动Activity，使用startActivity()方法，在Intent中指定Component，使用putExtras()方法添加附加信息。
 					
			Intent intent = new Intent(MainActivity.this, OtherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("message", et_message.getText().toString());
            bundle.putParcelable("book", pBook);
            intent.putExtras(bundle);
            startActivity(intent);

 - 启动Broadcast，使用sendBroadcast()方法，使用putExtras()方法来添加附件信息，setAction来添加动作。
	
			Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("message", et_message.getText().toString());
            bundle.putSerializable("book", mBook);
            intent.putExtras(bundle);
            intent.setAction("aaaaaa");
            sendBroadcast(intent);

 - 启动Service，使用startService()方法来启动服务，在Intent中指定Component，使用putExtras()方法来添加附件信息。
	 
			Intent intent = new Intent(MainActivity.this, MyService.class);
            intent.putExtra("message", et_message.getText().toString());
            startService(intent);
##Intent可传递的数据类型##
 - 简单数据类型
	
		Intent putExtra(String name, int[] value)  
		Intent putExtra(String name, float value)  
		Intent putExtra(String name, byte[] value)  
		Intent putExtra(String name, long[] value)  
		Intent putExtra(String name, float[] value)  
		Intent putExtra(String name, long value)  
		Intent putExtra(String name, String[] value)  
		Intent putExtra(String name, boolean value)  
		Intent putExtra(String name, boolean[] value)  
		Intent putExtra(String name, short value)  
		Intent putExtra(String name, double value)  
		Intent putExtra(String name, short[] value)  
		Intent putExtra(String name, String value)  
		Intent putExtra(String name, byte value)  
		Intent putExtra(String name, char[] value)  
		Intent putExtra(String name, CharSequence[] value) 
 - 传递Bundle类型
  	
			Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("message", et_message.getText().toString());
            intent.putExtras(bundle);
            intent.setAction("aaaaaa");
            sendBroadcast(intent);
 - 传递Serializable对象
   		
	    //实现  Serializable 接口
		public class SerializableBook implements Serializable {
    		String name ;
    		String author ;
    		String ISBN;
    		String price;

   			public SerializableBook(String name , String author, String ISBN, String price){
        		this.name = name;
       	 		this.author = author;
       	 		this.ISBN = ISBN;
        		this.price = price;
    		}

        // Intent传递Serializable数据
		View.OnClickListener broadcastOnclickListener = new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            Intent intent = new Intent();
	            Bundle bundle = new Bundle();
	            bundle.putString("message", et_message.getText().toString());
	            bundle.putSerializable("book", mBook);
	            intent.putExtras(bundle);
	            intent.setAction("aaaaaa");
	            sendBroadcast(intent);
	        }
	    };
		// 接收Intent传递的数据
		class MyReceiver extends BroadcastReceiver {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            Bundle bundle = intent.getExtras();
	            SerializableBook book = (SerializableBook)bundle.getSerializable("book");
	            String str = bundle.getString("message");
	            Toast.makeText(MainActivity.this, "intent通过广播传递的数据： "+str+"\nintent通过广播传递的对象 "+ book.getName().toString()+" "+book.getAuthor()+" "+book.getISBN()+" "+book.getPrice()
	                    , Toast.LENGTH_SHORT).show();
	
	        }
	    };
 - 传递Parcelable对象
 		
        //  实现 Parcelable 接口
		public class ParcelableBook implements Parcelable {

    		String name ;
    		String author ;
    		String ISBN;
    		String price;

   		 public static final ParcelableBook.Creator<ParcelableBook> CREATOR = new Creator<ParcelableBook>() {
        		@Override
        		public ParcelableBook createFromParcel(Parcel parcel) {
            		ParcelableBook book = new ParcelableBook();
            		book.name = parcel.readString();
		            book.author = parcel.readString();
		            book.ISBN = parcel.readString();
		            book.price = parcel.readString();
		            return book;
		        }
		
		        @Override
		        public ParcelableBook[] newArray(int i) {
		            return new ParcelableBook[i];
		        }
		    };
		    @Override
		    public int describeContents() {
		        return 0;
		    }
		
		    @Override
		    public void writeToParcel(Parcel parcel, int i) {
		        parcel.writeString(name);
		        parcel.writeString(author);
		        parcel.writeString(ISBN);
		        parcel.writeString(price);
		    }

		// Intent传递Parcelable数据 启动Activity
		View.OnClickListener activityOnclickListener = new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            Intent intent = new Intent(MainActivity.this, OtherActivity.class);
	            Bundle bundle = new Bundle();
	            bundle.putString("message", et_message.getText().toString());
	            bundle.putParcelable("book", pBook);
	            intent.putExtras(bundle);
	            startActivity(intent);
	        }
	    };

		//接收Intent传递的数据
		Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ParcelableBook book = (ParcelableBook) bundle.getParcelable("book");
        String str = bundle.getString("message");
        tv_activityReceive.setText("intent传递的数据： "+str+"\nintent传递的对象:  "+ book.getName().toString()+" "+book.getAuthor()+" "+book.getISBN()+" "+book.getPrice());


 -
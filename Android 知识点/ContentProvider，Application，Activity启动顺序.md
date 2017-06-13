#ContentProvider，Activity，Application执行顺序#

##demo中的代码##

  -  Application的入口
  		public class MyApplication extends Application {

            static final String TAG = "MyApplication" ;

            public MyApplication() {
                super();
            }
            @Override
            public void onCreate() {
                super.onCreate();
                Log.i(TAG, "   onCreate().....");
            }
  -  ContentProvider的入口

        public class MyProvider extends ContentProvider {

            MyDBHelper myDBHelper ;
            SQLiteDatabase db;

            private static final UriMatcher sMatcher ;

            static{
                sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
                sMatcher.addURI(BookU.AUTOHORITY,BookU.TABLE_NAME, BookU.ITEM);
                sMatcher.addURI(BookU.AUTOHORITY, BookU.TABLE_NAME+"/#", BookU.ITEM_ID);

            }

            @Override
            public boolean onCreate() {
                Log.i("ContentProvider","  onCreate().....");
                this.myDBHelper = new MyDBHelper(this.getContext());
                return true;
            }
  	
  -  Activity的入口

        @Override
    	protected void onCreate(Bundle savedInstanceState) {
            Log.i(TAG,"  onCreate()....");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            
##运行结果##

		07-24 23:26:20.562 10102-10102/com.example.administrator.contentprovicer I/ContentProvider:   onCreate().....
		07-24 23:26:20.571 10102-10102/com.example.administrator.contentprovicer I/MyApplication:    onCreate().....
		07-24 23:26:20.594 10102-10102/com.example.administrator.contentprovicer I/MainActivity:   onCreate()....
        
从运行结果来看，可以得出结论：最先启动的ContentProvider，其次是Application，最后是Activity。
##思考##
ContentProvider，Application，Activity的启动都是在ActivityManagerService中完成的，而ContentProvider的启动ActivityManagerService是由installSystemProviders()方法来实现的，它是把ContentProvider安装到system_server进程中，Application的启动则是在之后进行的。

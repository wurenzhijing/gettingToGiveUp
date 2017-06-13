#Android单元测试——Robolectric框架


##简介##

Robolectric是一个允许单元测试脱离Android的SDK，直接运行于JVM之上的单元测试框架，可以像运行普通的jUnit测试一样在IDE中或者在终端使用构建脚本运行我们的测试，基于Robolectric的测试需要使用其特定的test runner（RobolectricTestRunner）来运行。

##引入Robolectric框架##

 -  添加依赖
	 	
		testCompile 'junit:junit:4.12'
		testCompile 'org.mockito:mockito-core:1.9.5'
	    testCompile 'org.robolectric:robolectric:3.0'
	    testCompile 'org.robolectric:robolectric-annotations:3.0'
	    //robolectric针对support-v4的shadows
	    testCompile 'org.robolectric:shadows-support-v4:3.0'
	    testCompile 'com.jayway.awaitility:awaitility:1.7.0'
 -  更改配置，将Build Variant里面的Test Artifact选择为Unit Tests，working directory设置为$MODULE_DIR$。
 -  在测试用例之前加上注解

		@RunWith(RobolectricGradleTestRunner.class)
		@Config(constants = BuildConfig.class ,sdk = 18)
		public class MainActivityTest {
 		
		}

##Activity的测试##


  - 创建Activity实例

		@Test
	    public void testActivity(){
	        assertNotNull(activity);
	        assertEquals(activity.getTitle(),"InentLearning");
	    }
 -  生命周期
		
		@Test
	    public void testLifecycle(){
	        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class).create().start() ;
	        Activity activity =  activityController.get() ;
	        TextView tv = (TextView)activity.findViewById(R.id.tv_life ) ;
	        assertEquals("onCreate" , tv.getText().toString());
	
	        activityController.resume() ;
	        assertEquals("onResume" , tv.getText().toString());
	
	        activityController.destroy() ;
	        assertEquals("onDestroy" , tv.getText().toString());
	    }
 -  Activity的跳转
 
		@Test
	    public void testStartActivity(){
	        et_content.setText("dsafsgagagaggdsa");
	        btn_activity.performClick();
	        assertTrue(btn_activity.isEnabled());
	
	        Intent intent_activity = new Intent( activity, OtherActivity.class) ;
	        Bundle bundle = new Bundle();
	        bundle.putString("message", et_content.getText().toString());
	        bundle.putParcelable("book", pBook);
	        intent_activity.putExtras(bundle);
	
	        Intent resultIntent = ShadowApplication.getInstance().getNextStartedActivity();
	        assertEquals(intent_activity , resultIntent) ;
	    }
 -  UI组件状态
		
		btn_broadcast.performClick() ;
        assertTrue(btn_broadcast.isEnabled());

 -  Toast
		
		assertEquals(ShadowToast.getTextOfLatestToast() , "intent通过广播传递的数据： dsafsgagagaggdsa\nintent通过广播传递的对象: "
                + sharedPreferences.getString("book_name","")
                +" "+sharedPreferences.getString("book_author","")
                +" "+sharedPreferences.getString("book_ISBN","")
                +" "+sharedPreferences.getString("book_price",""));
 -  Fragment的测试

	 	@Test
	    public void testFragment(){
	        MyFragment myFragment = new MyFragment() ;
	        SupportFragmentTestUtil.startFragment(myFragment);
	        assertNotNull(myFragment.getView());
	    }


##Broadcast的测试##


 	@Test
    public void testBroadcast(){
        et_content.setText("dsafsgagagaggdsa");
        btn_broadcast.performClick() ;
        assertTrue(btn_broadcast.isEnabled());

        ShadowApplication shadowApplication = ShadowApplication.getInstance() ;
        String action = "aaaaaa" ;
        Intent intent = new Intent(action);
        Bundle bundle = new Bundle();
        bundle.putString("message", et_content.getText().toString());
        bundle.putSerializable("book", mBook);
        intent.putExtras(bundle);
        //  test wheather rigister
        assertTrue(shadowApplication.hasReceiverForIntent(intent));

        MyReceiver myReceiver = new MyReceiver();
        myReceiver.onReceive(RuntimeEnvironment.application ,intent);
        //  test  onReceive result
        SharedPreferences sharedPreferences = shadowApplication.getSharedPreferences("book" , Context.MODE_PRIVATE);
        assertEquals( "java编程思想" ,sharedPreferences.getString("book_name",""))  ;

        //  test Toast
        assertEquals(ShadowToast.getTextOfLatestToast() , "intent通过广播传递的数据： dsafsgagagaggdsa\nintent通过广播传递的对象: "
                + sharedPreferences.getString("book_name","")
                +" "+sharedPreferences.getString("book_author","")
                +" "+sharedPreferences.getString("book_ISBN","")
                +" "+sharedPreferences.getString("book_price",""));
    }

 
##Service的测试##


	@Test
    public void testService(){
        btn_service.performClick() ;
        assertTrue(btn_service.isEnabled());

        et_content.setText("dsafsgagagaggdsa");

        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent intent = new Intent(activity, MyService.class);
        intent.putExtra("message", et_content.getText().toString());
        shadowApplication.startService(intent);

        Log.i("-----------" , intent+"") ;

        MyService s = new MyService() ;
        int i = s.onStartCommand(intent , 0, 0) ;
        Log.i("------jj-----" , "   "+i) ;
        SharedPreferences sharedPreferences = shadowApplication.getSharedPreferences("message" , Context.MODE_PRIVATE);
        assertEquals( "dsafsgagagaggdsa" ,sharedPreferences.getString("content",""))  ;
    }
##数据库的测试##
 - 添加数据

		@Test
	    public void save(){
	        Book book = new Book();
	        book.setName("android");
	        book.setAuthor("heibn");
	        book.setIsbn("80543_35");
	        book.setPrice(78.00);
	        long resault = new BookDao(context).save(book) ;
	        assertTrue(resault>0) ;
	    }
 - 删除数据
 	
		@Test
	    public void delete(){
	        Book book = new Book();
	        book.setName("androidstudio");
	        book.setAuthor("heibin");
	        book.setIsbn("8054d3_35");
	        book.setPrice(748.00);
	        new BookDao(context).save(book);
	
	        int result = new BookDao(context).delete("androidstudio");
	        assertTrue(result>0);
	    }

 - 查询数据
		
		@Test
	    public void getAll(){
	        Book book = new Book();
	        book.setName("androidstudio");
	        book.setAuthor("heibin");
	        book.setIsbn("8054d3_35");
	        book.setPrice(748.00);
	        new BookDao(context).save(book);
	
	        Book book1 = new Book();
	        book1.setName("androidstudio");
	        book1.setAuthor("heibin");
	        book1.setIsbn("8054d3_35");
	        book1.setPrice(999.00);
	        new BookDao(context).save(book1);
	
	        Book book2 = new Book();
	        book2.setName("androidstudio1.0");
	        book2.setAuthor("heijbin");
	        book2.setIsbn("805e454d3_35");
	        book2.setPrice(666.00);
	        new BookDao(context).save(book2);
			//  查询所有数据
	        List<Book> list = new BookDao(context).getAll() ;
	        assertEquals(list.size() , 3);
			//  查询单个数据
			Book newbook = new BookDao(context).query("androidstudio");
	    	assertEquals((int)newbook.getPrice()+"" , (int)(999.00)+"");
	    }

		

 - 修改数据

 
		@Test
	    public void update(){
	        Book book = new Book();
	        book.setName("androidstudio");
	        book.setAuthor("heibin");
	        book.setIsbn("8054d3_35");
	        book.setPrice(748.00);
	        new BookDao(context).save(book);
	
	        Book book1 = new Book();
	        book1.setName("androidstudio");
	        book1.setAuthor("heibin");
	        book1.setIsbn("8054d3_35");
	        book1.setPrice(999.00);
	        long result = new BookDao(context).update(book1);
	
	        assertEquals(result , 1);
	
	        Book newbook = new BookDao(context).query("androidstudio");
	        assertEquals((int)newbook.getPrice()+"" , (int)(999.00)+"");
	    }

##ContentProvider的测试##
对ContentProvider的测试需要借助营子对象ShadowContentProvider来实现，具体如下：


	@RunWith(RobolectricGradleTestRunner.class)
	@Config(constants = BuildConfig.class ,sdk =18)
	public class BookContentProviderTest {
	
	    ContentResolver mContentResolver ;
	    ShadowContentResolver mShadowContentResolver ;
	    BookContentProvider mProvider ;
	    String AUTHORITY = "com.example.administrator.testLearning.provider" ;
	    Uri URI_INFO = Uri.parse("content://"+ AUTHORITY +"/"+ BookSQLiteHelper.TABLE_NAME) ;
	
	
	    @Before
	    public void setUp(){
	        ShadowLog.stream = System.out ;
	        Application application = RuntimeEnvironment.application ;
	        mProvider = new BookContentProvider(application) ;
	
	        mContentResolver = application.getContentResolver() ;
	        // 创建ContentProvider的  shadow 对象
	        mShadowContentResolver = Shadows.shadowOf(mContentResolver) ;
	        mProvider.onCreate() ;
	        // 注册 ContentProvider对象 和 Uri
	        ShadowContentResolver.registerProvider(AUTHORITY , mProvider);
	    }
	
	    @After
	    public void tearDown(){
	        Field instance;
	        try {
				//  每次操作完成之后  通过反射原理取出BookSQLiteHelper，清空
	            instance = BookSQLiteHelper.class.getDeclaredField("bookSQLiteHelper");
	            Log.d("------",instance+"");
	            instance.setAccessible(true);
	            instance.set(null, null);
	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
		//  查询数据
	    @Test
	    public void query(){
	        Book book = new Book();
	        book.setName("android");
	        book.setAuthor("heibn");
	        book.setIsbn("8054335");
	        book.setPrice(78.00);
	        ContentValues contentValues = new ContentValues() ;
	        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
	        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
	        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
	        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());
	
	        mShadowContentResolver.insert(URI_INFO , contentValues) ;
	
	        Book book1 = new Book();
	        book1.setName("androidstudio");
	        book1.setAuthor("heibin");
	        book1.setIsbn("65465");
	        book1.setPrice(999.00);
	        ContentValues cv = new ContentValues() ;
	        cv.put(BookSQLiteHelper.BOOK_NAME , book1.getName());
	        cv.put(BookSQLiteHelper.BOOK_AUTHOR , book1.getAuthor());
	        cv.put(BookSQLiteHelper.BOOK_ISBN ,book1.getIsbn());
	        cv.put(BookSQLiteHelper.BOOK_PRICE, book1.getPrice());
	
	        mShadowContentResolver.insert(URI_INFO , cv) ;
	        //查询所有数据
	        Cursor cursor = mShadowContentResolver.query(URI_INFO , null ,null ,null ,null);
	        assertEquals(cursor.getCount() ,2) ;
	
	        // 查询ISBN为 8054335 的数据
	        Uri uri = ContentUris.withAppendedId(URI_INFO , 8054335) ;
	        Cursor cursor1 = mShadowContentResolver.query(uri , null , null , null , null);
	        assertEquals(cursor1.getCount() , 1);
	
	        cursor.close();
	        cursor1.close();
	    }
		//  添加数据
	    @Test
	    public void insert(){
	        Book book = new Book();
	        book.setName("android");
	        book.setAuthor("heibn");
	        book.setIsbn("8054335");
	        book.setPrice(78.00);
	        ContentValues contentValues = new ContentValues() ;
	        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
	        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
	        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
	        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());
	        mShadowContentResolver.insert(URI_INFO , contentValues) ;
	
	        Cursor cursor = mShadowContentResolver.query(URI_INFO , null ,BookSQLiteHelper.BOOK_ISBN + "=?" ,new String[]{"8054335"} , null );
	        assertEquals(cursor.getCount() , 1 );
	        cursor.close();
	    }
	    //  修改数据
	    @Test
	    public void updata(){
	        Book book = new Book();
	        book.setName("android");
	        book.setAuthor("heibn");
	        book.setIsbn("8054335");
	        book.setPrice(78.00);
	        ContentValues contentValues = new ContentValues() ;
	        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
	        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
	        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
	        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());
	        Uri uri = mShadowContentResolver.insert(URI_INFO , contentValues) ;
	
	        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , "hhhhhh");
	        int result = mShadowContentResolver.update( uri , contentValues ,null ,null) ;
	        assertEquals(result ,1 );
	
	        Cursor cursor = mShadowContentResolver.query(URI_INFO ,null ,BookSQLiteHelper.BOOK_ISBN +"=?" , new String[]{"8054335"} , null) ;
	        cursor.moveToFirst() ;
	        String author = cursor.getString(cursor.getColumnIndex(BookSQLiteHelper.BOOK_AUTHOR));
	        assertEquals(author , "hhhhhh");
	        cursor.close();
	    }
		//  删除数据
	    @Test
	    public void delete(){
	
	        Book book = new Book();
	        book.setName("android");
	        book.setAuthor("heibn");
	        book.setIsbn("8054335");
	        book.setPrice(78.00);
	        ContentValues contentValues = new ContentValues() ;
	        contentValues.put(BookSQLiteHelper.BOOK_NAME , book.getName());
	        contentValues.put(BookSQLiteHelper.BOOK_AUTHOR , book.getAuthor());
	        contentValues.put(BookSQLiteHelper.BOOK_ISBN ,book.getIsbn());
	        contentValues.put(BookSQLiteHelper.BOOK_PRICE, book.getPrice());
	
	        mShadowContentResolver.insert(URI_INFO , contentValues) ;

	        Uri uri = ContentUris.withAppendedId(URI_INFO , 8054335) ;
	        mShadowContentResolver.delete(uri , null , null) ;
	
	        Uri uri1 = ContentUris.withAppendedId(URI_INFO , 8054335) ;
	        Cursor cursor1 = mShadowContentResolver.query(uri1 , null , null , null , null);
	        assertEquals(cursor1.getCount() , 0);
	  	}
	}  


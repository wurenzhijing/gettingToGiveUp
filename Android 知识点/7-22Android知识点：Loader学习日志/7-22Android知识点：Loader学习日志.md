#Loader——装载器#

##Loader的简介##
装载器从Android3.0开始引进，它在activity或fragment中异步加载数据变得简单。装载器具有如下特性：

 - 对每个Activity和Fragment都有效。
 - 异步加载数据的能力，在单独的线程中读取数据。
 - 拥有一个数据改变通知机制，当数据源做出改变时会及时通知。
 
LoaderManager是加载器的管理，一个LoaderManager管理一个或多个Loader，LoaderManager管理Loader的初始化，重启和销毁操作。

LoaderManager.LoaderCallbacks是一个用于客户端与LoaderManager交互的回调接口。

AsyncTaskLoader提供一个AsyncTask来执行异步加载工作的抽象类。

CursorLoader是AsyncTaskLoader的子类，它查询ContentResolver然后返回一个Cursor。

##启动一个Loader##
通常要在activity的onCreate()方法中或fragment的onActivityCreated()方法中初始化一个装载器。
		
		loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,callbacks);

initLoader()方法有以下参数：

 - 一个唯一ID来标志装载器,在这个例子中，ID是0。如果ID存在，那么这个Loader会被重用。

 - 可选的参数,用于装载器初始化时。

 -  一个LoaderManager.LoaderCallbacks的实现．被LoaderManager调用以报告装载器的事件。
  
##重启一个Loader##

使用restartLoader()方法来对Loader进行重启，从而进行了一次新的查询，实现数据的更新。
	
	loaderManager.restartLoader(0,null,callbacks);

##LoaderManager的回调##

LoaderManager自动管理装载器的生命．LoaderManager会在需要时开始和停止装载动作，并且维护装载器的状态和它所关联的内容。使用LoaderManager.LoaderCallbacks的方法在某个事件发生时介入到数据加载的过程中。LoaderManager.LoaderCallBacks是一个回调接口，它使得客户端可以与LoaderManager交互。它包含以下方法：
 
 - onCreateLoader()根据传入的ID，初始化并返回一个新的装载器；
 - onLoadFinished()当一个装载器完成了它的装载过程后被调用；
 - onLoaderReset()当一个装载器被重置而其数据无效时被调用。

		
		private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
	        @Override
	        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
	            Uri uri = Uri.parse("content://com.example.administrator.loaderlearning.PeopleContentProvider/people");
	
	            CursorLoader cursorloader =  new CursorLoader(MainActivity.this,uri,null,null,null,null);
	            return cursorloader;
	        }
	
	        @Override
	        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                // simpleCursorAdapter 用于显示列表的数据 
	            simpleCursorAdapter.swapCursor(cursor);
	            listView.setAdapter(simpleCursorAdapter);

	            Log.i("____________","onLoadFinashed()---------"+loader.toString());
	        }
	
	        @Override
	        public void onLoaderReset(Loader<Cursor> loader) {
	            simpleCursorAdapter.swapCursor(null);
	            Log.i("_____-----","oneLoaderReset()");
	        }
	    };

##SimpleCursorAdapter##

SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to , int flags)是显示列表的适配器。

		String[] str= new String[]{"name","gender","age","height","weight","number"};
        int[] id= new int[]{ R.id.tv_name,R.id.tv_gender,R.id.tv_age,R.id.tv_height,R.id.tv_weight,R.id.tv_number};

        simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.list_item,null, str,id,0);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,callbacks);

        loaderManager.restartLoader(0,null,callbacks);
        Log.i(this.toString(),"--------"+loaderManager.toString());



#ContentProvider#
ContentProvider是Android四大组件之一，主要用于对外共享数据，也就是通过ContentProvider把应用中的数据共享给其他应用访问，其他应用可以通过ContentProvider对指定应用中的数据进行操作。
##ContentProvider##
Android提供了一些主要的ContentProvider，通过这些provider可以查看它们包含的数据。下面是ContentProvider的主要方法：
 
  - onCreate(),在创建ContentProvider时调用；
  - query(Uri, String[], String, String[], String)，用于查询指定Uri的ContentProvider，返回一个Cursor；
  - insert(Uri, ContentValues),用于添加数据到指定Uri的ContentProvider中；
  - update(Uri, ContentValues, String, String[]),用于更新指定Uri的ContentProvider中的数据;
  - delete(Uri, String, String[]) 用于从指定Uri的ContentProvider中删除数据;
  - getType(Uri) 用于返回指定的Uri中的数据的MIME类型。
	
    
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
        this.myDBHelper = new MyDBHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        db = myDBHelper.getWritableDatabase();
        Cursor cursor;
        Log.d("-------", String.valueOf(sMatcher.match(uri)));
        switch (sMatcher.match(uri)) {
            case BookU.ITEM:
                cursor = db.query(BookU.TABLE_NAME, strings, s, strings1, null, null, null);

                break;
            case BookU.ITEM_ID:
                String name = uri.getPathSegments().get(1);
                cursor = db.query(BookU.TABLE_NAME, strings, BookU.NAME+"="+name+(!TextUtils.isEmpty(s)?"AND("+s+')':""),strings1, null, null, s1);
                break;
            default:
                Log.d("!!!!!!", "Unknown URI"+uri);
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case BookU.ITEM:
                return BookU.CONTENT_TYPE;
            case BookU.ITEM_ID:
                return BookU.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = myDBHelper.getWritableDatabase();
        long rowId;
        if(sMatcher.match(uri)!=BookU.ITEM){
            throw new IllegalArgumentException("Unknown URI"+uri);
        }
        rowId = db.insert(BookU.TABLE_NAME,BookU.NAME,contentValues);
        if(rowId>0){
            Uri noteUri= ContentUris.withAppendedId(BookU.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new IllegalArgumentException("Unknown URI"+uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        db = myDBHelper.getWritableDatabase();
        int count = 0 ;
        switch (sMatcher.match(uri)) {
            case BookU.ITEM:
                count = db.delete(BookU.TABLE_NAME,s, strings);
                break;
            case BookU.ITEM_ID:
                String name = uri.getPathSegments().get(1);
                count = db.delete(BookU.NAME, BookU.NAME+"="+name+(!TextUtils.isEmpty(BookU.NAME="?")?"AND(" + s + ')':""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    
##ContentResolver##
当外部应用需要对ContentProvider中的数据进行添加、删除、修改和查询操作时，可以使用ContentResolver类来完成，要获取ContentResolver对象，可以使用Context提供的getContentResolver()方法。


  - insert(Uri uri, ContentValues values) 用于添加数据到指定Uri的ContentProvider中;
  - update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 用于更新指定Uri的ContentProvider中的数据;
  - query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 用于查询指定Uri的ContentProvider。
	
    
    public void onResume(){
        super.onResume();
        contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(BookU.CONTENT_URI,new String[]{BookU.NAME,BookU.AUTHOR,BookU.ISBN, BookU.PRICE},null,null,null);
        while(cursor.moveToNext()){
            Toast.makeText(this,
                    cursor.getString(cursor.getColumnIndex(BookU.NAME))+" "+
                            cursor.getString(cursor.getColumnIndex(BookU.AUTHOR))+" "
                            +cursor.getString(cursor.getColumnIndex(BookU.ISBN))+" "
                            +cursor.getString(cursor.getColumnIndex(BookU.PRICE)),Toast.LENGTH_SHORT).show();

        }
        cursor.close();
    }
    


##Uri##
Uri代表要操作的数据，可以用来标识每个ContentProvider。
	
    public View.OnClickListener myDeleteCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String bookname = bookList.get(2).getName();

            Log.i("------",bookname);

            ContentResolver contentResolver = getContentResolver();
            Uri uri = Uri.parse("content://com.example.administrator.contentprovice/book");
            String where = "name=?" ;
            String[] selectionArgs = { bookname };
            contentResolver.delete(uri,where,selectionArgs);

            Toast.makeText(MainActivity.this,"成功删除   "+bookname,Toast.LENGTH_LONG).show();

            listViewAdapter.notifyDataSetChanged();
        }
    };
    
#数据持久化——SQLite#


##SQLite简介##
SQLite是一款轻型的关系型数据库，功能强大的它有以下特点：
 
  - 轻量级，使用SQLite只需要带一个动态库就可以享受它的全部功能；
  - 独立性，SQLite数据库的核心引擎不需要第三方软件，更不需要安装；
  - 隔离性，SQLite中的所有信息都在一个文件中；
  - 跨平台，支持大部分操作系统；
  - 多语言接口，支持多语言编程接口；
  - 安全性，通过独占性和共享锁来实现独立事务处理。
##SQLite的数据类型##
  - Varchar(n)，长度不固定最大长度为n的字符串，n不超过4000；
  - Char(n)，长度固定为n的字符串，n不超过254；
  - Integer，整数类型；
  - Real，IEEE浮点数；
  - Text，按照字符串来存储；
  - Blob，二进制存储；
  - TIME，时间

##SQLiteOpenHelper创建SQLite##
创建SQLite需要使用SQLiteOpenHelper辅助类，其中有两个方法分别是：
 
  - onCreate()，在初次生成数据库时被调用，其中可以初始化表结构，及添加数据；
  - onUpgrade()，在数据库版本发生变化时调用，版本号由程序员控制。
  

		
		public class MySQLiteHelper extends SQLiteOpenHelper {

		    static final String DATABASE_NAME = "students.db" ;
		    static final String TABLE_NAME = "student" ;
		    static final  int VERSION = 1 ;
		
		    public MySQLiteHelper(Context context){
		        super(context,DATABASE_NAME,null,VERSION);
		    }
            
		    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		        super(context, DATABASE_NAME, factory, VERSION);
		    }

		    @Override
		    public void onCreate(SQLiteDatabase sqLiteDatabase) {
		        sqLiteDatabase.execSQL("create table student (" +
		                "_id integer primary key autoincrement," +
		                "name varchar(10)," +
		                "gender varchar(5)," +
		                "class varchar(5)," +
		                "score integer)");
		    }
		    @Override
		    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		
		        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS student");
		        onCreate(sqLiteDatabase);
		    }
		}

 - SQLiteOpeHelper类中的getWritableDatabase()和getReadableDatabase()可以返回SQLiteDatabase的对象。


		 mySQLiteHelper = new MySQLiteHelper(this)		
		 SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();

##SQLiteDatabase的操作##

SQLiteDatabase的操作很多，常见的有增、删、查、改，这些操作都可以用execSQL()方法来实现，其参数为sql语句，使用另一种方法具体如下：
 
 - insert()，插入数据，通过使用ContentValues来装载数据，再插入数据库

		        
		        // 使用ContentValues来装载数据 
				ContentValues cv = new ContentValues() ;
                cv.put("name",name);
                cv.put("gender",gender);
                cv.put("class",classes);
                cv.put("score",Integer.parseInt(score));
                
                db.insert(MySQLiteHelper.TABLE_NAME,null,cv); 

                Log.i(TAG , "--"+cv);
                cv.clear();
                db.close();

 - delete()，删除数据，其参数分别为表名称，删除的条件，删除的条件参数

		       db.delete(MySQLiteHelper.TABLE_NAME,"name = ?",new String[]{et.getText().toString()});
               db.close();
 
 - query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)，查询数据各参数分别是表名称，列名称数组，条件子句，条件语句的参数数组，分组，分组条件，排序类，分页查询的限制，查询结果使用结果集Cursor来装载的。
		
			   SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
               Cursor cursor  = db.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME + " where name = ?", new String[]{et.getText().toString()} );
               Log.i(TAG,"--"+cursor.toString());
               if (cursor.moveToFirst()){  //判断Cursor是否为空
                   Toast.makeText(MainActivity.this,
					cursor.getString(cursor.getColumnIndex("name")) +" "
					+cursor.getString(cursor.getColumnIndex("gender"))+" "
                    +cursor.getString(cursor.getColumnIndex("class"))+" "
					+String.valueOf(cursor.getString(cursor.getColumnIndex("score"))),Toast.LENGTH_SHORT).show();
                }
               cursor.close();
               db.close();

 - update()，修改数据同样也是通过ContentValues来实现
 		
		SQLiteDatabase db = mySQLiteHelper.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;
        cv.put(item[i], et_message.getText().toString());
                             
        String where = "name = ?";
        String[] whereArgs = new String[]{et_stu.getText().toString()};

        db.update(MySQLiteHelper.TABLE_NAME,cv,where,whereArgs);
                             
        cv.clear();
        db.close();


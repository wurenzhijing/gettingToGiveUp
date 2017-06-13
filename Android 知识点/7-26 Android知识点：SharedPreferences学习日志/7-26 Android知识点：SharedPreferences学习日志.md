#数据持久化——SharedPreference#
##简介##
这是一个轻量级的存储类，用来存储一些简单的配置信息，使用Map数据结构来存储，采用XML方式将数据存储到设备中，文件存放在/data/data/<package name>/shared_prefs 目录下。

![](http://img.my.csdn.net/uploads/201109/27/0_1317125587np98.gif)
##获取SharedPreference对象##

    SharedPreferences sharedPreferences = getSharedPreferences("name",0);
getSharedPreference(String name,int mode)方法，其中name为文件名，mode为操作模式，一般为0或MODE_PRIVATE。每个应用都有一个默认的preferences.xml，可以通过getDefaultPreferences(Context)来获取。
##存储数据##
 - 获取Sharepreferences对象
 - 获取SharedPreferences.Editor对象
 - 通过Editor的putXXXX()方法来存放键值对
 - 通过Editor的commit()方法来提交操作
	 
	    SharedPreferences sharedPreferences = getSharedPreferences("name",0);
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.putString(key,value) ;
        editor.commit();

##读取数据##

 - 获取SharedPreferences对象
 - 通过getXXXX()获取数据

		SharedPreferences sharedPreferences = getSharedPreferences("name",0);
        String name = sharedPreferences.getString(key,null);

##删除数据##

 - 获取Sharepreferences对象
 - 获取SharedPreferences.Editor对象
 - 通过Editor的remove(key)方法来存放键值对
 - 通过Editor的commit()方法来提交操作

		SharedPreferences sharedPreferences = getSharedPreferences("name",0);
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.remove(key) ;
        editor.commit();
package com.example.administrator.contentprovicer;

import android.net.Uri;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class BookU {
    static final String DATEBASE_NAME = "book.db";
    static final String TABLE_NAME = "book";
    static final int Version = 1;
    static  String NAME = "name";
    static final String AUTHOR = "author";
    static final String ISBN = "isbn";
    static final String  PRICE = "price";

    static final String AUTOHORITY = "com.example.administrator.contentprovice";
    static final int ITEM = 1;
    static final int ITEM_ID = 2;

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd..example.administrator.contentprovice";
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd..example.administrator.contentprovice";
    static final Uri CONTENT_URI = Uri.parse("content://"+AUTOHORITY+"/book");
}

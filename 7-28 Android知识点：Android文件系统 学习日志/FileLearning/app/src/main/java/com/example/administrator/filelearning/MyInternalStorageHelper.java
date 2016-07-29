/**
 * User: wurenzhijing
 * Date: 2016-07-28
 * Time: 09:40
 * FIXME
 */
package com.example.administrator.filelearning;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EventListener;


public class MyInternalStorageHelper {

    Context context ;

    public MyInternalStorageHelper(Context context){
        this.context = context ;
    }

    public static long getAvailableSize(){
        File file = Environment.getDataDirectory() ;
        StatFs statFs = new StatFs(file.getPath()) ;
        long blockSize = statFs.getBlockSize() ;
        long availiableSize = statFs.getAvailableBlocks() ;
        return availiableSize*blockSize ;
    }

    public  static long getTotalSize(){
        File path = Environment.getDataDirectory() ;
        StatFs statFs = new StatFs(path.getPath()) ;
        long blockSize = statFs.getBlockSize() ;
        long totalSize = statFs.getBlockCount() ;
        return totalSize*blockSize ;
    }

    public void save(String fileName , String content)throws IOException{
        File file = new File(context.getFilesDir(),fileName) ;
        FileOutputStream fos = new FileOutputStream(file) ;
        fos.write(content.getBytes());
        fos.close();
    }

    public String getContent(String fileName )throws IOException{
        FileInputStream fileInputStream = context.openFileInput(fileName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024] ;
        int len =-1 ;
        while((len = fileInputStream.read(data))!= -1){
            byteArrayOutputStream.write(data , 0 , len);
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public  void append(String fileName , String content)throws IOException{
        FileOutputStream fileOutputStream = context.openFileOutput(fileName , Context.MODE_APPEND) ;
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }

    public boolean delete(String fileName){

        return context.deleteFile(fileName) ;
    }

}  


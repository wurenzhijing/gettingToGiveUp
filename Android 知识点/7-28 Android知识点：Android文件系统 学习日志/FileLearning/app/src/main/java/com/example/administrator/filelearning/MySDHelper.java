/**
 * User: wurenzhijing
 * Date: 2016-07-28
 * Time: 10:19
 * FIXME
 */
package com.example.administrator.filelearning;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class MySDHelper {

    private static  final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static boolean isSDExist(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;
    }

    public static String getSDBasrDir(){
        if (isSDExist()){
            return Environment.getExternalStorageDirectory().getAbsolutePath() ;
        }
        return null ;
    }

    public static long getSDSize(){
        if (isSDExist()){
            StatFs statFs = new StatFs(getSDBasrDir()) ;
            long blockSize = statFs.getBlockSize() ;
            long totalSize = statFs.getBlockCountLong() ;
            return blockSize*totalSize/1024/1024 ;

        }
        return 0 ;
    }

    public static long getSDAvailiableSize(){
        if (isSDExist()){
            StatFs statFs = new StatFs(getSDBasrDir()) ;
            long blockSize = statFs.getBlockSize() ;
            long availableSize = statFs.getAvailableBlocksLong();
            return  blockSize*availableSize/1024/1024 ;
        }
        return 0 ;
    }

    public boolean saveToDir(String fileName , String content)throws IOException{
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +"/me/"+fileName  ;

        File file = new File(dir) ;
        file.mkdirs();
        if (isSDExist()){

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] bytes = content.getBytes() ;
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            return true;
        }
        return false ;
    }

    public boolean savtToPrivateSD(String fileName , String content ,Context context)throws IOException{
        if (isSDExist()){
            File file = context.getExternalCacheDir();
            byte[] bytes = content.getBytes() ;
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
            bos.write(bytes);
            bos.flush();

            return true;
        }
        return false ;
    }

    public String getContent(String fileName,Context context) throws IOException{
        String dir = DIR +"/android/data/com.example.administrator.filelearning/cache/"+fileName  ;
        File file = new File(dir) ;

        if (file.exists()) {

              FileInputStream fileInputStream = new FileInputStream(file);
             // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
//
//            int len = 0;
//            byte[] content = new byte[len];
//            while ((len = bufferedInputStream.read(content)) != -1) {
//                byteArrayOutputStream.write(content, 0, len);
//                byteArrayOutputStream.flush();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024] ;
            int len =-1 ;
            while((len = fileInputStream.read(data))!= -1){
                byteArrayOutputStream.write(data , 0 , len);
            }
            return new String(byteArrayOutputStream.toByteArray());

        }
        return null ;
    }

    public void append(String fileName , String content) throws IOException{
        if (isSDExist()){
            FileOutputStream fileOutputStream = new FileOutputStream(fileName) ;
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

    }

    public boolean deleteSDFile(String fileName)throws IOException{
        File file = new File(fileName) ;
        if (file.exists()){
           return file.delete() ;
        }
        return false ;
    }

}  


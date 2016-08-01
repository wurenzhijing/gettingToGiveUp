/**
 * User: wurenzhijing
 * Date: 2016-08-01
 * Time: 10:33
 * FIXME
 */
package com.example.administrator.datacachelearning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class LoaderImageManager {

    Map<String ,SoftReference<Bitmap>> imageCache ; // 一级缓存
    boolean cacheToFile = false ;    //  是否缓存到本地文件
    String cacheDir ;   // 缓存目录

    String fileName ;


    public LoaderImageManager(Map<String ,SoftReference<Bitmap>> imageCache){
        this.imageCache = imageCache ;
    }

    // 设置是否缓存到外部文件
    public void setCahcheToFile(Boolean flag){
        this.cacheToFile = flag ;
    }

    // 设置缓存到外部文件的路径
    public void setCacheDir(String cacheDir){
        this.cacheDir = cacheDir ;
    }

    // 下载图片
    public Bitmap getBitmapFromHttp( String imageUrl , boolean cacheToMemory){
        Bitmap bitmap = null ;
        Log.i("________","开始下载");
        try{
            URL url = new URL(imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream() ;
            bitmap = BitmapFactory.decodeStream(inputStream) ;
            Log.i("________","完成下载");
            if (cacheToMemory){
                //  缓存到 内存中
                imageCache.put(imageUrl , new SoftReference<Bitmap>(bitmap));
                Log.i("________","缓存到内存"+imageCache.get(imageUrl));
                if (cacheToFile){
                    //  缓存到  cache文件夹中
                    String fileName = getMD5(imageUrl) ;
                    String filePath = cacheDir + "/" + fileName ;
                    File file = new File(cacheDir,fileName);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    Log.i("________","缓存到外部文件  "+filePath.toString());
                   // FileOutputStream fileOutputStream = new FileOutputStream(filePath) ;
                    bitmap.compress(Bitmap.CompressFormat.PNG , 100 , bufferedOutputStream) ;
                }
            }
            inputStream.close();;
            httpURLConnection.disconnect();
            return bitmap ;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return  null ;
        }catch (IOException e){
            e.printStackTrace();
            return null ;
        }
    }

    // 从内存缓存中获取 图片
    public Bitmap getBitmapFromMemory( String imageUrl){
       // Log.i("缓存方式","内存缓存 or 外部文件缓存");
        Bitmap bitmap = null ;
        if (imageCache.containsKey(imageUrl)){
           // Log.i("缓存方式","内存缓存 ");
            synchronized (imageCache){
                SoftReference<Bitmap> bitmapSoftReference = imageCache.get(imageUrl) ;
                if (bitmapSoftReference != null ){
                    bitmap = bitmapSoftReference.get() ;
                    return bitmap ;
                }
            }
        }
        if (cacheToFile){
            //Log.i("缓存方式"," 外部文件缓存");
            bitmap = getBitmapFromFile( imageUrl);
            if (bitmap != null){
                imageCache.put(imageUrl , new SoftReference<Bitmap>(bitmap));
            }
        }
        return  bitmap ;
    }

    // 从文件缓存中获取bitmap
    public Bitmap getBitmapFromFile(String imageUrl){
        Bitmap bitmap = null ;

        fileName = getMD5(imageUrl) ;
        //String filePath =cacheDir + "/" + fileName ;
        File f = new File(cacheDir);
        if (!f.exists()){
            f.mkdirs();
        }
        File file = new File(f.getAbsolutePath(),fileName) ;
        Log.i("    ____获取__________ ",file.getAbsolutePath().toString()+"    "+f.exists()+file.exists());

        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
           // FileInputStream fileInputStream =  new FileInputStream(filePath) ;
            bitmap = BitmapFactory.decodeStream(bufferedInputStream) ;
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return  null;
        }
        return bitmap ;
    }


    private static String getMD5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

}  


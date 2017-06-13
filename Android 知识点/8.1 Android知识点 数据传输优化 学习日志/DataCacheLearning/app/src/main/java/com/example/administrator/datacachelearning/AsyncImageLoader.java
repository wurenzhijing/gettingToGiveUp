/**
 * User: wurenzhijing
 * Date: 2016-08-01
 * Time: 10:24
 * FIXME
 */
package com.example.administrator.datacachelearning;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncImageLoader {

    static Map<String , SoftReference<Bitmap>> mImageCache ;  // 内存缓存
    static LoaderImageManager loaderImageManager ;          // 缓存管理器
    static ExecutorService executorService ;

    Handler handler ;  //   通知UI线程获取图片

    public interface ImageCallback{
        public void onImageLoaded(Bitmap bitmap , String imageUrl);
    }

    static {
        mImageCache = new HashMap<String ,  SoftReference<Bitmap>>();
        loaderImageManager = new LoaderImageManager(mImageCache);
    }

    public AsyncImageLoader(Context context){
        handler = new Handler() ;
        startThreadPoolNecessary();

        String defaultDir = context.getExternalCacheDir().getPath() ;
        //String defaultDir ="/data/data/com.example.administrator.datacachelearning/cache";
        setCacheDir(defaultDir) ;
    }

    //   设置是否缓存到 文件
    public void setCacheToFile(boolean flag){
        loaderImageManager.setCahcheToFile(flag);
    }

    // 设置缓存到外部文件的路径
    public void setCacheDir(String  dir){
        loaderImageManager.setCacheDir(dir);
    }

    // 开启线程池
    public static void startThreadPoolNecessary(){
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()){
            executorService = Executors.newSingleThreadExecutor() ;

        }
    }

    //异步下载图片 缓存到Memory
    public void downloadImage(  final String url , final ImageCallback callback){
        downloadImage( url , true , callback);
    }

    public void downloadImage(  final String url , final boolean cacheToMemory , final ImageCallback callback ){
        Bitmap bitmap =loaderImageManager.getBitmapFromMemory( url) ;
        Log.i("图片来源"," 缓存 or 下载");
        if (bitmap!= null){
            if (callback != null ){
                Log.i("图片来源","缓存");
                callback.onImageLoaded(bitmap , url );
            }
        }else {
            // 从网络下载
            executorService.submit(new Runnable(){
                @Override
                public void run() {
                    Log.i("图片来源","下载");
                    final Bitmap bitmap = loaderImageManager.getBitmapFromHttp( url , cacheToMemory);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null){
                                callback.onImageLoaded(bitmap , url);
                            }
                        }
                    });
                }
            });
        }
    }

    public boolean deleteCache(Context context){
        File file = new File(context.getExternalCacheDir().getPath()+"/"+loaderImageManager.fileName);
        Log.i("file",file.exists()+"    "+file.getAbsolutePath());
        if (!file.exists()){
            return false;
        }
        file.delete();

        loaderImageManager.imageCache.clear();

        return true;
    }


}


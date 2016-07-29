package com.example.administrator.networkconmunicationlearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageActivity extends AppCompatActivity {

    ImageView iv_dowmload ;
    Button btn_download , btn_save;
    String imageName ;
    String saveMessage ;
    Bitmap mBitmap ;
    ProgressDialog saveProgressDialog ;
    ProgressDialog dowmProgressDialog ;

    static final String TAG = "ImageActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

///storage/emulated/0/download_image/picture.jpg

        iv_dowmload = (ImageView)findViewById(R.id.iv_download) ;

        btn_download = (Button)findViewById(R.id.btn_download);
        btn_download.setOnClickListener(downloadOnClickListener);

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(saveOnClickListener);
    }

    public View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveProgressDialog = ProgressDialog.show(ImageActivity.this,"保存图片","正在保存图片,请稍后",true);

            new Thread(saveImageRunnable).start();
        }
    } ;

    public View.OnClickListener downloadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dowmProgressDialog = ProgressDialog.show(ImageActivity.this,"下载图片","正在下载图片,请稍后",true);
            new Thread(connectNet).start();
        }
    } ;

    private Runnable connectNet  = new Runnable() {
        @Override
        public void run() {
            try{
                String imageUrl = "http://d05.res.meilishuo.net/pic/l/f0/da/60f12fb56faf727c9530984b6853_500_331.jpg";
                imageName = "picture.jpg";
                mBitmap = BitmapFactory.decodeStream(downloadImage(imageUrl)) ;

                connectHandle.sendEmptyMessage(0);
                Log.i(TAG ,"设置图片");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private Handler connectHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            dowmProgressDialog.dismiss();
            Log.i(TAG , "显示图片");
            if (mBitmap != null){
                iv_dowmload.setImageBitmap(mBitmap);
            }
        }
    };

    private Runnable saveImageRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                saveImage(mBitmap , imageName);
                saveMessage =  "保存成功";
            }catch (Exception e){
                e.printStackTrace();
                saveMessage = "保存失败";
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }
    };

    private Handler messageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            saveProgressDialog.dismiss();
            Log.i(TAG , saveMessage);
            Toast.makeText(ImageActivity.this,saveMessage,Toast.LENGTH_SHORT).show();
        }
    };

    public void saveImage(Bitmap bitmap ,String imageName)throws IOException{;
        String dir = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();

        File mFile = new File(dir,imageName);
        Log.i(TAG,mFile.getAbsolutePath());

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(mFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,bufferedOutputStream) ;
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
    public InputStream downloadImage(String url)throws Exception{
        URL urlPath = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)urlPath.openConnection();
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            return connection.getInputStream();
        }
        return null ;
    }


}

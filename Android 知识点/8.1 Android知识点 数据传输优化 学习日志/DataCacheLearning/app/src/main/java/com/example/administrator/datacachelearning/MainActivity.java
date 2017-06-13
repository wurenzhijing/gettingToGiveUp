package com.example.administrator.datacachelearning;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView iv_image ;
    Button bt_deleteCache ,bt_loadImageFromFile , btn_startpolling , btn_stopPolling;

    String imageUrl = "http://img6.ph.126.net/cBR-rjhWWtkRDkCkIa81gQ==/1351361363205139216.jpg" ;

    AsyncImageLoader loader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loader = new AsyncImageLoader(getApplicationContext());
        loader.setCacheToFile(true) ;   //将图片缓存到外部文件

        iv_image = (ImageView)findViewById(R.id.iv_image) ;

        bt_deleteCache  = (Button)findViewById(R.id.bt_deleteCache) ;
        bt_deleteCache.setOnClickListener(deleteCacheOnClickListener);

        bt_loadImageFromFile =(Button)findViewById(R.id.bt_loadImageFromFile);
        bt_loadImageFromFile.setOnClickListener(loadOnClickListener);

        btn_startpolling = (Button)findViewById(R.id.btn_startpolling) ;
        btn_startpolling.setOnClickListener(startpollingOnClickListener);

        btn_stopPolling = (Button)findViewById(R.id.btn_stoppolling) ;
        btn_stopPolling.setOnClickListener(stoppollingOnClickListener);
    }

    public View.OnClickListener deleteCacheOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Boolean b = loader.deleteCache(getApplicationContext());
            Log.i("MainActivity  "  , "删除缓存成功"+b);
            Toast.makeText(MainActivity.this,"删除缓存成功",Toast.LENGTH_SHORT).show();
        }
    };

    public View.OnClickListener loadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            loader.downloadImage( imageUrl, true, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                    if (bitmap != null){
                        iv_image.setImageBitmap(bitmap);
                        Log.i("MainActivity  "  , "显示图片成功");

                    }else {
                        Toast.makeText(MainActivity.this,"图片下载失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Toast.makeText(MainActivity.this,"加载图片成功",Toast.LENGTH_SHORT).show();
        }
    };

    public View.OnClickListener startpollingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PollingUtils.startPolling(MainActivity.this, 2 , MyService.class , MyService.ACTION);
            Toast.makeText(MainActivity.this,"开启轮询",Toast.LENGTH_SHORT).show();
        }
    };

    public View.OnClickListener stoppollingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PollingUtils.stopPolling(MainActivity.this,MyService.class , MyService.ACTION);
            Toast.makeText(MainActivity.this,"关闭轮询",Toast.LENGTH_SHORT).show();
        }
    };
}

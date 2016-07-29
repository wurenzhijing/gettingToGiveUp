package com.example.administrator.networkconmunicationlearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {

    Button btn_post ,btn_get ,btn_downloadImage ,btn_socket;
    WebView wv_html;
    String webdata = null ;

    Boolean netState ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        netState = isNetWorkAvailable(this);
        if (netState == false){
            Toast.makeText(MainActivity.this,"network unavailable！",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,"network available！",Toast.LENGTH_SHORT).show();
        }

        wv_html = (WebView)findViewById(R.id.wv_html) ;


        btn_post = (Button)findViewById(R.id.btn_post) ;
        btn_post.setOnClickListener(postOnClickListener);

        btn_get = (Button)findViewById(R.id.btn_get) ;
        btn_get.setOnClickListener(getOnClickListener);

        btn_downloadImage = (Button)findViewById(R.id.btn_downloadImage) ;
        btn_downloadImage.setOnClickListener(downloadOnClickListener);

        btn_socket = (Button)findViewById(R.id.btn_socket);
        btn_socket.setOnClickListener(socketOnClickListener);

    }

    public View.OnClickListener socketOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this , SocketActivity.class));
        }
    };

    public View.OnClickListener downloadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

           startActivity(new Intent(MainActivity.this,ImageActivity.class));

        }
    };

    public View.OnClickListener postOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new Thread(httpPostThread).start();

            wv_html.loadData(webdata , "text/html; charset=UTF-8", null);
        }
    };

    Handler posthandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            webdata  = msg.obj.toString() ;
        }
    };
    Runnable httpPostThread = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();

            String str = httpPost("http://fanyi.baidu.com/?aldtype=85#zh/en/");


            message.obj = str ;
            posthandler.sendMessage(message);
        }
    };

    public View.OnClickListener getOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new Thread(httpGetThread).start();

            wv_html.loadData(webdata , "text/html; charset=UTF-8", null);


        }
    };

    Handler gethandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            webdata  = msg.obj.toString() ;
        }
    };
    Runnable httpGetThread = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();


            String str = httpGet("http://www.baidu.com/");
            message.obj = str ;
            gethandler.sendMessage(message);
        }
    };


    public String httpPost(String url){
        String params ;
        String str = null ;
        try{
            params = "username="+ URLEncoder.encode("hello","UTF-8")+"&password="+URLEncoder.encode("eoe","UTF-8");
            byte[] postData = params.getBytes() ;
            URL urlPath = new URL(url) ;
            HttpURLConnection urlConnection = (HttpURLConnection)urlPath.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestProperty("Content-Type" , "application/x-www-form-urlencode");
            urlConnection.connect();

            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(postData);
            dataOutputStream.flush();
            dataOutputStream.close();
            Log.i("-------POST-------",urlConnection.getResponseCode()+"");
            if (urlConnection.getResponseCode() == 200){
                byte[] data = readInputStream(urlConnection.getInputStream());
                  str = new String(data ,"UTF-8");
                Log.i("-----POST------",str);
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return str ;
    }

    public byte[] readInputStream(InputStream inputStream)throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len =0 ;
        while ((len = inputStream.read(buffer))!=-1){
            outputStream.write(buffer , 0 , len);
        }
        byte[] data = outputStream.toByteArray() ;
        outputStream.close();
        inputStream.close();
        return data ;
    }

    public  String  httpGet(String url){
        URL urlPath ;
        String resultData = null ;
        try{
            urlPath = new URL(url) ;
            HttpURLConnection urlConnection = (HttpURLConnection)urlPath.openConnection() ;
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String inputLine = null ;

            while((inputLine = bufferedReader.readLine()) != null){
                resultData+= inputLine ;
            }
            Log.i("_____GET_____",resultData);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return resultData ;
    }

    public static boolean isNetWorkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            Log.i("Network","Unavailable");
            return false ;
        }else{
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (infos!=null){
                for (int i= 0;i<infos.length ; i++){
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED){
                        Log.i("Network"," available");
                        return true ;
                    }
                }
            }
        }
        return false ;
    }
}

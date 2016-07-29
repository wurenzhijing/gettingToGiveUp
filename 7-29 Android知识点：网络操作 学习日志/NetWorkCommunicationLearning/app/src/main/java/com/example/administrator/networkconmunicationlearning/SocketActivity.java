package com.example.administrator.networkconmunicationlearning;

import android.app.LoaderManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketActivity extends AppCompatActivity {

    EditText et_message ;
    Button bt_send ;
    TextView tv_receive ;

    static final String TAG  = "SocketActivity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        et_message = (EditText)findViewById(R.id.et_message) ;
        tv_receive = (TextView)findViewById(R.id.tv_receive) ;
        bt_send = (Button)findViewById(R.id.bt_send);
        bt_send.setOnClickListener(sendOnClickListener);
    }

    public View.OnClickListener sendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(connectRunnable).start();
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.obj.toString() ;
            tv_receive.setText(str);
        }
    };

    Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message() ;
            String str = connect();
            message.obj = str ;
            handler.sendMessage(message) ;
        }
    } ;

    public String  connect(){
        Socket socket = null ;
        String backMessage = null ;
        try{
            InetAddress inetAddress = InetAddress.getByName("192.168.252.146");
            Log.i("TAG","TCP   connecting  "+inetAddress.toString());
            socket = new Socket(inetAddress ,8080);
            Log.i(TAG ,"TCP   sending");

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            String message = et_message.getText().toString() ;
            out.println(message);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            backMessage = in.readLine();
            Log.i(TAG  , "Server 返回"+backMessage);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.i(TAG ,"192.168.252.146 is unkown");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
        return backMessage ;
    }
}

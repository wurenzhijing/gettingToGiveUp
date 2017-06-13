package com.example.administrator.filelearning;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {

    EditText et_content ,et_fileName ,et_getfileName;
    Button btn_saveToSD ,btn_saveToData ,btn_getFromSD ,btn_getFromData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_fileName = (EditText)findViewById(R.id.et_fileName) ;
        et_content = (EditText)findViewById(R.id.et_content) ;
        et_getfileName = (EditText)findViewById(R.id.et_getfileName) ;

        btn_saveToSD = (Button)findViewById(R.id.btn_saveSD) ;
        btn_saveToSD.setOnClickListener(saveToSDOnClickListener);

        btn_getFromSD = (Button)findViewById(R.id.btn_getFromSD) ;
        btn_getFromSD.setOnClickListener(getFromSDOnClickListener);

        btn_saveToData = (Button)findViewById(R.id.btn_saveData) ;
        btn_saveToData.setOnClickListener(saveToDataOnClickListener);

        btn_getFromData = (Button)findViewById(R.id.btn_getFromData) ;
        btn_getFromData.setOnClickListener(getFromDataOnClickListener);
    }

    public View.OnClickListener saveToSDOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String fileName = et_fileName.getText().toString() ;
            String content = et_content.getText().toString() ;
            MySDHelper mySDHelper = new MySDHelper() ;
            if (content.equals("") || fileName.equals("")){
                Toast.makeText(MainActivity.this,"请输入要保存的信息",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    Log.i("-___________",mySDHelper.isSDExist()+"      "+ Environment.getExternalStoragePublicDirectory("agfa").canWrite());
                    Log.i("-------afasdfasfasf--",Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_DOWNLOADS+"/me/");
                    boolean b = mySDHelper.saveToDir(fileName , content);
                    boolean a = mySDHelper.savtToPrivateSD(fileName , content , MainActivity.this);
                    Toast.makeText(MainActivity.this,"文件"+fileName+"保存成功"+ a,Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public View.OnClickListener getFromSDOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MySDHelper mySDHelper = new MySDHelper() ;
            String fileName = et_getfileName.getText().toString() ;

            if ( fileName.equals("")){
                Toast.makeText(MainActivity.this,"请输入要保存的信息",Toast.LENGTH_SHORT).show();
            }else {
                try {

                    Log.i("_____________",MainActivity.this.getCacheDir().toString()+"/"+fileName+".txt");

                    String content = mySDHelper.getContent(fileName,MainActivity.this );
                    Toast.makeText(MainActivity.this, "文件" + content , Toast.LENGTH_SHORT).show();
                    Log.i(" ffffffffffff",content) ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } ;

    public View.OnClickListener saveToDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String fileName = et_fileName.getText().toString() ;
            String content = et_content.getText().toString() ;
            MyInternalStorageHelper myInternalStorageHelper = new MyInternalStorageHelper(MainActivity.this) ;
            if (content.equals("") || fileName.equals("")){
                Toast.makeText(MainActivity.this,"请输入要保存的信息",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    myInternalStorageHelper.save(fileName , content);
                    Toast.makeText(MainActivity.this,"文件"+fileName+"保存成功",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public View.OnClickListener getFromDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String fileName = et_getfileName.getText().toString() ;
            MyInternalStorageHelper myInternalStorageHelper = new MyInternalStorageHelper(MainActivity.this) ;
            if ( fileName.equals("")){
                Toast.makeText(MainActivity.this,"请输入要保存的信息",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    String content = myInternalStorageHelper.getContent(fileName);
                    Toast.makeText(MainActivity.this, "文件" + content , Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}

package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewLogs extends AppCompatActivity {

    private EditText Content_1;
    private TextView DATE;
    public static final String LOGS_NAME="logs_name";
    private String logsName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);

        Intent intent=getIntent();
        logsName=intent.getStringExtra(LOGS_NAME);
        Content_1 = (EditText) findViewById(R.id.Content_1);
        DATE=(TextView)findViewById(R.id.date);
        DATE.setText(logsName);
        String inputText=load();
        if(!TextUtils.isEmpty(inputText)){
            Content_1.setText(inputText);
            Content_1.setSelection(inputText.length());
        }
    }

    //重写onDestroy方法，在退出编辑返回首页的时候获得输入内容
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText=Content_1.getText().toString();
        save(inputText);
    }

    public void save(String inputText) {
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{//文本写入文件
            out=openFileOutput(logsName, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }catch (IOException e) {//异常处理
            e.printStackTrace();
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{
            in=openFileInput(logsName);
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            //逐行读取写入content
            while((line=reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e) {//异常处理
            e.printStackTrace();
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }
}
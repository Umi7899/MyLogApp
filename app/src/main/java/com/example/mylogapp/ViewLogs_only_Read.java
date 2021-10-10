package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ViewLogs_only_Read extends AppCompatActivity {

    private TextView Content_2,DATE;
    public static final String LOGS_NAME="logs_name";
    private String logsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs_only_read);
        Intent intent=getIntent();
        logsName=intent.getStringExtra(LOGS_NAME);
        Content_2 = (TextView) findViewById(R.id.Content_2);
        DATE=(TextView)findViewById(R.id.date);
        DATE.setText(logsName);
        String inputText=load();
        if(!TextUtils.isEmpty(inputText)){
            Content_2.setText(inputText);
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
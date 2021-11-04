package com.example.mylogapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Memento extends AppCompatActivity {
    private Button button;
    private TimePicker timePicker;
    private EditText Content;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    String date_string = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memento);
        button=findViewById(R.id.button1);
        timePicker=findViewById(R.id.pick);
        //设置24小时制
        timePicker.setIs24HourView(true);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //闹钟启动时才会跳转
                Intent intent=new Intent(Memento.this,Notice.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(Memento.this,1,intent,0);
                Calendar calendar= Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);
                AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                Toast.makeText(Memento.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
            }
        });
        Content = (EditText) findViewById(R.id.Content);
        String inputText=load();
        if(!TextUtils.isEmpty(inputText)){
            Content.setText(inputText);
            Content.setSelection(inputText.length());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText=Content.getText().toString();
        save(inputText);
    }

    public void save(String inputText) {
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{//文本写入文件
            out=openFileOutput(date_string+"1", Context.MODE_PRIVATE);
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
            in=openFileInput(date_string+"1");
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
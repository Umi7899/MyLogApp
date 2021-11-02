package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
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
    String savetext;
    Myconnection conn;//
    Intent intentms;//
    int musicnote=0;//
    private Musicservice.MyMusicBinder control;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs_only_read);
        intentms = new Intent(ViewLogs_only_Read.this, Musicservice.class);//
        Intent intent=getIntent();
        logsName=intent.getStringExtra(LOGS_NAME);
        Content_2 = (TextView) findViewById(R.id.Content_2);
        DATE=(TextView)findViewById(R.id.date);
        DATE.setText(logsName);
        TextView textView=findViewById(R.id.count_words_1);

        String inputText=load();
        savetext=load();
        if(load()=="") {
            inputText="0";
            savetext = "0";
        }
        String num= String.valueOf(inputText.trim().length()-1);
        //textView.setText(num+"个字");
        if(!TextUtils.isEmpty(inputText.substring(1))){
            Content_2.setText(inputText.substring(1));
        }
        if(musicnote!=0) {//
            intentms.putExtra("music",musicnote);//
            ViewLogs_only_Read.this.startService(intentms);//
            conn = new Myconnection();//
            bindService(intentms, conn, BIND_AUTO_CREATE);//
        }//
        addpictures();
    }


    protected void onDestroy() {
        super.onDestroy();
        intentms.putExtra("music",0);
        ViewLogs_only_Read.this.startService(intentms);//
        conn = new Myconnection();//
        bindService(intentms, conn, BIND_AUTO_CREATE);//
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
            musicnote=Integer.valueOf(content.toString().substring(0,1));
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
    public void addpictures()
    {
        System.out.println(savetext);
        SpannableString addemoji = new SpannableString(savetext);
        Drawable e=null;
        //e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
        for(int i=0;i<savetext.length();i++) {
            if(savetext.charAt(i)=='[') {
                ImageSpan emoji;
                switch(savetext.charAt(i+6)) {
                    case '0':
                        e=getResources().getDrawable(R.drawable.laugh);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '1':
                        e=getResources().getDrawable(R.drawable.cry);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '2':
                        e=getResources().getDrawable(R.drawable.arrogance);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '3':
                        e=getResources().getDrawable(R.drawable.dementia);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '4':
                        e=getResources().getDrawable(R.drawable.unhappy);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '5':
                        e=getResources().getDrawable(R.drawable.sweat);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '6':
                        e=getResources().getDrawable(R.drawable.think);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '7':
                        e=getResources().getDrawable(R.drawable.good);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                }
            }
        }
        Content_2.setText(addemoji.subSequence(1,addemoji.length()));
        //Content.setText("jshjkshu");
    }
    private class Myconnection implements ServiceConnection {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {

        }
        public void onServiceDisconnected(ComponentName componentName)
        {

        }
    }
}
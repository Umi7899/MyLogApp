package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogToday extends AppCompatActivity {

    private EditText Content;
    String NameID=null;
    static int sign=0;
    static String savetext;
    static int index;
    String loadjudge="N";
    Myconnection conn;//
    Intent intentms;//
    static int musicnote=0;//
    private Musicservice.MyMusicBinder control;//
    static Editable edit;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    String date_string = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_today);

        intentms = new Intent(LogToday.this, Musicservice.class);//
        musicnote=this.getIntent().getIntExtra("musicnote",0);
        loadjudge=this.getIntent().getStringExtra("loadjudge");
        System.out.println(musicnote+"A");

        Button music=(Button) findViewById(R.id.music);
        Button emoji=(Button) findViewById(R.id.emoji);
        Button backmain=(Button)findViewById(R.id.backmain);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign=2;
                savetext=String.valueOf(musicnote)+Content.getText().toString();
                Intent intentmusic=new Intent(LogToday.this,MusicList.class);
                startActivity(intentmusic);
            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign=1;
                index=Content.getSelectionStart()+1;
                savetext=String.valueOf(musicnote)+Content.getText().toString();
               // System.out.println(savetext);
                Intent intentemoji=new Intent(LogToday.this,EmojiList.class);
                intentemoji.putExtra("musicnote",musicnote);
                startActivity(intentemoji);
            }
        });
        backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmain=new Intent(LogToday.this,MainActivity.class);
                startActivity(intentmain);
                System.out.println(musicnote);
                finish();
            }
        });
        Content = (EditText) findViewById(R.id.Content);
        String inputText=load();
        if(sign==0)
        savetext=load();
        if(load()=="")
        {
            inputText="0";
            if(sign==0)
                savetext="0";
        }
        System.out.println(musicnote+"B");
        //System.out.println(sign);
        if(!TextUtils.isEmpty(inputText)){
            Content.setText(inputText.substring(1));
            //Content.setSelection(inputText.length());
        }
        //if(sign==0)
        if(musicnote!=0) {//
            intentms.putExtra("music",musicnote);//
            LogToday.this.startService(intentms);//
            conn = new Myconnection();//
            bindService(intentms, conn, BIND_AUTO_CREATE);//
        }//
    }
    protected void onStart()
    {
        super.onStart();
        if(sign==1) {
            NameID = this.getIntent().getStringExtra("emojiid");
            if(NameID!=null)
            addpictures(NameID);
            else
                Content.setText(savetext);
            sign=0;
        }else
            addpictures();
    }
    //重写onDestroy方法，在退出编辑返回首页的时候获得输入内容
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText=Content.getText().toString();
        System.out.println("save");
        save(inputText);
        intentms.putExtra("music",0);
        LogToday.this.startService(intentms);//
        conn = new Myconnection();//
        bindService(intentms, conn, BIND_AUTO_CREATE);//
        System.out.println(musicnote);
    }

    public void save(String inputText) {
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{//文本写入文件
            out=openFileOutput(date_string, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(String.valueOf(musicnote)+inputText);
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
            in=openFileInput(date_string);
            reader=new BufferedReader(new InputStreamReader(in));
            /*if(loadjudge=="Y") {
                System.out.println(loadjudge);
                System.out.println("loadsuccess");
                musicnote = Integer.valueOf(date_string.substring(11));
            }*/
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
    public void addpictures(String id)
    {
        StringBuffer s=new StringBuffer(savetext);
        if (index < 0 || index >= savetext.length() ){
            s.append(id);
        }else{
            s.insert(index,id);//光标所在位置插入文字
        }
        //System.out.println(s);
        SpannableString addemoji = new SpannableString(s);
        Drawable e=null;
        //e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i)=='[') {
                ImageSpan emoji;
                switch(s.charAt(i+6)) {
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
                        e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                }
            }
        }
        Content.setText(addemoji.subSequence(1,addemoji.length()));
        //Content.setText("jshjkshu");
    }
    public void addpictures() {
        System.out.println(savetext);
        SpannableString addemoji = new SpannableString(savetext);
        Drawable e = null;
        //e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
            for (int i = 0; i < savetext.length(); i++) {
                if (savetext.charAt(i) == '[') {
                    ImageSpan emoji;
                    switch (savetext.charAt(i + 6)) {
                        case '0':
                            e = getResources().getDrawable(R.drawable.laugh);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '1':
                            e = getResources().getDrawable(R.drawable.cry);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '2':
                            e = getResources().getDrawable(R.drawable.arrogance);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '3':
                            e = getResources().getDrawable(R.drawable.dementia);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '4':
                            e = getResources().getDrawable(R.drawable.unhappy);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '5':
                            e = getResources().getDrawable(R.drawable.sweat);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '6':
                            e = getResources().getDrawable(R.drawable.think);
                            e.setBounds(0, 0, 40, 40);
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                        case '7':
                            e = getResources().getDrawable(R.drawable.good);
                            e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
                            emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                            addemoji.setSpan(emoji, i, i + 8, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            break;
                    }
                }
            }
        Content.setText(addemoji.subSequence(1, addemoji.length()));
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
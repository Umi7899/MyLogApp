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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    String NameID=null;
    static int sign=0;
    static String savetext="0";
    static int index;
    private TextView DATE;
    public static final String LOGS_NAME="logs_name";
    private String logsName;
    Myconnection conn;//
    Intent intentms;//
    int musicnote=0;//
    private Musicservice.MyMusicBinder control;//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);
        intentms = new Intent(ViewLogs.this, Musicservice.class);//
        Intent intent=getIntent();
        Button music1=(Button) findViewById(R.id.music1);
        Button emoji1=(Button) findViewById(R.id.emoji1);
        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign=2;
                savetext=String.valueOf(musicnote)+Content_1.getText().toString();
                Intent intentmusic=new Intent(ViewLogs.this,MusicList.class);
                startActivityForResult(intentmusic,1);
            }
        });
        emoji1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign=1;
                index=Content_1.getSelectionStart()+1;
                savetext=String.valueOf(musicnote)+Content_1.getText().toString();
                // System.out.println(savetext);
                Intent intentemoji=new Intent(ViewLogs.this,EmojiList.class);
                //intentemoji.putExtra("musicnote",musicnote);
                startActivityForResult(intentemoji,2);
            }
        });
        logsName=intent.getStringExtra(LOGS_NAME);
        Content_1 = (EditText) findViewById(R.id.Content_1);
        DATE=(TextView)findViewById(R.id.date);
        DATE.setText(logsName.substring(0,11));
        String inputText=load();
        savetext=load();
        if(load()=="") {
            inputText="0";
            savetext = "0";
        }
        if(!TextUtils.isEmpty(inputText.substring(1))){
            Content_1.setText(inputText.substring(1));
            Content_1.setSelection(inputText.substring(1).length());
        }
        if(musicnote!=0) {//
            intentms.putExtra("music",musicnote);//
            ViewLogs.this.startService(intentms);//
            conn = new Myconnection();//
            bindService(intentms, conn, BIND_AUTO_CREATE);//
        }//
        //addpictures();
    }
    protected void onStart()
    {
        super.onStart();
        System.out.println(NameID);
        //if(sign==1) {
            //NameID = this.getIntent().getStringExtra("emojiid");
            if(NameID!=null)
                addpictures(NameID);
            else
        //        Content_1.setText(savetext.substring(1));
       //     sign=0;
        //}else
            addpictures();
    }
    //重写onDestroy方法，在退出编辑返回首页的时候获得输入内容
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText=String.valueOf(musicnote)+Content_1.getText().toString();
        save(inputText);
        intentms.putExtra("music",0);
        ViewLogs.this.startService(intentms);//
        conn = new Myconnection();//
        bindService(intentms, conn, BIND_AUTO_CREATE);//
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    musicnote=data.getIntExtra("musicnote",0);
                }
            case 2:
                if(resultCode==RESULT_OK)
                {
                    NameID=data.getStringExtra("emojiid");
                }
                else
                {
                    NameID="";
                }
        }
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
                content.append(line+"\n");
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
    public void addpictures(String id) {
        StringBuffer s = new StringBuffer(savetext);
        if (index < 0 || index >= savetext.length()) {
            s.append(id);
        } else {
            s.insert(index, id);//光标所在位置插入文字
        }
        //System.out.println(s);
        SpannableString addemoji = new SpannableString(s);
        Drawable e = null;
        //e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
        for (int i = 0; i < s.length(); i++) {
            //if(s.charAt(i)=='[') {
            ImageSpan emoji;
            switch (s.charAt(i)) {
                case '璽':
                    e = getResources().getDrawable(R.drawable.laugh);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '嚳':
                    e = getResources().getDrawable(R.drawable.cry);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '郄':
                    e = getResources().getDrawable(R.drawable.arrogance);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '贔':
                    e = getResources().getDrawable(R.drawable.dementia);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '鞷':
                    e = getResources().getDrawable(R.drawable.unhappy);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '鼊':
                    e = getResources().getDrawable(R.drawable.sweat);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '韘':
                    e = getResources().getDrawable(R.drawable.think);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case '鏚':
                    e = getResources().getDrawable(R.drawable.good);
                    e.setBounds(0, 0, 40, 40);
                    emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                    addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
            }
        }
    //}
        Content_1.setText(addemoji.subSequence(1,addemoji.length()));
        //Content.setText("jshjkshu");
    }
    public void addpictures()
    {
        System.out.println(savetext);
        SpannableString addemoji = new SpannableString(savetext);
        Drawable e=null;
        //e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
        for(int i=0;i<savetext.length();i++) {
            //if(savetext.charAt(i)=='[') {
                ImageSpan emoji;
                switch(savetext.charAt(i)) {
                    case '璽':
                        e = getResources().getDrawable(R.drawable.laugh);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '嚳':
                        e = getResources().getDrawable(R.drawable.cry);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '郄':
                        e = getResources().getDrawable(R.drawable.arrogance);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '贔':
                        e = getResources().getDrawable(R.drawable.dementia);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '鞷':
                        e = getResources().getDrawable(R.drawable.unhappy);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '鼊':
                        e = getResources().getDrawable(R.drawable.sweat);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '韘':
                        e = getResources().getDrawable(R.drawable.think);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    case '鏚':
                        e = getResources().getDrawable(R.drawable.good);
                        e.setBounds(0, 0, 40, 40);
                        emoji = new ImageSpan(e, ImageSpan.ALIGN_BASELINE);
                        addemoji.setSpan(emoji, i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                }
            }
        Content_1.setText(addemoji.subSequence(1,addemoji.length()));
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
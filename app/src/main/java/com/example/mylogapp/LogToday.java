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
    String NameID = null;
    static int pausesign = 0;
    static String savetext = "0";
    static int index;

    Myconnection conn;//
    Intent intentms;//
    static int musicnote = 0;//
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
        //musicnote=this.getIntent().getIntExtra("musicnote",0);
        //loadjudge=this.getIntent().getStringExtra("loadjudge");
        System.out.println(musicnote + "A");

        Button music = (Button) findViewById(R.id.music);
        Button emoji = (Button) findViewById(R.id.emoji);
        //Button backmain=(Button)findViewById(R.id.backmain);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausesign = 0;
                savetext = String.valueOf(musicnote) + Content.getText().toString();
                Intent intentmusic = new Intent(LogToday.this, MusicList.class);
                startActivityForResult(intentmusic, 1);
            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausesign = 0;
                index = Content.getSelectionStart() + 1;
                savetext = String.valueOf(musicnote) + Content.getText().toString();
                // System.out.println(savetext);
                Intent intentemoji = new Intent(LogToday.this, EmojiList.class);
                //intentemoji.putExtra("musicnote",musicnote);
                startActivityForResult(intentemoji, 2);
            }
        });

        Content = (EditText) findViewById(R.id.Content);
        String inputText = load();
        //if(sign==0)
        savetext = load();
        if (load() == "") {
            inputText = "0";
            savetext = "0";
        }
        System.out.println(musicnote + "B");
        //System.out.println(sign);
        if (!TextUtils.isEmpty(inputText.substring(1))) {
            Content.setText(inputText.substring(1));
            Content.setSelection(inputText.substring(1).length());
        }
        //if(sign==0)
        if (musicnote != 0) {//
            intentms.putExtra("music", musicnote);//
            LogToday.this.startService(intentms);//
            conn = new Myconnection();//
            bindService(intentms, conn, BIND_AUTO_CREATE);//
        }//
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pausesign == 1) {
            String inputText = load();
            savetext = load();
            if (load() == "") {
                inputText = "0";
                savetext = "0";
            }
            if (!TextUtils.isEmpty(inputText.substring(1))) {
                Content.setText(inputText.substring(1));
                Content.setSelection(inputText.substring(1).length());
            }
            if (musicnote != 0) {
                intentms.putExtra("music", musicnote);//
                LogToday.this.startService(intentms);//
                conn = new Myconnection();//
                bindService(intentms, conn, BIND_AUTO_CREATE);//
            }
        }
        if (NameID != null) {
            addpictures(NameID);
            NameID=null;
        }
        else
            addpictures();
        pausesign = 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pausesign == 1) {
            String inputText = String.valueOf(musicnote) + Content.getText().toString();
            save(inputText);
            intentms.putExtra("music", 0);
            LogToday.this.startService(intentms);
            conn = new Myconnection();
            bindService(intentms, conn, BIND_AUTO_CREATE);
        }
    }

    //重写onDestroy方法，在退出编辑返回首页的时候获得输入内容
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = String.valueOf(musicnote) + Content.getText().toString();
        System.out.println("save");
        save(inputText);
        intentms.putExtra("music", 0);
        LogToday.this.startService(intentms);//
        conn = new Myconnection();//
        bindService(intentms, conn, BIND_AUTO_CREATE);//
        System.out.println(musicnote);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    musicnote = data.getIntExtra("musicnote", 0);
                }
            case 2:
                if (resultCode == RESULT_OK) {
                    NameID = data.getStringExtra("emojiid");
                } else {
                    NameID = "";
                }
        }
    }

    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {//文本写入文件
            out = openFileOutput(date_string, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {//异常处理
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput(date_string);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            //逐行读取写入content
            int count=0;
            while ((line = reader.readLine()) != null) {
                if((line.equals("0") || line.equals("1") || line.equals("2") || line.equals("3"))&&count==0)
                    content.append(line);
                else
                    content.append(line + "\n");
                count++;
            }
            musicnote = Integer.valueOf(content.toString().substring(0, 1));
        } catch (IOException e) {//异常处理
            e.printStackTrace();
        } finally {
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
        Content.setText(addemoji.subSequence(1, addemoji.length()));
        //Content.setText("jshjkshu");
    }

    public void addpictures() {
        System.out.println(savetext);
        SpannableString addemoji = new SpannableString(savetext);
        Drawable e = null;
        //e.setBounds(0, 0, e.getIntrinsicWidth(), e.getIntrinsicHeight());
        for (int i = 0; i < savetext.length(); i++) {
            //if (savetext.charAt(i) == '[') {
            ImageSpan emoji;
            switch (savetext.charAt(i)) {
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
        Content.setText(addemoji.subSequence(1, addemoji.length()));
    }

    private class Myconnection implements ServiceConnection {//

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    }
}
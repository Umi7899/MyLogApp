package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicList extends AppCompatActivity {
    private ListView musiclist;
    public String musicname;
    Myconnection conn;
    private Musicservice.MyMusicBinder control;
    int musicnote=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        musiclist=(ListView) (ListView) findViewById(R.id.musiclist);
//        Button Return=(Button) findViewById(R.id.Returnlogtoday);
//        Return.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentback=new Intent();
//                intentback.putExtra("musicnote",musicnote);
//                setResult(RESULT_OK,intentback);
//                finish();
//            }
//        });
        //if(ContextCompat.checkSelfPermission(MusicList.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        //{
       //     ActivityCompat.requestPermissions(MusicList.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
       // }else
       // {
            initMediaPlayer();
       // }

    }
    private void initMediaPlayer(){
        try{
            String[] mlist ={"平淡的音乐.mp3","感伤的音乐.mp3","欢快的音乐.mp3","暂停播放"};
            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(MusicList.this,android.R.layout.simple_list_item_1,mlist);
            //设置Adapter
            musiclist.setAdapter(mAdapter);
            musiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    musicnote=position+1;
                    Intent intent=new Intent(MusicList.this,Musicservice.class);
                    intent.putExtra("music",position+1);
                    MusicList.this.startService(intent);
                     conn=new Myconnection();
                    bindService(intent,conn, Context.BIND_AUTO_CREATE);
                    Intent intentback=new Intent();
                    intentback.putExtra("musicnote",musicnote);
                    setResult(RESULT_OK,intentback);
                    finish();
                }
            });}catch (Exception e){
            e.printStackTrace();
        }
    }
    private class Myconnection implements ServiceConnection{
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {

        }
        public void onServiceDisconnected(ComponentName componentName)
        {

        }
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                initMediaPlayer();
                }else{
                    Toast.makeText(this,"无法使用插入音乐",Toast.LENGTH_SHORT).show();
                    Intent back=new Intent(MusicList.this,LogToday.class);
                    startActivity(back);
                }
        }
    }
}
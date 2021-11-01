package com.example.mylogapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.TimerTask;

public class Musicservice extends Service{
    private MediaPlayer player;
    public Musicservice() { }
    @Override
    public IBinder onBind(Intent intent) {
        return new MyMusicBinder();
    }
    public class MyMusicBinder extends Binder{
        Musicservice getService(){
            return Musicservice.this;
        }
    }
    public void onCreate(){
        super.onCreate();
        player=MediaPlayer.create(this,R.raw.music1);
    }
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        super.onStartCommand(intent, flags, startId);
        switch(intent.getIntExtra("music",0)){
            case 0:
                //player.stop();
                //player.release();
                if(player!=null&&player.isPlaying())
                    player.pause();
                break;
            case 1:
                player.reset();
                player=MediaPlayer.create(this,R.raw.music1);
                if(!player.isPlaying())
                {
                    player.start();
                    player.setLooping(true);
                }
                break;
            case 2:
                player.reset();
                player=MediaPlayer.create(this,R.raw.music2);
                if(!player.isPlaying())
                {
                    player.start();
                    player.setLooping(true);
                }
                break;
            case 3:
                player.reset();
                player=MediaPlayer.create(this,R.raw.music3);
                if(!player.isPlaying())
                {
                    player.start();
                    player.setLooping(true);
                }
                break;
            case 4:
                if(player!=null&&player.isPlaying())
                player.pause();
                break;
        }
        return START_STICKY;
    }
    public void onDestroy(){
        super.onDestroy();
        if(player.isPlaying())
        {
            player.stop();//停止播放音乐
        }
        player.release();
    }
}
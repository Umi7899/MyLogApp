package com.example.mylogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmojiList extends AppCompatActivity {
    private ListView emojilist;
    private List<Picture> picturelist=new ArrayList<>();
    int musicnote=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_list);
        musicnote=this.getIntent().getIntExtra("musicnote",0);
        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentback=new Intent(EmojiList.this,LogToday.class);
                intentback.putExtra("emojiid","");
                startActivity(intentback);
            }
        });
        emojilist=(ListView) (ListView) findViewById(R.id.emojilist);
        //if(ContextCompat.checkSelfPermission(EmojiList.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
       // {
       //     ActivityCompat.requestPermissions(EmojiList.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
       // }else
       // {
            initPicture();
            EmojiAdapter adapter=new EmojiAdapter(EmojiList.this,R.layout.picture_item,picturelist);
            emojilist.setAdapter(adapter);
            emojilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Picture picture=picturelist.get(position);
                    //SpannableString addemoji=new SpannableString("");
                    //addemoji.setSpan(picture.spanStr,addemoji.length()-1,addemoji.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    Intent logtoday=new Intent(EmojiList.this,LogToday.class);
                    logtoday.putExtra("emojiid",picture.getNameID());
                    logtoday.putExtra("musicnote",musicnote);
                    logtoday.putExtra("loadjudge","N");
                    startActivity(logtoday);
                }
            });
        //}
    }
    private void initPicture(){
        Picture laugh=new Picture("笑","[emoji0]",R.drawable.laugh);
        picturelist.add(laugh);
        Picture cry=new Picture("哭","[emoji1]",R.drawable.cry);
        picturelist.add(cry);
        Picture arrogance=new Picture("嚣张","[emoji2]",R.drawable.arrogance);
        picturelist.add(arrogance);
        Picture dementia=new Picture("痴呆","[emoji3]",R.drawable.dementia);
        picturelist.add(dementia);
        Picture unhappy=new Picture("沮丧","[emoji4]",R.drawable.unhappy);
        picturelist.add(unhappy);
        Picture sweat=new Picture("流汗","[emoji5]",R.drawable.sweat);
        picturelist.add(sweat);
        Picture think=new Picture("思考","[emoji6]",R.drawable.think);
        picturelist.add(think);
        Picture good=new Picture("棒","[emoji7]",R.drawable.good);
        picturelist.add(good);
    }
    public class Picture
    {
        private String name;
        private String nameID;
        private int imageID;
        public Picture(String name,String nameID,int imageID)
        {
            this.nameID=nameID;
            this.name=name;
            this.imageID=imageID;
        }
        public String getName()
        {
            return name;
        }
        public String getNameID()
        {
            return nameID;
        }
        public int getImageID()
        {
            return imageID;
        }
    }
    public class EmojiAdapter extends ArrayAdapter<Picture>{
        private int resourceID;
        public EmojiAdapter(Context context, int textViewResourceID, List<Picture> objects)
        {
            super(context,textViewResourceID,objects);
            resourceID=textViewResourceID;
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Picture picture=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            ImageView pictureImage=(ImageView) view.findViewById(R.id.picture_image);
            TextView pictureName=(TextView) view.findViewById(R.id.picture_name);
            pictureImage.setImageResource(picture.getImageID());
            pictureName.setText(picture.getName());
            return view;
        }
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initPicture();
                }else{
                    Toast.makeText(this,"无法使用插入表情",Toast.LENGTH_SHORT).show();
                    Intent back=new Intent(EmojiList.this,LogToday.class);
                    startActivity(back);
                }
        }
    }
}
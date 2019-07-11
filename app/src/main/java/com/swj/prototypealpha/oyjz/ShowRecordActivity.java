package com.swj.prototypealpha.oyjz;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.swj.prototypealpha.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowRecordActivity extends AppCompatActivity {
    private List<Record> recordList = new ArrayList<>();
    private MediaPlayer mPlayer = null;
    private boolean isPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);
        initRecord();
        RecordAdapter adapter = new RecordAdapter(ShowRecordActivity.this,R.layout.record_item,recordList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Record record = recordList.get(i);
                if(!isPlaying){
                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mPlayer.reset();
                            mPlayer.release();
                            isPlaying = false;
                        }
                    });
                    try {
                        mPlayer.setDataSource(record.getPath());
                        mPlayer.prepare();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    mPlayer.start();
                    isPlaying = true;
                }
                else{
                    mPlayer.stop();
                    mPlayer.reset();
                    mPlayer.release();
                    isPlaying = false;
                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mPlayer.reset();
                            mPlayer.release();
                            isPlaying = false;
                        }
                    });
                    try {
                        mPlayer.setDataSource(record.getPath());
                        mPlayer.prepare();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    mPlayer.start();
                    isPlaying = true;
                }
            }
        });
    }

    private void initRecord(){
        File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
        File[] files = dir.listFiles();
        for (File file : files) {
            recordList.add(new Record(file.getName(),file.getAbsolutePath()));
        }
    }
}

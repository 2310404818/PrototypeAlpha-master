package com.swj.prototypealpha.oyjz;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.swj.prototypealpha.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowRecordActivity extends AppCompatActivity {
    private List<Record> recordList = new ArrayList<>();
    private MediaPlayer mPlayer = null;
    private boolean isPlaying = false;
    private Toolbar toolbar;
    public void setToolbar(){
        toolbar = findViewById(R.id.list_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);
        initRecord();
        setToolbar();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(isPlaying) {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            isPlaying = false;
        }
    }

    private void initRecord(){
        File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
        File[] files = dir.listFiles();
        for (File file : files) {
            recordList.add(new Record(file.getName(),file.getAbsolutePath()));
        }
        Collections.sort(recordList, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  //应用程序图标的id
        {
            if(isPlaying) {
                mPlayer.stop();
                mPlayer.reset();
                mPlayer.release();
                isPlaying = false;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
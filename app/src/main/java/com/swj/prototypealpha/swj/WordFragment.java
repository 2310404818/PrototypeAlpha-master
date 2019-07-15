package com.swj.prototypealpha.swj;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.ShowRecordActivity;
import java.io.File;
import java.io.IOException;
import kr.co.namee.permissiongen.PermissionGen;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 笔录界面
 * 文字笔录
 * 录音笔录
 */

public class WordFragment extends Fragment {

    private Button btn_control;
    private Button btn_record;
    private boolean isStart = false;
    private MediaRecorder mr = null;
    public static TextView text_word_foundation;
    public static TextView text_word_record;
    public static String word_foundation;
    public static String word_question;
    EditText edit_word_foundation;
    EditText edit_word_question;
    FloatingActionButton  fab_foundation_submit;
    FloatingActionButton fab_record_submit;
    LinearLayout linearLayout;

    @SuppressLint("RestrictedApi")
    private void initUI()
    {
        linearLayout = getActivity().findViewById(R.id.linearLayout_focus);
        text_word_foundation = getActivity().findViewById(R.id.word_foundation_lable);
        text_word_record = getActivity().findViewById(R.id.word_question_record_lable);

        edit_word_foundation = getActivity().findViewById(R.id.word_doundation_edit);
        edit_word_foundation.setText("现场情况符合要求");
        edit_word_foundation.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edit_word_foundation.setGravity(Gravity.TOP);
        edit_word_foundation.setSingleLine(false);
        edit_word_foundation.setHorizontallyScrolling(false);

        edit_word_question = getActivity().findViewById(R.id.word_question_record_edit);
        edit_word_question.setText("要求1：XXXXXXXXXXXXXXXXXXXX"+
                "要求2：XXXXXXXXXXXXXXXXX");

        edit_word_question.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        edit_word_question.setGravity(Gravity.TOP);
        //改变默认的单行模式
        edit_word_question.setSingleLine(false);
        //水平滚动设置为False
        edit_word_question.setHorizontallyScrolling(false);

        fab_record_submit = getActivity().findViewById(R.id.flb_word_question_record_submit);

        fab_foundation_submit = getActivity().findViewById(R.id.flb_word_foundation_submit);


    }

    private void setOnClickListener()
    {
        fab_foundation_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                String foundation = edit_word_foundation.getText().toString()+'\n';
                String old_foundation = text_word_foundation.getText().toString();

                word_foundation = foundation;

                text_word_foundation.setText(old_foundation+foundation);
                linearLayout.setFocusableInTouchMode(true);
                edit_word_foundation.clearFocus();
                fab_foundation_submit.setVisibility(View.GONE);
                fab_record_submit.setVisibility(View.GONE);

            }
        });
        fab_record_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                String record = edit_word_question.getText().toString()+'\n';
                String old_rocord = text_word_record.getText().toString();
                text_word_record.setText(old_rocord+record);

                word_question = record;

                linearLayout.setFocusableInTouchMode(true);
                edit_word_question.clearFocus();
                fab_record_submit.setVisibility(View.GONE);
                fab_foundation_submit.setVisibility(View.GONE);
            }
        });

        edit_word_foundation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    fab_foundation_submit.setVisibility(View.VISIBLE);

                }
                else {
                    fab_foundation_submit.setVisibility(View.GONE);

                }
            }
        });

        edit_word_question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fab_record_submit.setVisibility(View.VISIBLE);

                } else {
                    fab_record_submit.setVisibility(View.GONE);


                }
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        setOnClickListener();

        request();
        btn_record = (Button) getView().findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShowRecordActivity.class);
                startActivity(intent);
            }
        });
        btn_control = (Button) getView().findViewById(R.id.btn_control);
        btn_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart){
                    startRecord();
                    btn_control.setText("停止录制");
                    isStart = true;
                }else{
                    stopRecord();
                    btn_control.setText("开始录制");
                    isStart = false;
                }
            }
        });
    }
    File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
    //开始录制
    private void startRecord(){
        if(mr == null){
            if(!dir.exists()){
                dir.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            File soundFile = new File(dir,str+".amr");
            if(!soundFile.exists()){
                try {
                    soundFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            mr = new MediaRecorder();
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);  //音频输入源
            mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);   //设置输出格式
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);   //设置编码格式
            mr.setOutputFile(soundFile.getAbsolutePath());
            try {
                mr.prepare();
                mr.start();  //开始录制
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //停止录制，资源释放
    private void stopRecord(){
        if(mr != null){
            mr.stop();
            mr.release();
            mr = null;
        }
    }

    private void request() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.WAKE_LOCK
                        , Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false);
    }


}

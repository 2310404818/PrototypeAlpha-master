package com.swj.prototypealpha.swj;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.ShowRecordActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import kr.co.namee.permissiongen.PermissionGen;

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
    private RequestParams params = new RequestParams();
    private String encodedString;
    private File soundFile;
    private String ProjectName,Address;
    private File dir;
    EditText edit_word_foundation;
    EditText edit_word_question;
    EditText editText;
    String str,date,time;
    LinearLayout linearLayout;
    MyApplication myApplication;

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
        editText = getActivity().findViewById(R.id.editText);
        editText.setText("跟踪检查");
        edit_word_question.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        edit_word_question.setGravity(Gravity.TOP);
        //改变默认的单行模式
        edit_word_question.setSingleLine(false);
        //水平滚动设置为False
        edit_word_question.setHorizontallyScrolling(false);




    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        myApplication = (MyApplication) getActivity().getApplication();
        ProjectName = myApplication.getProjectName();
        Address = myApplication.getAddress();
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"voice");
        if(!dir1.exists()){
            dir1.mkdirs();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        dir = new File(dir1,datenow);
        if (!dir.exists()){
            dir.mkdirs();
        }
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


    //开始录制
    private void startRecord(){
        myApplication = (MyApplication) getActivity().getApplication();
      //  dir = new File(myApplication.getFile(),myApplication.getProjectName());
        if(mr == null){
            if(!dir.exists()){
                dir.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH.mm.ss");
            SimpleDateFormat myDate = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat myTime = new SimpleDateFormat("HH.mm.ss");
            Date curDate = new Date(System.currentTimeMillis());
            str = formatter.format(curDate);
            date = myDate.format(curDate);
            time = myTime.format(curDate);
            soundFile = new File(dir,str+".amr");
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
            uploadVoice();
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
    //上传录音
    public void uploadVoice() {
      //  String file =null;
        if (soundFile!=null) {
                encodeImagetoString();

        } else {
            Toast.makeText(getActivity(), "没有指定录音文件或文件为空",
                    Toast.LENGTH_LONG).show();
        }
    }
    //录音转字符流
    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                File fileVoice = new File(String.valueOf(soundFile));
                InputStream voiceStream = null;
                byte[] byte_arr = null;
                try {
                    voiceStream = new FileInputStream(fileVoice);
                    byte_arr = new byte[voiceStream.available()];
                    encodedString = Base64.encodeToString(byte_arr, 0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                // 将转换后的录音添加到上传的参数中
                params.put("voice", encodedString);
                params.put("date", date);
                params.put("time",time);
                params.put("projectName",ProjectName);
                params.put("address",Address);
                // 上传录音
                iUpload();
            }
        }.execute(null, null, null);
    }
    public void iUpload() {
        String url = "http://47.102.119.140:8080/mobile_inspection_war/uploadVoice.jsp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
             //   Toast.makeText(getActivity(), "upload success", Toast.LENGTH_LONG).show();
                Log.d("成功","低矮降低");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                if (statusCode == 404) {
                    Toast.makeText(getActivity(),
                            "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // 当 Http 响应码'500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity(),
                            "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // 当 Http 响应码 404, 500
                else {
                    Toast.makeText(
                            getActivity(), "Error Occured n Most Common Error: n1. Device " +
                                    "not connected to Internetn2. Web App is not deployed in App servern3." +
                                    " App server is not runningn HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

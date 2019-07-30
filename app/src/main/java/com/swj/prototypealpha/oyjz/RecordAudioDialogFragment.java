package com.swj.prototypealpha.oyjz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.melnykov.fab.FloatingActionButton;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * 开始录音的 DialogFragment
 *
 * Created by developerHaoz on 2017/8/12.
 */

public class RecordAudioDialogFragment extends DialogFragment {

    private static final String TAG = "RecordAudioDialogFragme";
    private File soundFile;
    private MediaRecorder mRecorder = null;
    private int mRecordPromptCount = 0;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private TimerTask mIncrementTimerTask = null;
    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    private RequestParams params = new RequestParams();
    private String encodedString;

    long timeWhenPaused = 0;

    private FloatingActionButton mFabRecord;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;

    private OnAudioCancelListener mListener;

    public static RecordAudioDialogFragment newInstance() {
        RecordAudioDialogFragment dialogFragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_record_audio, null);
        initView(view);

        mFabRecord.setColorNormal(getResources().getColor(R.color.colorPrimary));
        mFabRecord.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));

        mFabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
            }
        });

        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void initView(View view) {
        mChronometerTime = (Chronometer) view.findViewById(R.id.record_audio_chronometer_time);
        mFabRecord = (FloatingActionButton) view.findViewById(R.id.record_audio_fab_record);
        mIvClose = (ImageView) view.findViewById(R.id.record_audio_iv_close);
    }

    private void onRecord(boolean start) {
        if (start) {
            mFabRecord.setImageResource(R.drawable.ic_media_stop);
            Toast.makeText(getActivity(), "开始录音...", Toast.LENGTH_SHORT).show();
            mChronometerTime.setBase(SystemClock.elapsedRealtime());
            mChronometerTime.start();
            Log.d("低价就及大家就","i大家积极");
            startRecording();
            Log.d("及大家就","i大家积极");
        } else {
            mFabRecord.setImageResource(R.drawable.ic_mic_white_36dp);
            mChronometerTime.stop();
            timeWhenPaused = 0;
            Toast.makeText(getActivity(), "录音结束...", Toast.LENGTH_SHORT).show();
            stopRecording();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void setOnCancelListener(OnAudioCancelListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    onRecord(mStartRecording);
                }
                break;
        }
    }

    public interface OnAudioCancelListener {
        void onCancel();
    }
    public void startRecording() {
        setFileNameAndPath();
        Log.d("好的花花u花花","i大家急啊急");
        mRecorder     = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  //音频输入源
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);   //设置输出格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);   //设置编码格式
        mRecorder.setOutputFile(soundFile.getAbsolutePath());
        try {
            mRecorder.prepare();
            mRecorder.start();  //开始录制
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileNameAndPath() {
        Log.d("就嗲家急啊急啊急","基地啊及佳佳");
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"voice");
        if(!dir1.exists()){
            dir1.mkdirs();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        File dir = new File(dir1,datenow);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH.mm.ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        soundFile = new File(dir,str+".amr");
        Log.d(String.valueOf(soundFile),"调集骄傲");
        if(!soundFile.exists()){
            try {
                soundFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
  //      Record myrecord = new Record()
        mRecorder.release();
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }
        mRecorder = null;
        uploadVoice();
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
                MyApplication myApplication= (MyApplication) getActivity().getApplication();
                SimpleDateFormat myDate = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat myTime = new SimpleDateFormat("HH.mm.ss");
                Date curDate = new Date(System.currentTimeMillis());
                String date = myDate.format(curDate);
                String time = myTime.format(curDate);
                params.put("voice", encodedString);
                params.put("date", date);
                params.put("time",time);
                params.put("projectName",myApplication.getProjectName());
                params.put("address",myApplication.getAddress());
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
    /**
     * 上传edittext实时监控
     */
    public  void WordFragmentRequest(final String projectName, final String address, final String date, final String Type,final String result,final String require) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/NoteUpload";
        String tag = "WordFragment";
        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //获取参数
                            String result = jsonObject.getString("Result");  //获取请求结果
                            if (result.equals("success")) {  //如果结果返回为成功
                                Log.d("笔录","上传成功");
                            }
                            else {
                                //做自己的登录失败操作，如Toast提示
                                Log.d("笔录","上传失败");
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                            //    Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("projectName", projectName);  //注⑥
                params.put("address", address);
                params.put("date",date);
                params.put("type",Type);
                params.put("result",result);
                params.put("require",require);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }


}

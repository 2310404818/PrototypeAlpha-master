package com.swj.prototypealpha.swj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.RequestParams;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.RecordAudioDialogFragment;
import com.swj.prototypealpha.oyjz.ShowRecordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private RequestParams params = new RequestParams();
    private String encodedString;
    private String datenow;
    private File soundFile;
    private String ProjectName,Address;
    private File dir;
    private String edit_foindation="现场情况符合要求";
    private String edit_question="要求";
    private String edit_type="检查类型";
    private Chronometer mChronometerTime;
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
        //现场情况
        edit_word_foundation = getActivity().findViewById(R.id.word_doundation_edit);
        edit_word_foundation.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edit_word_foundation.setGravity(Gravity.TOP);
        edit_word_foundation.setSingleLine(false);
        edit_word_foundation.setHorizontallyScrolling(false);
        //执法类型
        editText = getActivity().findViewById(R.id.editText);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setGravity(Gravity.TOP);
        editText.setSingleLine(false);
        editText.setHorizontallyScrolling(false);
        //执法措施和要求
        edit_word_question = getActivity().findViewById(R.id.word_question_record_edit);

        edit_word_foundation.setHint("现场情况符合要求");
        edit_word_question.setHint("要求");
        editText.setHint("检查类型");
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
        datenow = date1.format(date);
        dir = new File(dir1,datenow);
        if (!dir.exists()) {
            dir.mkdirs();
        }
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
                startRecord();
            }
        });

        edit_word_foundation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_foindation = s.toString();
            }
        });

        edit_word_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_question = s.toString();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_type = s.toString();
            }
        });
        setTextNow(ProjectName,Address,datenow);
    }

    /**
     * 判断是否离开了当前fragment
     */
    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if (hidden){

        }
        else {
            myApplication = (MyApplication) getActivity().getApplication();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
            String date2 = date1.format(date);
            if (edit_foindation.equals("现场情况符合要求") && edit_question.equals("要求") && edit_type.equals("检查类型"))
            {

            }
            else {
                edit_word_foundation.setText(edit_foindation);
                edit_word_question.setText(edit_question);
                editText.setText(edit_type);
                WordFragmentRequest(myApplication.getProjectName(),myApplication.getAddress(),date2,edit_type,edit_foindation,edit_question);
            }

        }
    }

    //开始录制
    private void startRecord(){

        final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
        fragment.show(getFragmentManager(),RecordAudioDialogFragment.class.getSimpleName());
        fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
            @Override
            public void onCancel() {
                fragment.dismiss();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word, container, false);
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
    /**
     * 拉取笔录
     */
    public  void setTextNow(final String checkProject, final String address,final String date) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/NoteDownload";
        String tag = "Check";
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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);  //获取参数
                            String MeasuresAndRequirements = jsonObject.getString("MeasuresAndRequirements");
                            String  Situation = jsonObject.getString("Situation");
                            String CheckType = jsonObject.getString("CheckType");
                            if (MeasuresAndRequirements.equals("null") && Situation.equals("null") && CheckType.equals("null")){

                            }
                            else {
                                edit_word_foundation.setText(Situation);
                                edit_word_question.setText(MeasuresAndRequirements);
                                editText.setText(CheckType);
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
                params.put("projectName",checkProject);
                params.put("address",address);
                params.put("date",date);

                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }

}

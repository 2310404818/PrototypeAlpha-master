package com.swj.prototypealpha.swj;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.ChooseCheckPerson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cz.msebera.android.httpclient.Header;


/**
 * 审阅界面
 * 检查项目、检查地点、检查时间、检查人、记录人、被检查人、建设单位
 * 监理单位、基本情况、执法措施和要求、签名
 * 开始签名、记录提交
 */

public class LookupFragment extends Fragment {
    private     TextView             text_look_proj;
    private     TextView             text_look_time;
    private     TextView             text_look_addr;
    private     TextView             text_bulid;
    private     TextView             text_check;
    private     TextView             text_look_foundation;
    private     TextView             text_look_rocord;
    private     TextView             text_checkpeople;
    private     TextView             text_donepeople;
    private     Button               button;
    private     Button               button_complete;
    private     File                 dir ;
    private     RecyclerView         re_picture;
    private     SimpleDateFormat     sdf;
    private     Date                 newTime;
    private     String               ProjectName;
    private     String               Address;
    private     RequestParams        params = new RequestParams();
    private     String[]             encodedString = new String[6];
    public      static   SignAdapter signadapter;
    private     static   List<ChargePerson> nameList = new ArrayList<>();
    public      static   List<Picture> signList    = new ArrayList<>();
    MyApplication myApplication;
    private String datenow;
    Timer timer = new Timer();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lookup, container, false);
    }
    public void initData(){
            nameList.clear();
            signList.clear();
            initAutograph();
            signadapter.notifyDataSetChanged();

    }
    /**
     * 获取对应目录下的文件
     */
    private void initAutograph(){
        nameList.clear();
        signList.clear();
        myApplication = (MyApplication) getActivity().getApplication();
        String name = dir.getName();
        //先进入二级目录，在进入三级目录加载三级目录下所有文件
        File myDir = new File(myApplication.getFile(),myApplication.getProjectName()+"autograph");
        File dir11 = new File(myDir,name);
        File[] files = dir11.listFiles();
        if (files!=null){
            for (File file : files) {
                  String nameForMe = file.getName();
                  ChargePerson myNameFor = new ChargePerson(nameForMe);
                  nameList.add(myNameFor);
                  Picture picture = new Picture(BitmapFactory.decodeFile(file.getAbsolutePath()));
                 signList.add(picture);
            }
        }
    }

    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            ProjectRequest(ProjectName,Address,datenow);
        }
        super.onHiddenChanged(hidden);
    }

    /**
     * 点击发起签名弹出框
     * 点击提交弹出框
     *
     */
    public void DialogFor(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定发起签名？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =new Intent(getActivity(),ChooseCheckPerson.class);
                        startActivityForResult(intent, 202);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }
    public void Dialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定提交检查记录？提交以后不可修改")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (signList.size()!=0){
                            for (int i=0;i<signList.size();i++){
                                uploadImage(i);
                            }
                            Toast.makeText(getActivity(),"记录已提交",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),CheckPerson.class);
                            intent.putExtra("title", ProjectName);
                            intent.putExtra("address",Address);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getActivity(),"未有签名文件，上传失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }
    public void initUI(){
        text_look_foundation = getActivity().findViewById(R.id.text_look_foundation);
        text_look_rocord = getActivity().findViewById(R.id.text_lookup_record);
        text_bulid = getActivity().findViewById(R.id.text_buildeproj);
        text_check = getActivity().findViewById(R.id.text_checkproj);
        text_look_time = getActivity().findViewById(R.id.text_currenttime4);
        text_look_addr = getActivity().findViewById(R.id.text_addrproj);
        newTime = new Date();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        text_look_time.setText(sdf.format(newTime));
        text_look_proj = getActivity().findViewById(R.id.text_projchenck);
        text_checkpeople = getActivity().findViewById(R.id.text_checkpeople);
        text_donepeople = getActivity().findViewById(R.id.text_donepeople);
        button = getActivity().findViewById(R.id.button);
        button_complete=getActivity().findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFor();
            }
        });
        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });
        re_picture =getActivity().findViewById(R.id.re_picture);
        re_picture.setLayoutManager(new GridLayoutManager(getActivity(),2));
        signadapter= new SignAdapter(signList,nameList);
        re_picture.setAdapter(signadapter);


    }
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        ProjectName = myApplication.getProjectName();
        Address = myApplication.getAddress();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
         datenow = date1.format(date);
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"autograph");
        if (!dir1.exists()){
            dir1.mkdir();
        }
        dir = new File(dir1,datenow);
        if (!dir.exists()){
            dir.mkdir();
        }
        ProjectRequest(ProjectName,Address,datenow);
        initUI();
        initAutograph();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                initData();

    }


    /**
     * 拉取信息
     */
    public void ProjectRequest(final String name, final String address,final String date) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/Review";    //注①
        String tag = "ProjectInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =  new JSONObject(response);  //获取参数
                         //   Log.i("返回参数", String.valueOf(jsonObject));
                            String UnitConstruction = jsonObject.getString("UnitConstruction");
                            String SupervisionUnion =jsonObject.getString("SupervisionUnion");
                            String Contractors =jsonObject.getString("Contractors");
                            String checkpPeople = jsonObject.getString("rummager");
                            String situation = jsonObject.getString("Situation");
                            String MeasuresAndRequirements = jsonObject.getString("MeasuresAndRequirements");
                            myApplication= (MyApplication) getActivity().getApplication();
                            text_bulid.setText(UnitConstruction);
                            text_check.setText(SupervisionUnion);
                            text_look_addr.setText(Address);
                            text_look_proj.setText(ProjectName);
                            text_checkpeople.setText(checkpPeople);
                            text_donepeople.setText(Contractors);
                            text_look_foundation.setText(situation);
                            text_look_rocord.setText(MeasuresAndRequirements);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                            //   Log.d("网络异常","大家都叫睡大觉");
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                //  Log.d("基地啊涉及到激动静安寺大家哦i的骄傲的","嗲话降低哦家的骄傲的静安寺哦");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("checkProject", name);  //注⑥
                params.put("address", address);
                params.put("checkTime",date);

                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    /**
     * 上传照片
     *
     */
     //开始上传图片
    private void uploadImage(int ix) {
        int ixf =ix;
        encodeImagetoString(ixf);
    }

    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString(final int ifor) {
            final int infor =ifor;
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
            };
            @Override
            protected String doInBackground(Void... params) {
                Bitmap bitmap = signList.get(infor).getImageID();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // 压缩图片
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Base64图片转码为String
                encodedString[infor] = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                //      prgDialog.setMessage("Calling Upload");
                // 将转换后的图片添加到上传的参数中
                params.put("image", encodedString[infor]);
                params.put("checkName",ProjectName);
                params.put("signatureName", nameList.get(infor).getName());
                params.put("date",sdf.format(newTime));
            //    Log.d(sdf.format(newTime),"大家打架");
           //     Log.d(nameList.get(infor).getName(),"23333333333333的，奥");
                // 上传图片
                imageUpload();
            }
        }.execute(null, null, null);
    }

    public void imageUpload() {
        String url = "http://47.102.119.140:8080/mobile_inspection_war/uploadSignature.jsp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
           //     Toast.makeText(getActivity(), "upload success", Toast.LENGTH_LONG).show();
                Log.d("upload success","上传成功");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 404) {
                    Log.d("上传失败","就是这样");
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // 当 Http 响应码'500'
                else if (statusCode == 500) {
                    Log.d("出了点小问题","就是这样");
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // 当 Http 响应码 404, 500
                else {
                    Log.d("233333333333","就是这样");
                    Toast.makeText(getActivity(), "Error Occured n Most Common Error: n1. Device " +
                            "not connected to Internetn2. Web App is not deployed in App servern3." +
                            " App server is not runningn HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

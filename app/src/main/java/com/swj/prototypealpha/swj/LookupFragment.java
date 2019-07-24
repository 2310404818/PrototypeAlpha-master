package com.swj.prototypealpha.swj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.ChooseCheckPerson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 审阅界面
 * 检查项目、检查地点、检查时间、检查人、记录人、被检查人、建设单位
 * 监理单位、基本情况、执法措施和要求、签名
 * 开始签名、记录提交
 */

public class LookupFragment extends Fragment {
    TextView             text_look_proj;
    TextView             text_look_time;
    TextView             text_look_addr;
    TextView             text_bulid;
    TextView             text_check;
    TextView             text_look_foundation;
    TextView             text_look_rocord;
    TextView             text_checkpeople;
    TextView             text_writepeople;
    TextView             text_donepeople;
    Button               button;
    Button               button_complete;
    RecyclerView         re_picture;
    MyApplication myApplication;
    public static SignAdapter signadapter;
    private static List<ChargePerson> nameList = new ArrayList<>();
    public static   List<Picture> signList    = new ArrayList<>();


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lookup, container, false);
    }
    public void initData(){
        if (ChooseCheckPerson.flag!=0){
            nameList.clear();
            signList.clear();
     //       Log.d(String.valueOf(ChooseCheckPerson.flag),"的哈的话送降低哦啊降低哦啊降低哦按实际");
            for (int i=0;i<ChooseCheckPerson.flag;i++)
            {
                nameList.add(ChooseCheckPerson.name[i]);
                signList.add(ChooseCheckPerson.picture[i]);
            }
            signadapter.notifyDataSetChanged();
        }

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
                        Toast.makeText(getActivity(),"记录已提交",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),CheckPerson.class);
                        startActivity(intent);
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
        Date newTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        text_look_time.setText(sdf.format(newTime));
        text_look_proj = getActivity().findViewById(R.id.text_projchenck);
        text_checkpeople = getActivity().findViewById(R.id.text_checkpeople);
        text_writepeople = getActivity().findViewById(R.id.text_writepeople);
        text_donepeople = getActivity().findViewById(R.id.text_donepeople);
        button = getActivity().findViewById(R.id.button);
        button_complete=getActivity().findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFor();
                //       Intent intent =new Intent(getActivity(),ChooseCheckPerson.class);
                //     startActivityForResult(intent, 202);
            }
        });
        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Intent intent = new Intent(getActivity(),CheckPerson.class);
                //      startActivity(intent);
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

        String ProjectName = (String) getArguments().get("ProjectName");
        String Address = (String) getArguments().get("Address");
        ProjectRequest(ProjectName,Address);
        initUI();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202 && resultCode == 110) {
            for (int i = 0; i < 5; i++) {
                initData();
            }
        }
    }


    /**
     * 拉取项目详细信息
     */
    public void ProjectRequest(final String name, final String address) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/ProjectDetail ";    //注①
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
                            Log.i("返回参数",response);
                            JSONObject jsonObject =  new JSONObject(response);  //获取参数
                            Log.i("返回参数", String.valueOf(jsonObject));
                            String ProjectName =jsonObject.getString("projectName");
                            String Address = jsonObject.getString("address");
                            String UnitConstruction = jsonObject.getString("unitConstruction");
                            String SupervisionUnion =jsonObject.getString("supervisionUnion");
                            String Contractors =jsonObject.getString("contractors");
                            myApplication= (MyApplication) getActivity().getApplication();
                            text_bulid.setText(UnitConstruction);
                            text_check.setText(SupervisionUnion);
                            text_look_addr.setText(Address);
                            text_look_proj.setText(ProjectName);
                            text_checkpeople.setText(myApplication.getName());
                            text_writepeople.setText("李四");
                            text_donepeople.setText(Contractors);


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
                params.put("projectName", name);  //注⑥
                params.put("address", address);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }


}

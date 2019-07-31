package com.swj.prototypealpha.swj;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.swj.prototypealpha.activity.ChoosePerson;
import com.swj.prototypealpha.activity.SignedActivity;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发起检查界面
 * 详细信息
 * 签到
 * 发起检查
 * 检查人
 */
public class CheckPerson extends AppCompatActivity implements OnItemClickListener
{

    private Toolbar tlb_checkPerson;
    private RecyclerView recvv_checkPerson;
    private String title,address;
    private ItemAdapter adapter;
    private List<ItemBean> itemList = new ArrayList<>();
    private  ItemBean item4;
    private String[] person = new String[100];
    private int     num=0;
    MyApplication myApplication;
    private String myName;
    private void initUI()
    {
        recvv_checkPerson = findViewById(R.id.recv_addperson);
        tlb_checkPerson = findViewById(R.id.tlb_checklocperson);
        /**
         * 加载toolbat控件
         * 设置toolbar标题不显示
         * 设置左上角有返回小箭头，且小箭头可以点击
         */
        setSupportActionBar(tlb_checkPerson);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void DialogFor(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("确定发起检查？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(CheckPerson.this,StartActivity.class);
                        intent2.putExtra("title",title);
                        intent2.putExtra("address",address);
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
                        String datenow = date1.format(date);
                        CheckRequest(title,address,datenow,person);
                        startActivity(intent2);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_person);
        Intent intent =getIntent();
        title = intent.getStringExtra("title");
        address = intent.getStringExtra("address");
        myApplication= (MyApplication) getApplication();
        myApplication.setProject(title,address);
        myName = myApplication.getName();
        person[0]=myName;
        initUI();
        /**
         * 加载recyclerView控件的工具类LinearLayoutManager
         * 控制recyclerView控件中的item垂直向下显示
         * 加载适配器
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recvv_checkPerson.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(itemList);
        recvv_checkPerson.setAdapter(adapter);
        adapter.setItemClickListener(this);
        Update();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        CheckIsEx(myApplication.getProjectName(),myApplication.getAddress(),datenow,myName);
    }

    private void Update()
    {
        //加载图片资源
        itemList.clear();
        Bitmap proj_name = BitmapFactory.decodeResource(getResources(),R.mipmap.project_name);
        Bitmap proj_addr = BitmapFactory.decodeResource(getResources(),R.mipmap.proj_addr);
        Bitmap checker = BitmapFactory.decodeResource(getResources(),R.mipmap.checker);
        Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.right_arrow);
        Bitmap addPerson = BitmapFactory.decodeResource(getResources(),R.mipmap.check_add);
        Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);

        ItemBean item0 = new ItemBean("详细信息",title,proj_name,rightArrow);
        ItemBean item1 = new ItemBean("签到","开始",proj_addr,rightArrow);
        ItemBean item3 = new ItemBean("发起检查","确定",checker,rightArrow);
        itemList.add(item0);
        adapter.notifyItemChanged(0);
        itemList.add(item1);
        adapter.notifyItemChanged(1);
        itemList.add(item3);
        adapter.notifyItemChanged(2);
        MyApplication myApplication = (MyApplication) getApplication();
        item4 = new ItemBean("检查人",myName,startingtime,addPerson);
        itemList.add(item4);
        adapter.notifyItemChanged(3);
        adapter.notifyItemChanged(0,itemList.size());
    }

    @Override
    public void onItemClick(int position) {
      switch (position){
          case 0:
              Intent intent = new Intent(CheckPerson.this,ProjectInfoActivity.class);
              startActivity(intent);
              break;
          case 1:
              Intent intent1 = new Intent(CheckPerson.this,SignedActivity.class);
              startActivity(intent1);
              break;
          case 2:
              DialogFor();
              break;
          case 3:
              Intent intent3 = new Intent(CheckPerson.this,ChoosePerson.class);
              startActivityForResult(intent3,222);
              break;
          case 4:
              Intent intent4 = new Intent(CheckPerson.this, StartActivity.class);
              intent4.putExtra("title",title);
              intent4.putExtra("address",address);
              startActivity(intent4);
            //  CheckPerson.this.finish();
          //    finish();
              break;
      }

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(CheckPerson.this,LaunchActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==222&&resultCode==111){
            String name =data.getStringExtra("name");
            num++;
            person[num]=name;
            Bitmap addPerson = BitmapFactory.decodeResource(getResources(),R.mipmap.check_add);
            Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);
            itemList.remove(item4);
            item4 = new ItemBean("检查人",myName+" "+name,startingtime,addPerson);
            myName = myName+" "+name;
            itemList.add(item4);
            adapter.notifyDataSetChanged();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        myApplication = (MyApplication) getApplication();
        CheckIsEx(myApplication.getProjectName(),myApplication.getAddress(),datenow,myName);
    }
    /**
     * 发起检查，创建执法记录
     */
    public  void CheckRequest(final String checkProject, final String address,final String date,final String[] person) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/CheckStart";
        String tag = "Check";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                                //做自己的登录成功操作，如页面跳转
                                Log.d("上传成功","发起检查页面");
                            }
                            else {
                                //做自己的登录失败操作，如Toast提示
                                Log.d("上传失败","发起检查页面");
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
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
                params.put("checkProject",checkProject);
                params.put("address",address);
                params.put("checkTime",date);
                if (num==0){
                    params.put("rummager1",person[0]);

                }
                else {
                    for (int i =0;i<num+1;i++){
                        String dx = String.valueOf((i+1));
                        params.put("rummager"+dx,person[i]);
                    }
                }

                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    /**
     * 判断是否存在已经发起的检查
     */
    public  void CheckIsEx(final String checkProject, final String address, final String date, final String name) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/CheckJudge";
        String tag = "Check";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                            if (result.equals("check incompleted")) {  //如果结果返回为成功
                                //做自己的登录成功操作，如页面跳转
                                LinearLayoutManager layoutManager = new LinearLayoutManager(CheckPerson.this);
                                recvv_checkPerson.setLayoutManager(layoutManager);
                                adapter = new ItemAdapter(itemList);
                                recvv_checkPerson.setAdapter(adapter);
                                adapter.setItemClickListener(CheckPerson.this);
                                Update();
                                Bitmap checker = BitmapFactory.decodeResource(getResources(),R.mipmap.checker);
                                Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.right_arrow);
                                ItemBean itemBean5 = new ItemBean("当前项目","进入",checker,rightArrow);
                                itemList.add(itemBean5);
                                adapter.notifyDataSetChanged();
                            }
                            else {


                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
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
                params.put("checkProject",checkProject);
                params.put("address",address);
                params.put("checkTime",date);
                params.put("rummager",name);

                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }
}

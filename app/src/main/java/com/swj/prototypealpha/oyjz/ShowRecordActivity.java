package com.swj.prototypealpha.oyjz;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 查看录音界面
 * 录音列表
 */
public class ShowRecordActivity extends AppCompatActivity {
    private List<Record> recordList = new ArrayList<>();
    private MediaPlayer mPlayer = null;
    private boolean isPlaying = false;  //播放的时候为false,暂停的时候为true
    private Toolbar toolbar;
    private RecordAdapter adapter;
    private int num=0;
    private File[] files = null;
    private String ProjectName,Address;
    MyApplication myApplication;
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
        myApplication = (MyApplication) getApplication();
        ProjectName = myApplication.getProjectName();
        Address = myApplication.getAddress();
        adapter = new RecordAdapter(ShowRecordActivity.this,R.layout.record_item,recordList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Record record = recordList.get(i);
                    //暂停的时候
                    if(!isPlaying){
                    mPlayer = new MediaPlayer();
                        try {
                            File file = new File(record.getPath());
                            FileInputStream fis = new FileInputStream(file);
                            mPlayer.setDataSource(fis.getFD());
                            mPlayer.prepare();
                            mPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    isPlaying = true;
                }
                //播放的时候
                else{
                    mPlayer.release();      //结束播放，释放资源就行
                    mPlayer = null;
                    isPlaying = false;
                }
            }
        });
        //制定长按菜单界面
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("确认删除？");
                contextMenu.add(0,0,0,"取消");
                contextMenu.add(0,1,0,"确认");
            }
        });
    }

    @Override
    public  boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = (int)info.id;//这个id用于显示上下文子视图的ID
        switch(item.getItemId()){
            case 0:
                return true;
            case 1:
                myApplication = (MyApplication) getApplication();
                Record save = recordList.get(id);//在recordList中通过id获得对应record对象
                Log.d("名称为",save.getName());
                StringTokenizer str = new StringTokenizer(save.getName()," ");
                String date = str.nextToken();
                String time1 = (str.nextToken());
                String time = time1.substring(0,time1.lastIndexOf("."));
                File file = new File(save.getPath());
                file.delete();//删除对应文件
                recordList.remove(save);
                deleteVoiceRequest(myApplication.getProjectName(),myApplication.getAddress(),date,time);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * 加载录音
     * 并且按照时间从最近向下排序
     */
    private void initRecord(){
        myApplication = (MyApplication) getApplication();
        File dir = new File(myApplication.getFile(),myApplication.getProjectName()+"voice");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        File dir1 = new File(dir,datenow);
        files = dir1.listFiles();
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
                mPlayer.reset();
                mPlayer.release();
                isPlaying = false;
                mPlayer = null;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 删除录音
     */
    public  void deleteVoiceRequest(final String ProjectName,final String address, final String date,final String time) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/RecordDelete";
        String tag = "deleteVoice";

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

                            }

                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ShowRecordActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
                params.put("projectName", ProjectName);  //注⑥
                params.put("date", date);
                params.put("time",time);
                params.put("address",address);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}

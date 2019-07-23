package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发起检查中
 * 项目信息
 * 项目名称、地址、建设单位、开工时间
 * 监理单位、当前进度、施工单位、检查记录
 */
public class ProjFragment extends Fragment {
    private RecyclerView recvv_proinfo;

    private ItemAdapter adapter;

    private List<ItemBean> itemList = new ArrayList<>();

    private void initUI()
    {
        recvv_proinfo = getActivity().findViewById(R.id.recv_start_pro_info);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();

        String ProjectName = (String) getArguments().get("ProjectName");
        String Address = (String) getArguments().get("Address");
    //    ProjectRequest(ProjectName,Address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recvv_proinfo.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(itemList);
        recvv_proinfo.setAdapter(adapter);
        recvv_proinfo.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        Update();

        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 7)
                {
                    Intent intent =new Intent(getActivity(),HistoryAccordActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });

    }

    private void Update()
    {
        itemList.clear();
        Bitmap proj_name = BitmapFactory.decodeResource(getResources(),R.mipmap.project_name);
        Bitmap proj_addr = BitmapFactory.decodeResource(getResources(),R.mipmap.proj_addr);
        Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);
        Bitmap builder= BitmapFactory.decodeResource(getResources(),R.mipmap.builder);
        Bitmap schedule= BitmapFactory.decodeResource(getResources(),R.mipmap.schedule);
        Bitmap checknote= BitmapFactory.decodeResource(getResources(),R.mipmap.check_note);
        Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.right_arrow);
        Bitmap white      = BitmapFactory.decodeResource(getResources(),R.drawable.border);
        ItemBean item0 = new ItemBean("项目名称","橘子洲大桥提质改造工程",proj_name, white  );
        ItemBean item1 = new ItemBean("项目地址","岳麓区",proj_addr, white  );
        ItemBean item3 = new ItemBean("开工时间","2018.09",startingtime, white  );
        ItemBean item2 = new ItemBean("建设单位","长沙市工务局",builder, white  );
        ItemBean item6 = new ItemBean("施工单位","湖南省绿林市政景观工程有限公司",builder, white  );
        ItemBean item4 = new ItemBean("监理单位","城规监理",builder, white  );
        ItemBean item5 = new ItemBean("当前进度","已完成20个站点土建工程，7个站点钢结构施工",schedule, white  );
        ItemBean item7 = new ItemBean("检查记录","2月18日，执法服务",checknote,rightArrow);

        itemList.add(item0);
        adapter.notifyItemChanged(0);
        itemList.add(item1);
        adapter.notifyItemChanged(1);
        itemList.add(item2);
        adapter.notifyItemChanged(2);
        itemList.add(item3);
        adapter.notifyItemChanged(3);
        itemList.add(item4);
        adapter.notifyItemChanged(4);
        itemList.add(item5);
        adapter.notifyItemChanged(5);
        itemList.add(item6);
        adapter.notifyItemChanged(6);
        itemList.add(item7);
        adapter.notifyItemChanged(7);
        adapter.notifyItemChanged(0,itemList.size());
    }
    /**
     * 拉取项目详细信息
     */
    public void ProjectRequest(final String name, final String address) {
        //请求地址
        String url = "http://257v7842r5.wicp.vip/mobile_inspection_war/ProjectDetail ";    //注①
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
                            itemList.clear();
                            Log.i("返回参数",response);
                            JSONObject jsonObject =  new JSONObject(response);  //获取参数
                            Log.i("返回参数", String.valueOf(jsonObject));
                            String ProjectName =jsonObject.getString("projectName");
                            String Address = jsonObject.getString("address");
                            String UnitConstruction = jsonObject.getString("unitConstruction");
                            String StartTime = jsonObject.getString("startTime");
                            String SupervisionUnion =jsonObject.getString("supervisionUnion");
                            String CurrentProgress = jsonObject.getString("currentProgress");
                            String Contractors =jsonObject.getString("contractors");

                            Bitmap proj_name = BitmapFactory.decodeResource(getResources(),R.mipmap.project_name);
                            Bitmap proj_addr = BitmapFactory.decodeResource(getResources(),R.mipmap.proj_addr);
                            Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);
                            Bitmap builder= BitmapFactory.decodeResource(getResources(),R.mipmap.builder);
                            Bitmap schedule= BitmapFactory.decodeResource(getResources(),R.mipmap.schedule);
                            Bitmap checknote= BitmapFactory.decodeResource(getResources(),R.mipmap.check_note);
                            Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.right_arrow);
                            Bitmap white  = BitmapFactory.decodeResource(getResources(),R.drawable.border);
                            ItemBean item0 = new ItemBean("项目名称",ProjectName,proj_name,white );
                            ItemBean item1 = new ItemBean("项目地址",Address,proj_addr,white );
                            ItemBean item3 = new ItemBean("开工时间",StartTime,startingtime,white );
                            ItemBean item2 = new ItemBean("建设单位",UnitConstruction,builder,white );
                            ItemBean item6 = new ItemBean("施工单位",Contractors,builder,white );
                            ItemBean item4 = new ItemBean("监理单位",SupervisionUnion,builder,white );
                            ItemBean item5 = new ItemBean("当前进度",CurrentProgress,schedule,white );
                            ItemBean item7 = new ItemBean("检查记录","2月18日，执法服务",checknote,rightArrow);

                            itemList.add(item0);
                            itemList.add(item1);
                            itemList.add(item2);
                            itemList.add(item3);
                            itemList.add(item4);
                            itemList.add(item5);
                            itemList.add(item6);
                            itemList.add(item7);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            recvv_proinfo.setLayoutManager(layoutManager);
                            adapter = new ItemAdapter(itemList);
                            recvv_proinfo.setAdapter(adapter);
                            recvv_proinfo.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                            adapter.notifyDataSetChanged();
                            adapter.setItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (position == 7)
                                    {
                                        Intent intent =new Intent(getActivity(),HistoryAccordActivity.class);
                                        startActivity(intent);

                                    }
                                }

                                @Override
                                public void onDeleteClick(int position) {

                                }
                            });


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proj, container, false);
    }


}

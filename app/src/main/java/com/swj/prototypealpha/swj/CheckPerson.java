package com.swj.prototypealpha.swj;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.ChooseCheckPerson;
import com.swj.prototypealpha.activity.ChoosePerson;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;
import com.swj.prototypealpha.swj.util.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 发起检查跳转界面
 */
public class CheckPerson extends AppCompatActivity implements OnItemClickListener
{

    private Toolbar tlb_checkPerson;

    private RecyclerView recvv_checkPerson;

    private ItemAdapter adapter;

    private List<ItemBean> itemList = new ArrayList<>();

    private List<String> personList = new ArrayList<>();

    private FloatingActionButton btn_addPerson;

    private  ItemBean item4;

 //   private boolean flag=false;


    private void initUI()
    {
        recvv_checkPerson = findViewById(R.id.recv_addperson);
        tlb_checkPerson = findViewById(R.id.tlb_checklocperson);
        btn_addPerson = findViewById(R.id.lfbtn_add_person);


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_person);
        Intent intent =getIntent();
        String title = intent.getStringExtra("title");
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
        btn_addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckPerson.this,StartActivity.class);
                startActivity(intent);
            }
        });
  /*      Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");
        Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);
        Bitmap addPerson = BitmapFactory.decodeResource(getResources(),R.mipmap.check_add);
        ItemBean item5 = new ItemBean("检查人",name,startingtime,addPerson);
        itemList.add(item5);
        adapter.notifyItemChanged(4);
 */      // itemList.add()
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

        ItemBean item0 = new ItemBean("详细信息","橘子洲大桥提质改造工程",proj_name,rightArrow);
        ItemBean item1 = new ItemBean("签到","开始",proj_addr,rightArrow);
        ItemBean item3 = new ItemBean("发起检查","确定",checker,rightArrow);


        itemList.add(item0);
        adapter.notifyItemChanged(0);
        itemList.add(item1);
        adapter.notifyItemChanged(1);
        itemList.add(item3);
        adapter.notifyItemChanged(2);

        item4 = new ItemBean("检查人","张三",startingtime,addPerson);
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
              Intent intent1 = new Intent(CheckPerson.this,AddLocActivity.class);
              startActivity(intent1);
              break;
          case 2:
              Intent intent2 = new Intent(CheckPerson.this,StartActivity.class);
              startActivity(intent2);
              break;
          case 3:
              Intent intent3 = new Intent(CheckPerson.this,ChoosePerson.class);
              startActivityForResult(intent3,222);
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
       //     Log.d(name,"大大实打实大苏打");
            Bitmap addPerson = BitmapFactory.decodeResource(getResources(),R.mipmap.check_add);
            Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);
            itemList.remove(item4);
            item4 = new ItemBean("检查人","张三 "+name,startingtime,addPerson);
            itemList.add(item4);
            adapter.notifyDataSetChanged();

        }
    }
}

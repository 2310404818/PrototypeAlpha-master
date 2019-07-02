package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;
import com.swj.prototypealpha.swj.util.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CheckPerson extends AppCompatActivity implements OnItemClickListener
{

    private Toolbar tlb_checkPerson;

    private RecyclerView recvv_checkPerson;

    private ItemAdapter adapter;

    private List<ItemBean> itemList = new ArrayList<>();

    private List<String> personList = new ArrayList<>();

    private FloatingActionButton btn_addPerson;

    private Spinner spi_add_person;


    private void initUI()
    {
        recvv_checkPerson = findViewById(R.id.recv_addperson);
        tlb_checkPerson = findViewById(R.id.tlb_checklocperson);
        btn_addPerson = findViewById(R.id.lfbtn_add_person);
        spi_add_person = findViewById(R.id.spi_add_people);
        setSupportActionBar(tlb_checkPerson);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_person);

        initUI();

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
        personList.add("请选择检查人员");
        personList.add("李四");
        personList.add("王五");
        personList.add("马六");
        SpinnerAdapter spi_adapter = new SpinnerAdapter(this,personList);
        spi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_add_person.setAdapter(spi_adapter);

        spi_add_person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bitmap checker = BitmapFactory.decodeResource(getResources(),R.mipmap.checker);
                Bitmap addPerson = BitmapFactory.decodeResource(getResources(),R.mipmap.check_add);

                String name = "请选择检查人员";
                if(position == 1)   name = "李四";
                else if(position == 2) name = "王五";
                else if(position == 3) name = "马六";

                if(!name.equals("请选择检查人员"))
                {
                    ItemBean item0 = new ItemBean("检查人",name,checker,addPerson);
                    itemList.add(item0);
                    adapter.notifyItemChanged(0);
                    adapter.notifyItemChanged(0,itemList.size());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Update()
    {
        itemList.clear();
        Bitmap proj_name = BitmapFactory.decodeResource(getResources(),R.mipmap.project_name);
        Bitmap proj_addr = BitmapFactory.decodeResource(getResources(),R.mipmap.proj_addr);
        Bitmap startingtime = BitmapFactory.decodeResource(getResources(),R.mipmap.startingtime);
        Bitmap checker = BitmapFactory.decodeResource(getResources(),R.mipmap.checker);


        Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.right_arrow);
        Bitmap addPerson = BitmapFactory.decodeResource(getResources(),R.mipmap.check_add);
        ItemBean item0 = new ItemBean("项目名称","橘子洲大桥提质改造工程",proj_name,rightArrow);
        ItemBean item1 = new ItemBean("项目地址","岳麓区",proj_addr,rightArrow);
        ItemBean item3 = new ItemBean("开工时间","2018.09",startingtime,rightArrow);
        ItemBean item4 = new ItemBean("检查人","张三",checker,addPerson);

        itemList.add(item0);
        adapter.notifyItemChanged(0);
        itemList.add(item1);
        adapter.notifyItemChanged(1);
        itemList.add(item3);
        adapter.notifyItemChanged(2);
        itemList.add(item4);
        adapter.notifyItemChanged(3);

        adapter.notifyItemChanged(0,itemList.size());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(CheckPerson.this,AddLocActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

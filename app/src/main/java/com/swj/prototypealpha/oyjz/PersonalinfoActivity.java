package com.swj.prototypealpha.oyjz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.MainActivity;

/**
 * 个人中心界面
 * 手机号
 * 名称
 * 年龄
 * 性别
 * 身份证
 * 工号
 */
public class PersonalinfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText person_name,person_age,person_sex;
    private TextView person_phone,person_id,person_job,mybutton;
    MyApplication myApplication;
    public void setToolBar()
    {
        toolbar = findViewById(R.id.text_ps_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public void initUI(){
        myApplication= (MyApplication) getApplication();
        person_age = findViewById(R.id.person_age);
        person_id = findViewById(R.id.person_id);
        person_job = findViewById(R.id.person_job);
        person_name = findViewById(R.id.person_name);
        person_phone =findViewById(R.id.person_phone);
        person_sex = findViewById(R.id.person_sex);

        person_sex.setText(myApplication.getSex());
        person_age.setText(myApplication.getAge());
        person_phone.setText(myApplication.getTell());
        person_name.setText(myApplication.getName());
        person_job.setText(myApplication.getWorkNumber());
        person_id.setText(myApplication.getIdcard());
    }

    /**
     * 点击返回按钮弹出框
     * 提示信息未保存，是否离开
     *
     */
    public void Dialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("你修改的信息还未保存，是否离开？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
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
        setContentView(R.layout.activity_personalinfo);
        setToolBar();
        initUI();
        mybutton = findViewById(R.id.mybutton);
        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalinfoActivity.this,MainActivity.class);
                Toast.makeText(PersonalinfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            Dialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

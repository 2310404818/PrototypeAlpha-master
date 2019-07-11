package com.swj.prototypealpha.oyjz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.LoginActivity;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_get_code;
    private Button btn_register;
    private Toolbar toolbar;
    /**  倒计时 */
    private RegisterActivity.TimeCount m_TimeCount;
    public void setToolBar()
    {
        toolbar = findViewById(R.id.text_re_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void initUI()
    {
        btn_get_code = findViewById(R.id.btn_getcode);
        btn_register = findViewById(R.id.btn_register);
        setToolBar();
    }

    private void setOnclickListener()
    {
        btn_get_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        m_TimeCount = new TimeCount(60000, 1000);
        setOnclickListener();
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.btn_getcode:
                m_TimeCount.start();
                break;
            case R.id.btn_register:
                intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**     * 计时器     */
    class TimeCount extends CountDownTimer
    {
        Drawable drawable = btn_get_code.getBackground();
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }         @Override
    public void onTick(long l)
    {
        btn_get_code.setClickable(false);
        btn_get_code.setText(l/1000 + "秒后重新获取");
        btn_get_code.setBackgroundColor(Color.parseColor("#A1A1A1"));
    }         @Override
    public void onFinish()
    {
        btn_get_code.setClickable(true);
        btn_get_code.setBackgroundDrawable(drawable);
        btn_get_code.setText("获取验证码");

    }
    }
    /**
     * 返回图标响应
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  //应用程序图标的id
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


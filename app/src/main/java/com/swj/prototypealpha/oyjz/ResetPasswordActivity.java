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

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_set_getcode;
    private Button btn_reset;
    private Toolbar toolbar;

    /**  倒计时 */
    private ResetPasswordActivity.TimeCount m_TimeCount;

    public void setToolBar()
    {
        toolbar = findViewById(R.id.text_set_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initUI()
    {
        btn_set_getcode = findViewById(R.id.btn_set_getcode);
        btn_reset = findViewById(R.id.btn_reset);
        setToolBar();
    }

    private void setOnclickListener()
    {
        btn_set_getcode.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initUI();
        m_TimeCount = new TimeCount(60000, 1000);
        setOnclickListener();
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.btn_set_getcode:
                m_TimeCount.start();
                break;
            case R.id.btn_reset:
                intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                ResetPasswordActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**     * 计时器     */
    class TimeCount extends CountDownTimer
    {
        Drawable drawable = btn_set_getcode.getBackground();
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }         @Override
    public void onTick(long l)
    {
        btn_set_getcode.setClickable(false);
        btn_set_getcode.setText(l/1000 + "秒后重新获取");
        btn_set_getcode.setBackgroundColor(Color.parseColor("#A1A1A1"));
    }         @Override
    public void onFinish()
    {
        btn_set_getcode.setClickable(true);
        btn_set_getcode.setBackgroundDrawable(drawable);
        btn_set_getcode.setText("获取验证码");

    }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  //应用程序图标的id
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

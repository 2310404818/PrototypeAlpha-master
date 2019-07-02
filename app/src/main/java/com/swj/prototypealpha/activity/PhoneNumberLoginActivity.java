package com.swj.prototypealpha.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.swj.prototypealpha.R;

public class PhoneNumberLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_login;

    private TextView text_phone_login;

    private Button btn_getIdcod;

    /**  倒计时 */
    private  TimeCount m_TimeCount;


    private void initUI()
    {
        btn_login = findViewById(R.id.btn_login);
        text_phone_login = findViewById(R.id.phonelogin);
        btn_getIdcod = findViewById(R.id.get_identify_code);
    }

    private void setOnclickListener()
    {
        btn_login.setOnClickListener(this);
        text_phone_login.setOnClickListener(this);
        btn_getIdcod.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_login);

        initUI();
        m_TimeCount = new TimeCount(60000, 1000);
        setOnclickListener();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_login:
                intent = new Intent(PhoneNumberLoginActivity.this, MainActivity.class);
                startActivity(intent);
                PhoneNumberLoginActivity.this.finish();
                break;
            case R.id.phonelogin:
                intent = new Intent(PhoneNumberLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                PhoneNumberLoginActivity.this.finish();
                break;
            case R.id.get_identify_code:
                m_TimeCount.start();
            default:
                break;
        }

    }

    /**     * 计时器     */
    class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }         @Override
    public void onTick(long l)
    {
        btn_getIdcod.setClickable(false);
        btn_getIdcod.setText(l/1000 + "秒后重新获取");
    }         @Override
    public void onFinish()
    {
        btn_getIdcod.setClickable(true);
        btn_getIdcod.setText("获取验证码");
    }
    }
}

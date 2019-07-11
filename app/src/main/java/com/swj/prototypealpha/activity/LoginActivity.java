package com.swj.prototypealpha.activity;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.swj.prototypealpha.R;
        import com.swj.prototypealpha.oyjz.RegisterActivity;
        import com.swj.prototypealpha.oyjz.ResetPasswordActivity;

/**
 *
 * 登陆界面
 * 直接登陆
 * 手机号验证码登陆
 * 注册
 * 忘记密码
 *
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button btn_login;

    private TextView text_phone_login;
    private TextView goto_register;
    private TextView text_forgetPassword;

    private void initUI()
    {
        btn_login = findViewById(R.id.btn_login);
        text_phone_login = findViewById(R.id.accountlogin);
        goto_register = findViewById(R.id.gotoregister);
        text_forgetPassword = findViewById(R.id.text_forgetPassword);
    }

    private void setOnclickListener()
    {
        btn_login.setOnClickListener(this);
        text_phone_login.setOnClickListener(this);
        goto_register.setOnClickListener(this);
        text_forgetPassword.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setOnclickListener();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_login:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
            case R.id.accountlogin:
                intent = new Intent(LoginActivity.this, PhoneNumberLoginActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
            case R.id.gotoregister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.text_forgetPassword:
                intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

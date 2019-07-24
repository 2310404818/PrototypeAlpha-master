package com.swj.prototypealpha.oyjz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 重置密码界面
 * 手机号
 * 验证码
 * 新密码
 * 重复密码
 * 获取验证码倒计时
 */
public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_set_getcode;
    private Button btn_reset;
    private Toolbar toolbar;
    private TextView phone_reset;
    private TextView phonecode_reset;
    private TextView password;
    private TextView password_reset;

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
        phone_reset = findViewById(R.id.phone_reset);
        phonecode_reset = findViewById(R.id.phonecode_reset);
        password_reset = findViewById(R.id.password_reset);
        password = findViewById(R.id.newpassword);
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

    /**
     * 判断手机号正确
     * @param phoneNumber
     * @return
     */
    public boolean isPhone(String phoneNumber){
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(phoneNumber))
        {
            Toast.makeText(ResetPasswordActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if (!phoneNumber.matches(telRegex))
            {
                Toast.makeText(ResetPasswordActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }

    /**
     * 判断密码设置一致且符合格式
     * @param password
     * @param resetpassword
     * @return
     */
    public boolean isCorrect(String password,String resetpassword,String code){
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(resetpassword)){
            Toast.makeText(ResetPasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(code)){
            Toast.makeText(ResetPasswordActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (!password.equals(resetpassword)){
                Toast.makeText(ResetPasswordActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                if (!password.matches(passRegex))
                {
                    Toast.makeText(ResetPasswordActivity.this,"密码需6-16字母和数字格式",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        }
        return true;

    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.btn_set_getcode:
                String phoneNumber = phone_reset.getText().toString();
                boolean phoneFlag1 = isPhone(phoneNumber);
                if (phoneFlag1){
                    sendSMS(phoneNumber);
                    m_TimeCount.start();

                }

                break;
            case R.id.btn_reset:
                String phoneNumber1 = phone_reset.getText().toString();
                String newpassword = password.getText().toString();
                String password_rese = password_reset.getText().toString();
                String code = phonecode_reset.getText().toString();
                boolean phoneFlag = isPhone(phoneNumber1);
                boolean passFlag = isCorrect(newpassword,password_rese,code);
                if (passFlag && phoneFlag)
                {
                    ResetRequest(phoneNumber1,newpassword,code);
                }
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
    /**
     * 发送验证码
     * 检验验证码
     */
    public void sendSMS(final String phoneNumber){
        String url = "http://47.102.119.140:8080/mobile_inspection_war/SendSmsServlet";
        String tag = "SendSms";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.cancelAll(tag);
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                    String result = jsonObject.getString("Result");
                    if (result.equals("success")){
                        Log.d("11111111111111111111111111111111111111","1111111111111111111111111111111");

                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("tell", phoneNumber);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    /**
     * 连接服务器
     * 重置密码
     */
    public  void ResetRequest(final String accountNumber, final String password,final String code) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/ResetPassword";    //注①
        String tag = "ResetPassword";    //注②

        //取得请求队列
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                                //注册成功
                                Toast.makeText(ResetPasswordActivity.this,"重置密码成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                                ResetPasswordActivity.this.finish();
                            }
                            else if (result.equals("该号码未注册")){
                                Toast.makeText(ResetPasswordActivity.this,"该号码未注册",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("验证码错误")){
                                Toast.makeText(ResetPasswordActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("验证码已过期")){
                                Toast.makeText(ResetPasswordActivity.this,"验证码已过期，请重新发送",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("新密码和旧密码重复")){
                                Toast.makeText(ResetPasswordActivity.this,"新密码和旧密码重复，请输入正确密码",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //做自己的登录失败操作，如Toast提示
                                Toast.makeText(getApplicationContext(),"账号不存在",Toast.LENGTH_SHORT).show();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tell", accountNumber);  //注⑥
                params.put("newPassword", password);
                params.put("VCode",code);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}

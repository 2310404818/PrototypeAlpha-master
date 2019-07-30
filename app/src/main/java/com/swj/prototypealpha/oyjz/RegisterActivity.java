package com.swj.prototypealpha.oyjz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swj.prototypealpha.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册界面
 * 完成相关信息填写和短信验证码
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_get_code;
    private Button btn_register;
    private Toolbar toolbar;
    private TextInputEditText myphone,mycode,mypassword;
    /**  倒计时 */
    private RegisterActivity.TimeCount m_TimeCount;

    /**
     * 设置toobar和初始化各自界面
     */
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
        mycode =findViewById(R.id.mycode);
        mypassword =findViewById(R.id.mypassword);
        myphone =findViewById(R.id.myphone);
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

                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"发送失败,请检查手机号",Toast.LENGTH_SHORT).show();
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
     * 创建成功以后
     * 账号信息发送给服务器
     */
    public  void RegisterRequest(final String accountNumber, final String password,
                                final String code) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/Register";    //注①
        String tag = "Register";    //注②

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
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else if (result.equals("手机号错误")){
                                Toast.makeText(RegisterActivity.this,"手机号与发送验证码手机号不一致",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("验证码已过期")){
                                Toast.makeText(RegisterActivity.this,"验证码已过期",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("registered!")){
                                Toast.makeText(RegisterActivity.this,"该号码已注册",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("验证码错误")){
                                Toast.makeText(RegisterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //做自己的登录失败操作，如Toast提示
                                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
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
                params.put("Password", password);
                params.put("VCode",code);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }
    /**
     * 判断各个参数是否符合标准
     */
    public boolean isCorrect(){



        return true;
    }
    /**
     * 判断手机号正确
     * 判断密码格式正确
     */
    public boolean isPhone(String phoneNumber){
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(phoneNumber))
        {
            Toast.makeText(RegisterActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if (!phoneNumber.matches(telRegex))
            {
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }
    public boolean isCorrect(String password){
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (TextUtils.isEmpty(password) ){
            Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
                if (!password.matches(passRegex))
                {
                    Toast.makeText(RegisterActivity.this,"密码需6-16字母和数字格式",Toast.LENGTH_SHORT).show();
                    return false;
                }
        }
        return true;

    }

    /**
     * 各自点击事件
     * 发送验证码
     * 注册
     * @param view
     */
    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.btn_getcode:
                String phone1 = myphone.getText().toString();
                boolean phoneflag1 =   isPhone(phone1);
                if (phoneflag1){
                    sendSMS(phone1);
                    m_TimeCount.start();
                }
                break;
            case R.id.btn_register:

                String phone = myphone.getText().toString();
                String code = mycode.getText().toString();
                String password =mypassword.getText().toString();

                 boolean correctflag =isCorrect(password);
                 boolean phoneflag =   isPhone(phone);
                 if (TextUtils.isEmpty(code)){
                     Toast.makeText(RegisterActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     if (correctflag && phoneflag){
                         RegisterRequest(phone,password,code);
                     }
                 }

                break;
            default:
                break;
        }
    }

    /**
     * 发送验证码
     * 计时器
     * 点击以后显示59秒以后重新获取
     **/
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


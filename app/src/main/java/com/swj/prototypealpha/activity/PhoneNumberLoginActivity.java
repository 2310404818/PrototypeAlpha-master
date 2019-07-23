package com.swj.prototypealpha.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.swj.prototypealpha.oyjz.RegisterActivity;
import com.swj.prototypealpha.oyjz.ResetPasswordActivity;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号登陆界面
 * 手机号
 * 验证码
 *
 */
public class PhoneNumberLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_login;

    private TextView text_phone_login;
    private TextInputEditText phoneNumber,phoneCode;
    private Button btn_getIdcod;
    private String phone;
    private TextView text_forgetPassword,ext_register;
    private int remamberFlag;
    private CheckBox checkBox2_phone;
    /**  倒计时 */
    private  TimeCount m_TimeCount;


    private void initUI()
    {
        phoneCode = findViewById(R.id.phoneCode);
        phoneNumber = findViewById(R.id.phoneNumber);
        btn_login = findViewById(R.id.btn_login);
        text_phone_login = findViewById(R.id.phonelogin);
        btn_getIdcod = findViewById(R.id.get_identify_code);
        text_forgetPassword =findViewById(R.id.text_forgetPassword);
        ext_register =findViewById(R.id.text_register);
        checkBox2_phone = findViewById(R.id.checkBox2_phone);
    }

    private void setOnclickListener()
    {
        btn_login.setOnClickListener(this);
        text_phone_login.setOnClickListener(this);
        btn_getIdcod.setOnClickListener(this);
        text_forgetPassword.setOnClickListener(this);
        ext_register.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_login);
        initUI();
        m_TimeCount = new TimeCount(60000, 1000);
        setOnclickListener();
        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        //如果不为空
        if (sharedPreferences != null) {
            phone = sharedPreferences.getString("phone", "");
            remamberFlag = sharedPreferences.getInt("remeber_flag", 0);
            if (remamberFlag == 1){
                checkBox2_phone.setChecked(true);
                phoneNumber.setText(phone);
            }
        }

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
            Toast.makeText(PhoneNumberLoginActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if (!phoneNumber.matches(telRegex))
            {
                Toast.makeText(PhoneNumberLoginActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }

    /**
     * 发送验证码
     * 检验验证码
     */
    public void sendSMS(final String phoneNumber){
        String url = "http://257v7842r5.wicp.vip/mobile_inspection_war/SendSmsServlet";
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
                        Toast.makeText(PhoneNumberLoginActivity.this,"发送失败,请检查手机号",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(PhoneNumberLoginActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
     * 登陆
     * @param accountNumber
     * @param code
     */
    public  void LoginPhoneRequest(final String accountNumber, final String code) {
        //请求地址
        String url = "http://257v7842r5.wicp.vip/mobile_inspection_war/LoginByVCode";    //注①
        String tag = "LoginPhone";    //注②

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
                                Intent intent = new Intent(PhoneNumberLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                PhoneNumberLoginActivity.this.finish();
                            }
                            else if (result.equals("用户名不存在" )){
                                Toast.makeText(PhoneNumberLoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("验证码错误")){
                                Toast.makeText(PhoneNumberLoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(PhoneNumberLoginActivity.this, "验证码已过期，请重试", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
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
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("VCode", code);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    /**
     * 进度条加载
     */
    public void showDiag(){
        CircleProgressDialog circleProgressDialog;
        circleProgressDialog = new CircleProgressDialog(this);
        circleProgressDialog.showDialog();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_login:
                String phoneNUmber = phoneNumber.getText().toString();
                SharedPreferences sharedPreferences =getSharedPreferences("test",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone",phoneNUmber);
                if (checkBox2_phone.isChecked()){
                    remamberFlag = 1;
                    editor.putInt("remeber_flag",remamberFlag);
                }
                else {
                    remamberFlag = 0;
                    editor.putInt("remeber_flag",remamberFlag);
                }
                editor.apply();
         //       intent = new Intent(PhoneNumberLoginActivity.this, MainActivity.class);
        //        startActivity(intent);
                String code = phoneCode.getText().toString();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(PhoneNumberLoginActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean phoneflag = isPhone(phoneNUmber);
                    if (phoneflag){
                            showDiag();
                            LoginPhoneRequest(phoneNUmber,code);
                    }
                }

              //  finish();


                break;
            case R.id.phonelogin:
                intent = new Intent(PhoneNumberLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                PhoneNumberLoginActivity.this.finish();
                break;
            case R.id.get_identify_code:
          //      String phone =
                String phoneNumber0 = phoneNumber.getText().toString();
                boolean phoneFlag = isPhone(phoneNumber0);
                if (phoneFlag){
                    sendSMS(phoneNumber0);
                    m_TimeCount.start();
                }
                break;
            case R.id.text_forgetPassword:
                intent = new Intent(PhoneNumberLoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.text_register:
                intent =new Intent(PhoneNumberLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
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

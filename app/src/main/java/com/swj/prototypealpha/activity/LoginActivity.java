package com.swj.prototypealpha.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.RegisterActivity;
import com.swj.prototypealpha.oyjz.ResetPasswordActivity;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private TextInputEditText accountNumber,password;
    private TextView text_phone_login;
    private TextView goto_register;
    private TextView text_forgetPassword;
    private CheckBox checkBox_zidong;
    private CheckBox checkBox_remember;
    private ProgressDialog pd =null;
    //判断记住密码和自动登陆选框是否选中
    private int remamberFlag,zidongFlag;
    private String pass,name;
    private  CircleProgressDialog circleProgressDialog;
    MyApplication myApplication;
    private void initUI()
    {
        accountNumber = findViewById(R.id.accountNumber);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        text_phone_login = findViewById(R.id.accountlogin);
        goto_register = findViewById(R.id.gotoregister);
        text_forgetPassword = findViewById(R.id.text_forgetPassword);
        checkBox_zidong = findViewById(R.id.checkBox);
        checkBox_remember = findViewById(R.id.checkBox2);
    }

    private void setOnclickListener()
    {

        btn_login.setOnClickListener(this);
        text_phone_login.setOnClickListener(this);
        goto_register.setOnClickListener(this);
        text_forgetPassword.setOnClickListener(this);
        checkBox_remember.setOnClickListener(this);
        checkBox_zidong.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initUI();
        Intent intent1 =getIntent();
        String log = "YES";
        String log1 =intent1.getStringExtra("log");
        if (log1!= null){
            log = log1;
        }
        //获得保存在sharedperference中的账号信息，实现登陆
        SharedPreferences sharedPreferences =getSharedPreferences("prototype",MODE_PRIVATE);
        if (sharedPreferences != null){
            name = sharedPreferences.getString("USER_NAME","");
            pass = sharedPreferences.getString("PASSWORD","");
            remamberFlag = sharedPreferences.getInt("remeber_flag",0);
            zidongFlag = sharedPreferences.getInt("zidongFlag",0);
            accountNumber.setText(name);
        }
        if (remamberFlag == 1){
            checkBox_remember.setChecked(true);
            password.setText(pass);
            if (zidongFlag == 1 && log.equals("YES")) {
                checkBox_zidong.setChecked(true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            //    LoginRequest(name,pass);
            }
        }


        setOnclickListener();
    }
    /**
     * 进度条加载
     */
    public void shouDiag(){

        circleProgressDialog = new CircleProgressDialog(this);
        circleProgressDialog.showDialog();
    }

    /**
     * 服务器连接，判断账号密码正确
     * @param accountNumber
     * @param password
     */
    public  void LoginRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://257v7842r5.wicp.vip/mobile_inspection_war/Login";
        String tag = "Login";

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
                              PersonInfoRequest(accountNumber);
                            }
                            else if (result.equals("用户名不存在")){
                                circleProgressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //做自己的登录失败操作，如Toast提示
                                circleProgressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            circleProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                        //    Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                circleProgressDialog.dismiss();
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    /**
     * 拉取所有的个人信息
     */
    /**
     * 拉取项目详细信息
     */
    public void PersonInfoRequest(final String tell) {
        //请求地址
        String url = "http://257v7842r5.wicp.vip/mobile_inspection_war/UserDetail";    //注①
        String tag = "PersonInfo";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            myApplication = (MyApplication) getApplication();
                            JSONObject personInfo =  new JSONObject(response);  //获取参数
                            Log.i("返回参数", String.valueOf(personInfo));
                            String PersonName =personInfo.getString("userName");
                            String PersonTell = personInfo.getString("tell");
                            String PersonPassword = personInfo.getString("password");
                            String PersonSex =personInfo.getString("gender");
                            String PersonAge =personInfo.getString("age");
                            String PersonWordNumber = personInfo.getString("workNumber");
                            String PersonIdCard =personInfo.getString("id");
                            myApplication.PersonInfo(PersonTell,PersonName,PersonPassword,PersonIdCard,PersonWordNumber,PersonSex,PersonAge);

                                circleProgressDialog.dismiss();
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent1);
                                LoginActivity.this.finish();

                        } catch (JSONException e) {
                            circleProgressDialog.dismiss();
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
                            Log.d("网络异常","大家都叫睡大觉");
                            //    Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.d("基地啊涉及到激动静安寺大家哦i的骄傲的","嗲话降低哦家的骄傲的静安寺哦");
                //   Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tell", tell);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
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
            Toast.makeText(LoginActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if (!phoneNumber.matches(telRegex))
            {
                Toast.makeText(LoginActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }
    public boolean isCorrect(String password){
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (TextUtils.isEmpty(password) ){
            Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (!password.matches(passRegex))
            {
                Toast.makeText(LoginActivity.this,"密码需6-16字母和数字格式",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_login:
                String user_name = accountNumber.getText().toString();
                String pass_this = password.getText().toString();
                SharedPreferences sharedPreferences =getSharedPreferences("prototype",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_NAME",user_name);
                if (checkBox_remember.isChecked()){
                    remamberFlag = 1;
                    editor.putInt("remeber_flag",remamberFlag);
                    editor.putString("PASSWORD",pass_this);
                }
                else {
                    remamberFlag = 0;
                    editor.putInt("remeber_flag",remamberFlag);
                }
                if (checkBox_zidong.isChecked()){
                    zidongFlag = 1;
                    editor.putInt("zidongFlag",zidongFlag);
                }
                else {
                    zidongFlag = 0;
                    editor.putInt("zidongFlag",zidongFlag);
                }
                editor.apply();
                shouDiag();
                LoginRequest(user_name,pass_this);




                /*
                circleProgressDialog.dismiss();
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                */
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

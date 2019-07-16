package com.swj.prototypealpha.activity;

        import android.content.Intent;
        import android.support.design.widget.TextInputEditText;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
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
        import com.swj.prototypealpha.oyjz.RegisterActivity;
        import com.swj.prototypealpha.oyjz.ResetPasswordActivity;
        import com.swj.prototypealpha.swj.LaunchActivity;

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

    private void initUI()
    {
        accountNumber = findViewById(R.id.accountNumber);
        password = findViewById(R.id.password);
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
  //      testStringRequest();
        setOnclickListener();
    }
    public  void LoginRequest(final String accountNumber, final String password) {
        //请求地址

        String url = "http://257v7842r5.wicp.vip:8080/MyFirstWebApp/LoginServlet";    //注①
        String tag = "Login";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
         StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //获取参数
                            String result = jsonObject.getString("Result");  //获取请求结果
                            if (result.equals("success")) {  //如果结果返回为成功
                                //做自己的登录成功操作，如页面跳转
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
                                //做自己的登录失败操作，如Toast提示
                                Toast.makeText(getApplicationContext(),"登陆失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
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
                params.put("Password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_login:
                String strForNumber = accountNumber.getText().toString();
                String strForPasswoed = password.getText().toString();
                LoginRequest(strForNumber,strForPasswoed);
          //       intent = new Intent(LoginActivity.this, MainActivity.class);
         //       startActivity(intent);
        //        LoginActivity.this.finish();
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
/*
    private void testStringRequest(){
        String url = "http://api.k780.com/?app=weather.history&weaid=1&date=2015-07-20&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response:", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        }

        );
        queue.add(stringRequest);
    }*/
}

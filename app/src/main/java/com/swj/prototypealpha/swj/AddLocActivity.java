package com.swj.prototypealpha.swj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.swj.prototypealpha.swj.util.AlterDialogUtil.setAlterDialog;

public class AddLocActivity extends AppCompatActivity implements OnItemClickListener
{
    private String TAG = "AddLocActivity";

    private WebView webView;
    private double lat;

    private double lon;


//    String mUrl = "https://apis.map.qq.com/tools/locpicker?search=1&policy=0&coordtype=4&radius=200&type=0&backurl=https://callback&key=QULBZ-6M6KO-5YZWR-SEYTJ-GNNS5-O6B3L&referer=myapp";


    private Toolbar tlb_checkloc;

    private RecyclerView recvv_checkloc;

    private ItemAdapter adapter;

    private List<ItemBean> itemList = new ArrayList<>();

    private TextView text_takephoto;

    private FloatingActionButton btn_addloc;

    private LocationClient client = null;

    private MyLocationListener myLocationListener = new MyLocationListener();

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;



    private void bd09TOgcj02()
    {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lon = z * Math.cos(theta);
        lat = z * Math.sin(theta);
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
             lat = bdLocation.getLatitude();
             lon = bdLocation.getLongitude();

            bd09TOgcj02();
            float radius = bdLocation.getRadius();

            String coorType = bdLocation.getCoorType();

            int errorCode = bdLocation.getLocType();
        }
    }

    private void initUI()
    {
        recvv_checkloc = findViewById(R.id.recv_addloc);
        tlb_checkloc = findViewById(R.id.tlb_checkloc);
        btn_addloc = findViewById(R.id.lfbtn_add_loc);
        text_takephoto = findViewById(R.id.text_loc_takephoto);
        webView = findViewById(R.id.webView);
        setSupportActionBar(tlb_checkloc);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setLocation()
    {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd09ll");

        option.setScanSpan(1000);

        option.setOpenGps(true);

        client.setLocOption(option);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loc);

        initUI();

        client = new LocationClient(getApplicationContext());
        client.registerLocationListener(myLocationListener);


        /*权限的申请判断*/
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(AddLocActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(AddLocActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(AddLocActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty())
        {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(AddLocActivity.this, permissions, 1);
        }
        else
        {
            //开始执行
            setLocation();
            client.start();
        }
        WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);

//        webView.setWebViewClient(new WebViewClient()
//        {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url)
//            {
//                if (!url.startsWith("https://callback")) {
//                    view.loadUrl(url);
//                }
//                else {
//                    try {
//
//                        //转utf-8编码
//                        String decode = URLDecoder.decode(url, "UTF-8");
//
//                        //转uri，然后根据key取值
//                        Uri uri = Uri.parse(decode);
//                        String latng = uri.getQueryParameter("latng");//纬度在前，经度在后，以逗号分隔
//                        String[] split = latng.split(",");
//                        String lat = split[0];//纬度
//                        String lng = split[1];//经度
//                        String address = uri.getQueryParameter("addr");//地址
//
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return true;
//            }
//        });



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recvv_checkloc.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(itemList);

        recvv_checkloc.setAdapter(adapter);
        adapter.setItemClickListener(this);
        Update();
        btn_addloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AddLocActivity.this,"经度：" + lon + " 纬度："+ lat,Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "经度：" + lon + " 纬度："+ lat);
                setAlterDialog(AddLocActivity.this,"签到完成","签到成功，开始检查？");
                Update();
            }
        });
        text_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLocActivity.this,TakePhotoActivity.class);
                startActivity(intent);
            }
        });

    }
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String Url = "https://m.amap.com/picker/?keywords=写字楼,小区,学校&zoom=15&center="+lon+","+lat+"&radius=200&total=20&key=160d1eaca139c23db4e6f46d87f8e4bc";
            webView.loadUrl(Url);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) //利用分支语句进行判断
        {
            case 1:
                if (grantResults.length > 0)
                {
                    for (int result : grantResults)  //在grantResult中提取数据
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                    }
                    //开始执行
                    setLocation();
                    client.start();
                }
                else
                {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }

    }

    private void Update()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Bitmap checkedaddr = BitmapFactory.decodeResource(getResources(),R.mipmap.proj_addr);
        Bitmap signIn = BitmapFactory.decodeResource(getResources(),R.mipmap.signloc);
        ItemBean item = new ItemBean("外勤签到",""+dateFormat.format((new Date())),checkedaddr,signIn);

        itemList.add(item);
        adapter.notifyItemChanged(itemList.size()-1);

        adapter.notifyItemChanged(0,itemList.size());
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

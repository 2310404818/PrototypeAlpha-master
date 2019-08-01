package com.swj.prototypealpha.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 定位界面
 * 实现定位、附近相关兴趣点的列表，相机签到的跳转
 */

public class SignedActivity extends AppCompatActivity implements View.OnClickListener, AMapLocationListener, LocationSource, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener {

    private MapView mMapView;
    private ListView lv_view;
    private RelativeLayout rl_title;
    private EditText et_key;
    private Button btn_search;
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_curdate;
    private TextView text_takephoto;
    private TextView tv_commit;
    private AMap mAmap;
    private boolean isFirstLoc = true;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private String distance;
    private long firstTime;
    private ProgressDialog progDialog;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiSearch.SearchBound searchBound;
    private String city;
    private ArrayList<PoiItem> poiItems;
    private ArrayList<PoiItem> poiItems1;
    private ArrayList<Marker> markers = new ArrayList<>();
    private NearbyAdapter mAdapter;
    private String Latitude = "112.946615";
    private String longitude = "28.179526";
    private String posName="湖南大学";
    String deepType = "";
    private LatLng latlng;
    private String key_search;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed);
        mMapView = findViewById(R.id.bmapView);
        lv_view = findViewById(R.id.lv_view);
        rl_title = findViewById(R.id.rl_title);
        et_key = findViewById(R.id.et_key);
        btn_search = findViewById(R.id.btn_search);
        tv_back = findViewById(R.id.tv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_curdate = findViewById(R.id.tv_curdate);
        text_takephoto = findViewById(R.id.text_loc_takephoto);
        tv_commit = findViewById(R.id.tv_commit);

        btn_search.setOnClickListener(this);
        text_takephoto.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initMap();
        checkLocationPermission();//初始化请求权限，存储权限
    }

    private void initMap() {
        if (mAmap == null) {
            mAmap = mMapView.getMap();
            //开始定位
            initLoc();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置圆形的边框颜色
        mAmap.setMyLocationStyle(myLocationStyle);

        //设置定位监听
        mAmap.setLocationSource(this);
        // 是否显示定位按钮
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        mAmap.setMyLocationEnabled(true);
    }

    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
    }

    private void checkLocationPermission() {
        // 检查是否有定位权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(SignedActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY","没有权限");
            requestPermission(LOCATION_PERMISSION_CODE);
        } else {
            //已经获得权限，则执行定位请求。
    //        Toast.makeText(SignedActivity.this, "已获取定位权限",Toast.LENGTH_SHORT).show();

            startLocation();

        }
    }

    private void requestPermission(int permissioncode) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (permission !=null){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignedActivity.this,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if(permissioncode == LOCATION_PERMISSION_CODE) {
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
                            R.string.location_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                }


            } else {
                Log.i("MY","返回false 不需要解释为啥要权限，可能是第一次请求，也可能是勾选了不再询问");
                ActivityCompat.requestPermissions(SignedActivity.this,
                        new String[]{permission}, permissioncode);
            }
        }
    }

    /**
     * 开始定位
     */
    private void startLocation(){
        // 启动定位
        mLocationClient.startLocation();
        Log.i("MY","startLocation");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && mListener != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 显示我的位置
                mListener.onLocationChanged(amapLocation);
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm E");
                Date date = new Date(amapLocation.getTime());
                System.out.println(amapLocation.getTime());
                tv_curdate.setText(df.format(date) + "");
                city = amapLocation.getCity();
                //设置第一次焦点中心
                latlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                Latitude = String.valueOf(amapLocation.getLatitude());
                longitude = String.valueOf(amapLocation.getLongitude());
                posName = amapLocation.getPoiName();
                System.out.println("经纬度及地名"+"     "+Latitude+"   "+longitude+"    "+posName);
                mAmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17), 1000, null);
                key_search = amapLocation.getPoiName();

                getNearbyInfo(key_search);

//                //设置缩放级别
                mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
                //点击定位按钮 能够将地图的中心移动到定位点
                mListener.onLocationChanged(amapLocation);
                //将地图移动到定位点
                mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    et_key.setHint(key_search);
                    //添加图钉
                    marker = mAmap.addMarker(getMarkerOptions(amapLocation));
                    System.out.println("marker的值："+marker);
                    markers.add(marker);
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();

        //图标
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.mine_locate));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
//        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
//        options.period(60);
        return options;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.startLocation();
    }

    private void getNearbyInfo(String keyWord) {
        if ((keyWord == null || keyWord.isEmpty())) {
            return;
        }
//        showProgressDialog();
        mAmap.setOnMapClickListener(null);//搜索时清除掉map点击事件
        //第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, deepType, city);
        query.setPageSize(15);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
        //点附近2000米内的搜索结果
        LatLonPoint lp = new LatLonPoint(latlng.latitude, latlng.longitude);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 300, true));
        poiSearch.searchPOIAsyn();//开始搜索
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                markers = mAdapter.getmarkers();
                if (imm.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                key_search = et_key.getText().toString().trim();
                getNearbyInfo(key_search);
                break;
            case R.id.text_loc_takephoto:
                Intent intent = new Intent(SignedActivity.this,PhotoSignInActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_commit:
                posName = mAdapter.getPosName();
                SignedRequest(Latitude,longitude,posName);
                break;
        }
    }

    public void DialogFor(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("确定发起检查？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(SignedActivity.this,StartActivity.class);
                        startActivity(intent2);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {
            Log.i("---", "查询结果:" + i);
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果。
                poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                System.out.println("markers的内容："+markers);
                mAdapter = new NearbyAdapter(this, poiItems,posName,mAmap,markers);
                System.out.println("经纬度及地名"+"     "+Latitude+"   "+longitude+"    "+posName);
                lv_view.setAdapter(mAdapter);
            }
        } else if (i == 27) {
            Log.e("---", "error_network");
        } else if (i == 32) {
            Log.e("---", "error_key");
        } else {
            Log.e("---", "error_other：" + i);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latlng = cameraPosition.target;
        mAmap.clear();
        lv_view.setAdapter(mAdapter);
        getNearbyInfo(key_search);
    }

    private String getDistance() {
//        LatLng target_local = new LatLng(39.909604, 116.39722);//天安门
        LatLng target_local = new LatLng(40.109604, 116.29722);
        float realDistance = AMapUtils.calculateLineDistance(target_local, latlng);
        if (realDistance < 1000) {
            distance = realDistance + " 米";
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            distance = df.format(realDistance / 1000) + " km";
        }
        return distance;
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索中。。。");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.onDestroy();
        super.onDestroy();
    }
    //删除大于100米的目标
    public static void removeloc(ArrayList<PoiItem> poiItems, int target){
        Iterator<PoiItem> iter = poiItems.iterator();
        while (iter.hasNext()) {
            PoiItem item = iter.next();
            if (item.getDistance()>target) {
                iter.remove();
            }
        }
    }

    //服务器提交签到记录
    public  void SignedRequest(final String Latitude, final String longitude, final String posName) {
        //请求地址
        String url = "http://47.102.119.140:8080/mobile_inspection_war/Login";
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
                                DialogFor();
                            }
                            else if (result.equals("failed")){
                                Toast.makeText(SignedActivity.this,"请重试",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //做自己的登录失败操作，如Toast提示
                                Toast.makeText(SignedActivity.this,"请求异常",Toast.LENGTH_SHORT).show();
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
                Log.e("TAG", error.getMessage(), error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Latitude", Latitude);  //注⑥
                params.put("Longitude", longitude);
                params.put("posName", posName);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}

package com.swj.prototypealpha.activity;


import android.Manifest;
import android.arch.lifecycle.GeneratedAdapter;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MethodCallsLogger;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.ChargePerson;
import com.swj.prototypealpha.swj.LookupFragment;
import com.swj.prototypealpha.swj.Picture;
import com.swj.prototypealpha.swj.SignAdapter;
import com.swj.prototypealpha.swj.util.searchView.SignatureView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 手写签名界面
 * @author qianliang
 *
 */
public class ChooseCheckPerson extends AppCompatActivity implements View.OnClickListener {

    private SignatureView mSignView;
    private Spinner sp_name;
    private FloatingActionButton my_fab;
    RecyclerView recv_photo;
    android.support.v7.widget.Toolbar tlb_mycheck;
    public static int flag=0;
    public static ChargePerson[] name = new ChargePerson[6];
    String[] mItems;
    String path;
    int number=0;
    public  static  Picture[]     picture = new Picture[6];
    public Picture  mypicture;
    public ChargePerson myname;
    private static  List<ChargePerson> nameList = new ArrayList<>();
    public static   List<Picture> signList    = new ArrayList<>();


    private ArrayAdapter<String> adapter;
    public static SignAdapter signadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        flag=0;
        setContentView(R.layout.activity_choose_check_person);
        mItems = getResources().getStringArray(R.array.datalist);
        initView();
        initUI();
        String string = "建设单位负责人";
        myname =new ChargePerson(string);
        name[number] = myname;

        //spinner选择
        sp_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String str = (String) sp_name.getItemAtPosition(position);
                    myname= new ChargePerson(str);
                    name[number] = myname;
             //       Log.d(String.valueOf(myname),"好几代皇帝哦啊实打实降低哦案件哦");
             //       Log.d(str,"基地啊设计大奖偶爱睡大觉降低哦按实际");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String string = "建设单位负责人";
                myname =new ChargePerson(string);
                name[number] = myname;
           //     Log.d(string,"呼唤大顶哈顶哈圣诞节安静的环境卡是大家");
            }
        });
        my_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                ChooseCheckPerson.this.setResult(110,intent);

                flag=1;
                finish();
            }
        });

    }

    /**
     * 初始化写字板、下拉控件、recycleView控件
     * 设定rec控件布局和绑定适配器
     * 绑定下拉控件适配器和数据
     *
     */
    private void initView() {

        mSignView = findViewById(R.id.sv_sign_view);
        sp_name = findViewById(R.id.sp_name);
        recv_photo=findViewById(R.id.recv_lookup_picture);
        my_fab = findViewById(R.id.my_fab);

        recv_photo.setLayoutManager(new GridLayoutManager(this,3));
        signadapter= new SignAdapter(signList,nameList);
        recv_photo.setAdapter(signadapter);

        findViewById(R.id.btn_clear).setOnClickListener(ChooseCheckPerson.this);
        findViewById(R.id.btn_sure).setOnClickListener(ChooseCheckPerson.this);


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mItems);
        sp_name.setAdapter(adapter);
        sp_name.setSelection(0,true);
    }

    /**
     * 清除签字
     */
    private void clearClick() {
        mSignView.clear();

    }

    /**
     * 保存签名图片并展示
     */
    private void sureClick() {
        //  保存签名图片
        Bitmap imageBitmap = mSignView.getCachebBitmap();

        path = saveFile(imageBitmap);
        mypicture  = new Picture(BitmapFactory.decodeFile(path));
        picture[number] = mypicture;
        if (!TextUtils.isEmpty(path) ) {
        //    Log.d(String.valueOf(myname),"就是这个，你看着办吧");
            signList.add(mypicture);
            nameList.add(myname);
            int len = signList.size();
            signadapter.notifyItemChanged(len - 1);
            signadapter.notifyItemChanged(0, len);
            number++;

        }
        clearClick();
    }

    /**
     * 创建手写签名文件
     *
     * @return 照片地址
     */
    private String saveFile(Bitmap bitmap) {


        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory() + "/WttSingDemo/";
            File file = new File(sign_dir);
            if (!file.exists())
                file.mkdirs();

            String name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());

            _path = sign_dir + "IMG_" + name + ".jpg";
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }

    /**
     * 所有控件的点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clearClick();
                break;

            case R.id.btn_sure:
                sureClick();
                break;

        }

    }
    private void initUI()
    {
        tlb_mycheck = findViewById(R.id.tlb_check);
        setSupportActionBar(tlb_mycheck);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

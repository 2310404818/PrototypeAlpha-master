package com.swj.prototypealpha.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.ChargePerson;
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

/**
 * 手写签名界面
 * @author qianliang
 *
 */
public class ChooseCheckPerson extends AppCompatActivity implements View.OnClickListener {

    private SignatureView mSignView;
    private Spinner sp_name;
    TextView tx_complete;
    RecyclerView recv_photo;
    android.support.v7.widget.Toolbar tlb_mycheck;
    public static int flag=0;
    private String person;
    String[] mItems;
    String path;
    String _path;
    String sign_dir;
    public Picture  mypicture;
    public ChargePerson myname;
    private static  List<ChargePerson> nameList = new ArrayList<>();
    public static   List<Picture> signList    = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    public static SignAdapter signadapter;
    MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        setContentView(R.layout.activity_choose_check_person);
        mItems = getResources().getStringArray(R.array.datalist);
        initView();
        initUI();
        initML();
        String string = "建设单位负责人";
        person  = string;
        myname =new ChargePerson(string);
        tx_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                ChooseCheckPerson.this.setResult(110,intent);
                flag=nameList.size();
                ChooseCheckPerson.this.finish();
            }
        });
        //spinner选择
        sp_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String str = (String) sp_name.getItemAtPosition(position);
                    person = str;
                    myname= new ChargePerson(str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                String str = "建设单位负责人";
                person =str;
                myname= new ChargePerson(str);
            }
        });
        initAutograph();
        signadapter.setOnItemClickListener(new SignAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                DialogFor(position);

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
        tx_complete =findViewById(R.id.tx_complete);
        mSignView = findViewById(R.id.sv_sign_view);
        sp_name = findViewById(R.id.sp_name);
        recv_photo=findViewById(R.id.recv_lookup_picture);
        tx_complete.setClickable(true);

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
     * 获取对应目录下的文件
     */
    private void initAutograph(){
        nameList.clear();
        signList.clear();
        myApplication = (MyApplication) getApplication();
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"autograph");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        if (!dir1.exists()){
            dir1.mkdir();
        }
        File dir = new File(dir1,datenow);
        if (!dir.exists()){
            dir.mkdir();
        }
        String name = dir.getName();
        File dir11 = new File(dir1,name);
        File[] files = dir11.listFiles();
        if (files!=null){
            for (File file : files) {
                String nameForMe = file.getName();
                ChargePerson myNameFor = new ChargePerson(nameForMe);
                nameList.add(myNameFor);
                Picture picture = new Picture(BitmapFactory.decodeFile(file.getAbsolutePath()));
                signList.add(picture);
            }
        }
    }

    /**
     * 删除功能实现
     * 内存和界面上
     * 弹出框
     */
    public void DialogFor(final int position){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("确定发起签名？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Picture picture = signList.get(position);
                        Log.d(String.valueOf(picture.getImageID()),"几家大");
                        ChargePerson chargePerson =nameList.get(position);
                        String name = chargePerson.getName();
                        Log.d(name,"较低矮");
                        File file = new File(_path,name);
                        Log.d(_path,"几家大");
                        Log.d(String.valueOf(file),"口大家");
                        file.delete();
                        nameList.remove(position);
                        signList.remove(position);
                        signadapter.notifyDataSetChanged();

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



    /**
     * 保存签名图片并展示
     */
    private void sureClick() {

        //  保存签名图片
        Bitmap imageBitmap = mSignView.getCachebBitmap();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=3;//直接设置它的压缩率，表示1/2
        path = saveFile(imageBitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path,options);
        }catch (Exception e){
            e.printStackTrace();
        }
        mypicture  = new Picture(bitmap);
        if (!TextUtils.isEmpty(path) ) {
            signList.add(mypicture);
            nameList.add(myname);
            int len = signList.size();
            signadapter.notifyItemChanged(len - 1);
            signadapter.notifyItemChanged(0, len);
        }
        clearClick();
    }

    /**
     * 目录建造
     */
    public void initML(){
        myApplication = (MyApplication) getApplication();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        File file1 = new File(myApplication.getFile(),myApplication.getProjectName()+"autograph");
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file = new File(file1,datenow);
        if (!file.exists())
            file.mkdirs();
        sign_dir = file.getAbsolutePath();
        _path = sign_dir;
    }
    /**
     * 创建手写签名文件
     *
     * @return 照片地址
     */
    private String saveFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        _path = null;
        String myPath = null;
        try {
            initML();
            String picName = person+".jpg";
            File afile = new File(_path,picName);
            myPath = afile.getAbsolutePath();
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path,picName)).write(photoBytes);
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
        return myPath;
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

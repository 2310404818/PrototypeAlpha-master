package com.swj.prototypealpha.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.PersonalItemView;
import com.swj.prototypealpha.oyjz.PersonalinfoActivity;
import com.swj.prototypealpha.oyjz.ResetPasswordActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;

/**
 * 个人中心界面
 * 查看个人信息
 * 修改个人密码
 * 退出登陆
 */
public class MainFragment_R extends Fragment {

    private PersonalItemView right_name;
    private PersonalItemView right_passwordchange;
    private Button right_logout;
    private ImageView right_top,iv_touxiang;
    private TextView tvr_name,tvr_phone;
    private PopupWindow pw;
    private String imgPath;
    private File outputImage;
    private static Bitmap myBitmap;
    private RequestParams params = new RequestParams();
    private String encodedString;
    private static String path = "/sdcard/myHead/";// sd路径
    public static final int                  TAKE_PHOTO  = 1;
    public static       Uri                  imageUri;
    private static final int RESULT_LOAD_IMG = 0;
    private Bitmap bitmap;
    MyApplication myApplication;

    private void initUI () {
        right_name = getActivity().findViewById(R.id.right_name);
        right_passwordchange = getActivity().findViewById(R.id.right_passwordchange);
        right_logout = getActivity().findViewById(R.id.right_logout);
        right_top = getActivity().findViewById(R.id.right_top);
        iv_touxiang = getActivity().findViewById(R.id.iv_touxiang);
        tvr_name = getActivity().findViewById(R.id.tvr_name);
        tvr_phone = getActivity().findViewById(R.id.tvr_phone);
        myApplication = (MyApplication) getActivity().getApplication();
        tvr_phone.setText(myApplication.getTell());
        tvr_name.setText(myApplication.getName());

    }

    /**
     * 图片载入
     * 高斯模糊
     * 圆形头像
     */
    private void initData(){
        myApplication = (MyApplication) getActivity().getApplication();
        String name = myApplication.getTell();
            Glide.with(getActivity()).load("http://47.102.119.140:8080/pic/"+name+".jpg")
                    .error(R.drawable.toux)
                    .bitmapTransform(new BlurTransformation(getActivity(),25,3),new CenterCrop(getActivity()))
                    .into(right_top);

            Glide.with(getActivity()).load("http://47.102.119.140:8080/pic/"+name+".jpg")
                    .error(R.drawable.toux)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(iv_touxiang);
    }

    private void initData1(String url){
        Glide.with(getActivity()).load(url)
                .bitmapTransform(new BlurTransformation(getActivity(),25,3),new CenterCrop(getActivity()))
                .into(right_top);

        Glide.with(getActivity()).load(url)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(iv_touxiang);

    }


    /**
     * 点击事件
     */
    private void setOnclickLisener () {
        right_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getActivity(), PersonalinfoActivity.class);
                startActivity(intent);
            }
        });
        right_passwordchange.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v) {
                Intent intent=new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        right_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                String log = "NO";
                intent.putExtra("log",log);
                startActivity(intent);
            }
        });
        iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditphotoWindow(v);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //调取系统照相机
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        !=
                        PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {

        }
        myApplication = (MyApplication) getActivity().getApplication();
        Bitmap bt = BitmapFactory.decodeFile(path + myApplication.getTell()+".png");
        initUI();
        if (bt!=null)
        {
            Glide.with(getActivity()).load(path + myApplication.getTell()+".png")
                    .bitmapTransform(new BlurTransformation(getActivity(),25,3),new CenterCrop(getActivity()))
                    .into(right_top);

            Glide.with(getActivity()).load(path + myApplication.getTell()+".png")
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(iv_touxiang);
        }
        else
        {
            initData();
        }

        setOnclickLisener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_fragment__r, container, false);
    }

    /**
     * 底部弹出框
     * 头像的上传
     * 拍照和相册
     * @param view
     */
    private void showEditphotoWindow(View view){
        View contentView=getActivity().getLayoutInflater().inflate(R.layout.popup_windows_title_image, null);
        pw=new PopupWindow(contentView,getActivity().getWindowManager().getDefaultDisplay().getWidth(), getActivity().getWindowManager().getDefaultDisplay().getHeight(), true);

       // pw = new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT,190,true);
        //点击空白处消失弹框
        pw.setFocusable(true);
        pw.setTouchable(true);

        //设置popupwindow弹出动画
        pw.setAnimationStyle(R.style.popupwindow_anim_style);
        //设置popupwindow背景
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);

        //处理popupwindow
        popupwindowselectphoto(contentView);
    }

    //初始化控件和控件的点击事件
    private void popupwindowselectphoto(View contentView) {
        TextView tv_select_pic=(TextView) contentView.findViewById(R.id.tv_photo);
        TextView tv_pai_pic=(TextView) contentView.findViewById(R.id.tv_photograph);
        TextView tv_cancl=(TextView) contentView.findViewById(R.id.tv_cancle);
        LinearLayout layout=(LinearLayout) contentView.findViewById(R.id.dialog_ll);
        tv_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
                pw.dismiss();
            }
        });
        tv_pai_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                pw.dismiss();
            }
        });
        tv_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    pw.dismiss();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    pw.dismiss();
                }
            }
        });


    }
    /**
     * 拍照和相册实现
     */
    public void loadImage() {
        //这里就写了从相册中选择图片，相机拍照的就略过了
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    /**
     * 图片保存到本地
     *
     */
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        myApplication = (MyApplication) getActivity().getApplication();
        FileOutputStream b = null;
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();// 创建文件夹
        }

        String fileName =path+ myApplication.getTell()+".png";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 相册相片选择结果
     * 照相机照相结果
     * 写到头像
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMG:
                try {
                    if (resultCode == RESULT_OK && null != data) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        // 获取游标
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgPath = cursor.getString(columnIndex);
                        cursor.close();
                        initData1(imgPath);
                        uploadImage();
                        Bitmap head=BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
                        setPicToView(head);
                    } else {
                        Toast.makeText(getActivity(), "你未选择图片",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "出现错误", Toast.LENGTH_LONG).show();
                }
                break;
            case TAKE_PHOTO:
                if (resultCode==Activity.RESULT_CANCELED){

                }
              else {
                    Glide.with(getActivity()).load(imageUri)
                            .bitmapTransform(new BlurTransformation(getActivity(),25,3),new CenterCrop(getActivity()))
                            .into(right_top);

                    Glide.with(getActivity()).load(imageUri)
                            .bitmapTransform(new CropCircleTransformation(getActivity()))
                            .into(iv_touxiang);
                    upLoadImage();
                    try {
                        Bitmap head=BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        setPicToView(head);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    }
        }
    }
    /**
     * 图片转bit64位
     */
    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
            };
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // 压缩图片
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Base64图片转码为String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
          //      prgDialog.setMessage("Calling Upload");
                // 将转换后的图片添加到上传的参数中
                params.put("image", encodedString);
                params.put("filename",myApplication.getTell());
                // 上传图片
                imageUpload();
            }
        }.execute(null, null, null);
    }

    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString1() {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
            };
            @Override
            protected String doInBackground(Void... params) {
                Bitmap bitmap = null;
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                try {
                   bitmap =
                            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri),null,options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // 压缩图片
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Base64图片转码为String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                //      prgDialog.setMessage("Calling Upload");
                // 将转换后的图片添加到上传的参数中
                params.put("image", encodedString);
                params.put("filename",myApplication.getTell());

                // 上传图片
                imageUpload();
            }
        }.execute(null, null, null);
    }
    /**
     * 上传图片
     *
     */
    //开始上传图片
    private void uploadImage() {
        if (imgPath != null && !imgPath.isEmpty()) {
            encodeImagetoString();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }
    private void upLoadImage(){
        try {
            Bitmap bitmap =
                    BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
            if (bitmap!=null){
                encodeImagetoString1();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void imageUpload() {
        String url = "http://47.102.119.140:8080/mobile_inspection_war/uploadImage.jsp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
          //      Toast.makeText(getActivity(), "upload success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 404) {
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // 当 Http 响应码'500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // 当 Http 响应码 404, 500
                else {
                    Toast.makeText(getActivity(), "Error Occured n Most Common Error: n1. Device " +
                                    "not connected to Internetn2. Web App is not deployed in App servern3." +
                                    " App server is not runningn HTTP Status code : " + statusCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void takePhoto () {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");

        String filename = dateFormat.format(date) + ".png";
        outputImage = new File(getActivity().getExternalCacheDir(), filename);
        try {
            if (outputImage.exists())
                outputImage.delete();
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24)
            imageUri = FileProvider.getUriForFile(getActivity(), "com.swj.prototypealpha" +
                    ".fileprovider", outputImage);
        else
            imageUri = Uri.fromFile(outputImage);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
}

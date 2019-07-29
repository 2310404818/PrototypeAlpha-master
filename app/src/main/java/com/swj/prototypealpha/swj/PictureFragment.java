package com.swj.prototypealpha.swj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.swj.prototypealpha.Enity.photoEntity;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.photoAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 发起检查
 * 照片工具类
 */
public class PictureFragment extends Fragment {

    public static final int                  TAKE_PHOTO  = 1;
    public static       Uri                  imageUri;
    public static       ImageAdapter         adapter;
    public static       List<Picture>        pictureList = new ArrayList<>();
    private final       String               TAG         = "PictureFragment";
    private             FloatingActionButton fabtn_picture;
    private             RecyclerView         recv_photo;
    private             File                 outputImage;
    private             RequestParams        params = new RequestParams();
    private             String               encodedString;
    private             File[]               files = null;
    private             File                 dir ;
    private             ArrayList<photoEntity> photoEntities;
    private             photoAdapter         mydapter;
    private             String               myname;
    MyApplication       myApplication;


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree (Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabtn_picture = getActivity().findViewById(R.id.fabtn_takephoto);
        myApplication = (MyApplication) getActivity().getApplication();
        //根据住建执法一级目录建立二级目录，三级目录
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"checkPhoto");
        if (!dir1.exists()){
            dir1.mkdir();
        }
        dir = new File(dir1,datenow);
        if (!dir.exists()){
            dir.mkdir();
        }
        photoEntities= initPhoto();
        fabtn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                takePhoto();
            }
        });
        recv_photo = getActivity().findViewById(R.id.recv_photo);
        recv_photo.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recv_photo.setLayoutManager(layoutManager);
        mydapter = new photoAdapter(getActivity().getApplicationContext(),photoEntities);
        recv_photo.setAdapter(mydapter);
     //   recv_photo.setOnClickListener();
        mydapter.setOnItemClickListener(new photoAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                DialogFor(position);

            }
        });
    }

    /**
     * 删除功能实现
     * 内存和界面上
     * 弹出框
     */
    public void DialogFor(final int position){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定发起签名？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // mydapter.notifyDataSetChanged();
                        photoEntity photo =photoEntities.get(position);
                        File file = new File(photo.getPath());
                        file.delete();
                        photoEntities.remove(position);
                        mydapter.notifyDataSetChanged();

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
     * 删除照片
     * 服务器
     *
     */

    /**
     * 获取对应目录下的文件
     */
    private ArrayList<photoEntity> initPhoto(){
        ArrayList<photoEntity> photoEntities = new ArrayList<>();
        pictureList.clear();
        myApplication = (MyApplication) getActivity().getApplication();
        String name = dir.getName();
        //先进入二级目录，在进入三级目录加载三级目录下所有文件
        File myDir = new File(myApplication.getFile(),myApplication.getProjectName()+"checkPhoto");
        File dir11 = new File(myDir,name);
        files = dir11.listFiles();
        if (files!=null){
            for (File file : files) {
                photoEntity photoEntity = new photoEntity();
                photoEntity.setPath(file.getAbsolutePath());
                photoEntities.add(photoEntity);
            }
        }

        return photoEntities;
    }
    /**
     * 读取图片的旋转的角度
     * <p>
     * 图片绝对路径
     *
     * @return 图片的旋转角度
     */
    private int getBitmapDegree () {
        int degree = 0;
        try {
            InputStream inputStream = new FileInputStream(outputImage);
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(inputStream);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    private void takePhoto () {
        myApplication= (MyApplication) getActivity().getApplication();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        String filename = dateFormat.format(date) + ".jpg";
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = date1.format(date);
        File dir1 = new File(myApplication.getFile(),myApplication.getProjectName()+"checkPhoto");
        if (!dir1.exists()){
            dir1.mkdir();
        }
        dir = new File(dir1,datenow);
        if (!dir.exists()){
            dir.mkdir();
        }
        outputImage = new File(dir, filename);
        if (Build.VERSION.SDK_INT >= 24)
            imageUri = FileProvider.getUriForFile(getActivity(), "com.swj.prototypealpha.fileprovider",outputImage);
        else
            imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode==Activity.RESULT_CANCELED){

                }
                else {
                        photoEntity photo = new photoEntity() ;
                        photo.setPath(outputImage.getAbsolutePath());
                        photoEntities.add(photo);
                        mydapter.notifyDataSetChanged();
                        upLoadImage(imageUri);
                }
                break;
            default:
                break;
        }
    }

    private void upLoadImage(Uri imageUri){
        try {
            Bitmap bitmap =
                    BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
            if (bitmap!=null){
                encodeImagetoString1(imageUri);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString1(final Uri imageUri) {
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
                switch (getBitmapDegree()) {
                    case 90:
                        bitmap = rotateBitmapByDegree(bitmap, 90);
                        break;
                    case 180:
                        bitmap = rotateBitmapByDegree(bitmap, 180);
                        break;
                    case 270:
                        bitmap = rotateBitmapByDegree(bitmap, 270);
                        break;
                    default:
                        break;
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
                myApplication = (MyApplication) getActivity().getApplication();
                Date newTime = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdx = new SimpleDateFormat("HH-mm-ss");
                params.put("image", encodedString);
                params.put("projectName",myApplication.getProjectName());
                params.put("address",myApplication.getAddress());
                params.put("date",sdf.format(newTime));
                params.put("photoName",sdx.format(newTime));
                // 上传图片
                imageUpload();
            }
        }.execute(null, null, null);
    }
    public void imageUpload() {
        String url = "http://47.102.119.140:8080/mobile_inspection_war/uploadCheckPhoto.jsp";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
           //     Toast.makeText(getActivity(), "upload success", Toast.LENGTH_LONG).show();
                Log.d("上传成功","没毛病");
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
}

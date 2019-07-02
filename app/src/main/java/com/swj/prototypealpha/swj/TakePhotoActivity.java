package com.swj.prototypealpha.swj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.swj.prototypealpha.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar tlb_upphoto;

    private ImageButton btn_upphoto_takephoto;

    private ImageView image_picture;

    private FloatingActionButton fla_btn_takephoto;

    private static final int TAKE_PHOTO = 1;

    private Uri m_imageUri;

    private File outputImage;

    private void initView()
    {
        btn_upphoto_takephoto = findViewById(R.id.image_takephoto);
        image_picture = findViewById(R.id.show_photo);
        fla_btn_takephoto = findViewById(R.id.fla_btn_submit);
    }

    private void setButtonClick()
    {
        fla_btn_takephoto.setOnClickListener(this);                                               //获取验证码按钮事件
        btn_upphoto_takephoto.setOnClickListener(this);                                                //确定按钮事件
    }

    private void setToolBar()
    {
        tlb_upphoto = findViewById(R.id.tlb_takephoto);
        setSupportActionBar(tlb_upphoto);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        initView();
        setToolBar();
        setButtonClick();
    }


    private void takephoto()
    {
        outputImage = new File(getExternalCacheDir(),"personal_picture.jpeg");  //应用关联目录下存放拍摄照片
        try
        {
            if(outputImage.exists())
                outputImage.delete();
            outputImage.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24)
            m_imageUri = FileProvider.getUriForFile(TakePhotoActivity.this,"com.swj.prototypealpha.fileprovider",outputImage);
        else
            m_imageUri = Uri.fromFile(outputImage);

        Intent intent =  new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,m_imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if(requestCode == RESULT_FIRST_USER)
                {
                    try
                    {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(m_imageUri));
                        switch (getBitmapDegree())
                        {
                            case 90:
                                bitmap = rotateBitmapByDegree(bitmap,90);
                                break;
                            case 180:
                                bitmap = rotateBitmapByDegree(bitmap,180);
                                break;
                            case 270:
                                bitmap = rotateBitmapByDegree(bitmap,270);
                                break;
                            default:
                                break;
                        }
                        image_picture.setImageBitmap(bitmap);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree() {
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

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     * 需要旋转的图片
     * @param degree
     * 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try
        {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        catch (OutOfMemoryError e) {
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.image_takephoto:
                    takephoto();
                break;
            case R.id.fla_btn_submit:
                finish();
                break;
        }
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

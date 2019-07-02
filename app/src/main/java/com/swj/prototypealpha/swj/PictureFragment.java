package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swj.prototypealpha.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_FIRST_USER;


public class PictureFragment extends Fragment {

    public static final int                  TAKE_PHOTO  = 1;
    public static       Uri                  imageUri;
    public static       ImageAdapter         adapter;
    public static       List<List<Picture>>  pictureList = new ArrayList<>();
    private final       String               TAG         = "PictureFragment";
    private             FloatingActionButton fabtn_picture;
    private             RecyclerView         recv_photo;
    private             File                 outputImage;
    private             int                  itemPosition;

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
        List<Picture> pictures = new ArrayList<>();
        pictureList.add(pictures);
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabtn_picture = getActivity().findViewById(R.id.fabtn_takephoto);

        fabtn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                takePhoto();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recv_photo = getActivity().findViewById(R.id.recv_photo);
        recv_photo.setLayoutManager(linearLayoutManager);
        adapter = new ImageAdapter(getContext(), pictureList, null);
        recv_photo.setAdapter(adapter);
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
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");

        String filename = dateFormat.format(date) + ".jpg";
        outputImage = new File(getActivity().getExternalCacheDir(), filename);
        try {
            if (outputImage.exists())
                outputImage.delete();
            outputImage.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "takephoto: " + "IO异常");
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

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (requestCode == RESULT_FIRST_USER) {
                    try {
                        Bitmap bitmap =
                                BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
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
                        Picture picture = new Picture(bitmap);
                        pictureList.get(0).add(picture);
                        int len = pictureList.size();
                        adapter.notifyItemChanged(len - 1);
                        adapter.notifyItemChanged(0, len);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}

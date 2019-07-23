package com.swj.prototypealpha.swj;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 手写签名对应的一个实体类
 */
public class Picture implements Parcelable
{
    private Bitmap imageID;

    public Picture(Bitmap imageID)
    {
        this.imageID = imageID;
    }

    protected Picture(Parcel in) {
        imageID = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public  Bitmap getImageID() {
        return imageID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageID, flags);
    }
}

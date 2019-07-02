package com.swj.prototypealpha.swj;

import android.graphics.Bitmap;

public class Picture
{
    private Bitmap imageID;

    public Picture(Bitmap imageID)
    {
        this.imageID = imageID;
    }

    public  Bitmap getImageID() {
        return imageID;
    }

}

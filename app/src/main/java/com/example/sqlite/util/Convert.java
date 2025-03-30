package com.example.sqlite.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Convert {

    public static byte[] convertToBytes(Bitmap bitmap){
        if(bitmap == null){
            throw new IllegalArgumentException("Bitmap cannot be null");
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }

    public static Bitmap convertToBitmap(byte[] image){
        if(image != null && image.length > 0){
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        return null;
    }
}

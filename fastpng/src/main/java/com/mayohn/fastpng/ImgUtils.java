package com.mayohn.fastpng;

import android.graphics.Bitmap;

import java.io.File;

public class ImgUtils {

    public static String writeImage(String path, Bitmap bit) {
        if (bit == null && bit.isRecycled()) return null;
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return write(path, bit);
    }

    public static Bitmap readImage(String path) {
        File file = new File(path);
        if (!file.exists()) return null;
        return read(path);
    }

    private static native String write(String path, Bitmap bit);

    private static native Bitmap read(String path);

    static {
        System.loadLibrary("fastpng");
    }
}

package com.suwell.fastpng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.mayohn.fastpng.ImgUtils;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LibPngActivity";
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        Bitmap bitmap = getAssetsPng();
        String path = getCacheDir().getAbsolutePath() + "/cache/testlibPng.png";
        long start = System.currentTimeMillis();
        ImgUtils.writeImage(path, bitmap);
        long start1 = System.currentTimeMillis();
        Log.i(TAG, "save time: " + (start1 - start));
        bitmap = ImgUtils.readImage(path);
        long start2 = System.currentTimeMillis();
        Log.i(TAG, "save time: " + (start2 - start1));
        imageView.setImageBitmap(bitmap);
    }
    private Bitmap getAssetsPng() {
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream is = assetManager.open("1624521569195.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}